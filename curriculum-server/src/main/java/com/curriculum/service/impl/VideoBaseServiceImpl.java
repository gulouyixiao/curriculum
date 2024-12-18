package com.curriculum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.constant.MessageConstant;
import com.curriculum.context.AuthenticationContext;
import com.curriculum.exception.CurriculumException;
import com.curriculum.mapper.MediaFilesMapper;
import com.curriculum.mapper.VideoAuditMapper;
import com.curriculum.mapper.VideoBaseMapper;
import com.curriculum.model.dto.MovieDto;
import com.curriculum.model.dto.PageParams;
import com.curriculum.model.dto.VideoPageParams;
import com.curriculum.model.dto.VideoPublishDto;
import com.curriculum.model.po.MediaFiles;
import com.curriculum.model.po.VideoAudit;
import com.curriculum.model.po.VideoBase;
import com.curriculum.model.vo.PageResult;
import com.curriculum.model.vo.VideoAuditVO;
import com.curriculum.service.VideoBaseService;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 视频
 */
@Slf4j
@Service
public class VideoBaseServiceImpl extends ServiceImpl<VideoBaseMapper, VideoBase> implements VideoBaseService {
	@Autowired
	private VideoBaseMapper videoBaseMapper;

	@Autowired
	private MediaFilesMapper mediaFilesMapper;

	@Autowired
	private VideoAuditMapper videoAuditMapper;

	/**
	 * 视频条件分页查询
	 * @param videoPageParams
	 * @return
	 */
	public PageResult<VideoBase> PageQuery(VideoPageParams videoPageParams){

		if(videoPageParams.getVideoType() == null){
			CurriculumException.cast("视频类型不能为null");
		}

		LambdaQueryWrapper<VideoBase> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.select(VideoBase::getId,VideoBase::getUsername,VideoBase::getTitle,VideoBase::getDescription
		,VideoBase::getTags,VideoBase::getCover,VideoBase::getTimelength,VideoBase::getStyle,VideoBase::getGrade
		,VideoBase::getPlaybackVolume);

		//分离多个关键字
		String tags = videoPageParams.getTags();
		if(tags != null && !tags.isEmpty()){
			for (String s : tags.split(",")) {
				queryWrapper.like(VideoBase::getTags,s);
			}
		}
		queryWrapper.eq(VideoBase::getVideoType,videoPageParams.getVideoType());
		Page<VideoBase> ipage = new Page<>(videoPageParams.getPage(),videoPageParams.getPageSize());

		Page<VideoBase> videoBasePage = videoBaseMapper.selectPage(ipage, queryWrapper);

		return new PageResult(videoBasePage.getRecords(),videoBasePage.getTotal(), videoPageParams.getPage(), videoPageParams.getPageSize());
	}

	/**
	 * 添加番剧
	 * @param movieDto
	 * @param fileTime
	 * @param mediaFiles
	 */
	@Override
	public void addAnime(MovieDto movieDto, String fileTime, MediaFiles mediaFiles) {
		VideoBase videoBase = new VideoBase();
		videoBase.setMediaId(mediaFiles.getId());
		BeanUtils.copyProperties(mediaFiles,videoBase);
		videoBase.setGrade(movieDto.getGrade());
		videoBase.setParentid(Long.valueOf(movieDto.getParentId()));
		videoBase.setTitle(movieDto.getTitle());
		videoBase.setStartTime(LocalDateTime.now());
		videoBase.setTimelength(fileTime);
		videoBase.setThumbupCount(0L);
		videoBase.setCommentCount(0L);
		videoBase.setPlaybackVolume(0L);
		videoBase.setAuditStatus("202001");
		videoBase.setStatus("203001");
		videoBase.setUrl(mediaFiles.getUrl());

		videoBaseMapper.insert(videoBase);
	}


	/**
	 * 获取播放量排名前五的列表
	 * @param videoType 视频类型
	 * @param limit 获取记录的限制数量
	 * @return 播放量排名前x的信息列表
	 */
	@Override
	public List<VideoBase> recommend(String videoType, int limit) {
		LambdaQueryWrapper<VideoBase> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(VideoBase::getVideoType, videoType)
				.orderByDesc(VideoBase::getPlaybackVolume)
				.last("LIMIT " + limit);
		return this.list(queryWrapper);
	}

