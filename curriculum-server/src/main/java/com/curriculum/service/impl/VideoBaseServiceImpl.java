package com.curriculum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.exception.CurriculumException;
import com.curriculum.mapper.VideoBaseMapper;
import com.curriculum.model.dto.MovieDto;
import com.curriculum.model.dto.VideoPageParams;
import com.curriculum.model.po.MediaFiles;
import com.curriculum.model.po.VideoBase;
import com.curriculum.model.vo.PageResult;
import com.curriculum.model.vo.VideoToMain;
import com.curriculum.model.vo.VideoVo;
import com.curriculum.service.VideoBaseService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 视频
 */
@Slf4j
@Service
public class VideoBaseServiceImpl extends ServiceImpl<VideoBaseMapper, VideoBase> implements VideoBaseService {
	@Autowired
	private VideoBaseMapper videoBaseMapper;

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

		videoBaseMapper.insert(videoBase);
	}

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
}
