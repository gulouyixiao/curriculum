package com.curriculum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.constant.MessageConstant;
import com.curriculum.context.AuthenticationContext;
import com.curriculum.exception.CurriculumException;
import com.curriculum.mapper.UserMapper;
import com.curriculum.mapper.VideoBaseMapper;
import com.curriculum.mapper.VideoCommentsMapper;
import com.curriculum.model.dto.CommentsPageParams;
import com.curriculum.model.po.User;
import com.curriculum.model.po.VideoBase;
import com.curriculum.model.po.VideoComments;
import com.curriculum.model.vo.PageResult;
import com.curriculum.service.VideoCommentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 视频评论 服务实现类
 */
@Slf4j
@Service
public class VideoCommentsServiceImpl extends ServiceImpl<VideoCommentsMapper, VideoComments> implements VideoCommentsService {
	@Autowired
	private VideoCommentsMapper videoCommentsMapper;
	@Autowired
	private VideoBaseMapper videoBaseMapper;
	@Autowired
	private UserMapper userMapper;
	/**
	 * 视频评论区分页查询
	 * @param commentsPageParams
	 * @return
	 */
	@Override
	public PageResult<VideoComments> commentsPageQuery(CommentsPageParams commentsPageParams) {
		Long parentCommentId = commentsPageParams.getParentCommentId();
		LambdaQueryWrapper<VideoComments> queryWrapper = new LambdaQueryWrapper<>();

		//1.查询某个视频下的评论区
		queryWrapper.eq(VideoComments::getVideoId,commentsPageParams.getId());
		if(parentCommentId != null){
			//2.查询该评论下的评论
			queryWrapper.eq(VideoComments::getParentCommentId,parentCommentId);
		}else {
			//3.查询该视频下的直属评论
			queryWrapper.isNull(VideoComments::getParentCommentId);
		}

		//分页查询
		Long page = commentsPageParams.getPage();
		Long pageSize = commentsPageParams.getPageSize();
		Page<VideoComments> ipage = new Page<>(page, pageSize);
		Page<VideoComments> commentsPage = videoCommentsMapper.selectPage(ipage, queryWrapper);
		List<VideoComments> commentsList = commentsPage.getRecords();

		return new PageResult<>(commentsList,commentsPage.getTotal(),page,pageSize);
	}


	/**
	 * 发表评论
	 * @param videoComments
	 */
	@Override
	public void commentsPublish(VideoComments videoComments) {
		//查找视频是否存在
		VideoBase videoBase = videoBaseMapper.selectById(videoComments.getVideoId());

		//视频不存在、视频未发布、视频审核未通过
		if(videoBase == null){
			CurriculumException.cast(MessageConstant.VIDEO_NOT_FOUND);
		}
		if(!"203002".equals(videoBase.getStatus()) || "202002".equals(videoBase.getAuditStatus())){
			CurriculumException.cast(MessageConstant.VIDEO_STATUS_ERROR);
		}

		//获取当前用户id
		Long userId = AuthenticationContext.getContext();
		User user = userMapper.selectById(userId);
		if(user == null){
			CurriculumException.cast(MessageConstant.ACCOUNT_ERROR);
		}
		//完善评论数据
		videoComments.setUserId(userId);
		videoComments.setUsername(user.getUsername());
		videoComments.setCreateDate(LocalDateTime.now());

		Long parentCommentId = videoComments.getParentCommentId();
		//1.发表直属该视频的评论
		//2.发表评论下的评论
		if(parentCommentId != null){
			//查询父评论
			VideoComments parentComment = this.getById(parentCommentId);
			if(parentComment == null){
				CurriculumException.cast(MessageConstant.NETWORK_ERROR);
			}
			if(parentComment.getParentCommentId() != null){
				//评论只支持二级评论，不能一直评论下包含评论套娃
				CurriculumException.cast(MessageConstant.NETWORK_ERROR);
			}

		}
		this.save(videoComments);
	}

}