	@Override
	public List<VideoBase> show(String videoType, int limit) {
		LambdaQueryWrapper<VideoBase> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(VideoBase::getVideoType, videoType)
				.orderByDesc(VideoBase::getPlaybackVolume)
				.last("LIMIT " + limit);
		return this.list(queryWrapper);
	}

	/**
	 * 发布视频
	 * @param videoPublishDto
	 */
	@Override
	@Transactional
	public void videoPublish(VideoPublishDto videoPublishDto) {
		//查找视频的媒资文件
		MediaFiles mediaFiles = mediaFilesMapper.selectById(videoPublishDto.getMediaId());
		if(mediaFiles == null){
			CurriculumException.cast(MessageConstant.VIDEO_NOT_FOUND);
		}
		Long userId = AuthenticationContext.getContext();
		if(mediaFiles.getUserId() != userId){
			//视频发布与媒资上传者不是同一人
			CurriculumException.cast(MessageConstant.PERMISSION_DENIED);
		}

		//发布视频
		VideoBase videoBase = new VideoBase();
		BeanUtils.copyProperties(videoPublishDto,videoBase);
		videoBase.setParentid(null);
		videoBase.setUserId(userId);
		videoBase.setUrl(mediaFiles.getUrl());
		videoBase.setTimelength(mediaFiles.getTimelength());
		videoBase.setUsername(mediaFiles.getUsername());
		videoBase.setStartTime(LocalDateTime.now());
		videoBase.setCreateDate(LocalDateTime.now());
		videoBase.setGrade("1");
		videoBase.setAuditStatus("202002");
		videoBase.setStatus("203002");
		videoBase.setVideoType("001002");
		this.save(videoBase);

		//进入审核表
		VideoAudit videoAudit = new VideoAudit();
		videoAudit.setVideoId(videoBase.getId());
		videoAudit.setAuditStatus("202003");

		//todo 采用消息队列，进行审核 待完成
		videoAudit.setAuditStatus("202002");
		videoAudit.setId(1L);
		videoAudit.setAuditMind("无");
		videoAudit.setAuditDate(LocalDateTime.now());

		videoAuditMapper.insert(videoAudit);
	}




	/**
	 * 发布番剧
	 * @param videoPublishDto
	 */
	@Override
	public void animePublish(VideoPublishDto videoPublishDto) {
		//补全该番剧信息,正常可能每个都有各自的信息，这里不管了
		LambdaUpdateWrapper<VideoBase> updateWrapper = new LambdaUpdateWrapper<>();
		updateWrapper.eq(VideoBase::getParentid,videoPublishDto.getParentid())
				.set(VideoBase::getTitle,videoPublishDto.getTitle())
				.set(VideoBase::getTags,videoPublishDto.getTags())
				.set(VideoBase::getStyle,videoPublishDto.getStyle())
				.set(VideoBase::getCover,videoPublishDto.getCover())
				.set(VideoBase::getVideoType,"001003")
				.set(VideoBase::getStatus,"203002")
				.set(VideoBase::getAuditStatus,"202002")
				.set(VideoBase::getDescription,videoPublishDto.getDescription());
		this.update(updateWrapper);

		VideoBase videoBase = new VideoBase();
		BeanUtils.copyProperties(videoPublishDto,videoBase);
		videoBase.setMediaId(null);

		//进入审核表
		VideoAudit videoAudit = new VideoAudit();
		videoAudit.setVideoId(videoBase.getParentid());
		videoAudit.setAuditStatus("202003");

		//todo 采用消息队列，进行审核 待完成
		videoAudit.setAuditStatus("202002");
		videoAudit.setId(1L);
		videoAudit.setAuditMind("无");
		videoAudit.setAuditDate(LocalDateTime.now());

		videoAuditMapper.insert(videoAudit);
	}


	/**
	 * 我的投稿
	 * @param pageParams
	 * @return
	 */
	@Override
	public PageResult<VideoAuditVO> submit(PageParams pageParams) {
		//查找用户投稿视频
		Long userId = AuthenticationContext.getContext();

		LambdaQueryWrapper<VideoBase> queryWrapper = new LambdaQueryWrapper<>();

		queryWrapper
				.select(VideoBase::getId,VideoBase::getVideoType,VideoBase::getTitle)
				.inSql(VideoBase::getParentid, "SELECT DISTINCT parentid FROM video_base")
				.or(wrapper -> wrapper.eq(VideoBase::getUserId, userId).isNull(VideoBase::getParentid));

		Page<VideoBase> ipage = new Page<>(pageParams.getPage(),pageParams.getPageSize());
		List<VideoBase> videoBaseList = videoBaseMapper.selectPage(ipage, queryWrapper).getRecords();
		long total = videoBaseMapper.selectPage(ipage, queryWrapper).getTotal();

		List videoAuditVOList = null;
		if(videoBaseList != null && videoBaseList.size() > 0){
			videoAuditVOList = new ArrayList();
			Map<Long, VideoBase> videoMap = new HashMap<>();

			Map<Long, VideoBase> newvideoMap = videoBaseList.stream()
					.filter(s -> "001002".equals(s.getVideoType()))
					.collect(Collectors.toMap(VideoBase::getId, s -> s, (o1, o2) -> o1));
			if(newvideoMap != null)
				videoMap.putAll(newvideoMap);

			newvideoMap = videoBaseList.stream()
					.filter(s -> "001003".equals(s.getVideoType()))
					.collect(Collectors.toMap(VideoBase::getParentid, s -> s, (o1, o2) -> o1));
			if(newvideoMap != null)
				videoMap.putAll(newvideoMap);

			//获取审核情况
			if(videoMap != null && videoMap.size() > 0){
				Set<Long> videoIds = videoMap.keySet();
				LambdaQueryWrapper<VideoAudit> queryWrapper1 = new LambdaQueryWrapper<>();
				queryWrapper1.in(VideoAudit::getVideoId,videoIds);
				List<VideoAudit> videoAuditList = videoAuditMapper.selectList(queryWrapper1);
				videoAuditVOList = videoAuditList.stream().map(s -> {
					VideoAuditVO videoAuditVO = new VideoAuditVO();
					BeanUtils.copyProperties(s, videoAuditVO);
					//补全视频审核信息
					VideoBase videoBase = videoMap.get(s.getVideoId());
					videoAuditVO.setTitle(videoBase.getTitle());
					return videoAuditVO;
				}).collect(Collectors.toList());
			}
		}
		return new PageResult<>(videoAuditVOList,total,pageParams.getPage(),pageParams.getPageSize());
	}


//	public PageResult<VideoToMain> getVideoBasePage(int page, int size, String tags) {
//        PageHelper.startPage(page, size);
//        List<VideoBase> videoBaseList = videoBaseMapper.getAllVideoByTags(tags);
//        videoBaseList.forEach(videoBase -> {
//            log.info("videoBase:{}", videoBase);
//        });
//
//		videoBaseMapper.insert(videoBase);
//	}

	@Override
	public PageResult show() {
		List<VideoBase> videoBases = videoBaseMapper.GroupByParentId();
		PageResult pageResult = new PageResult(videoBases, videoBases.size(), 1, videoBases.size());
		return pageResult;
	}

	@Override
	public VideoBase videovie(int id) {
		VideoBase list = videoBaseMapper.selectById(id);
		return list;
	}

	@Override
	public List<String> getTags() {
		List<String> tags = videoBaseMapper.getTags();
		Set<String> tt = new HashSet<>();
		for (String tag : tags) {
			// 分离每个标签字符串，并添加到 set 中以去重
			String[] individualTags = tag.split(",");
			for (String individualTag : individualTags) {
				// 去除可能的空格
				tt.add(individualTag.trim());
			}
		}
		List<String> tags1 = new ArrayList<>(tt);
		return tags1;
	}
}
