package com.curriculum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.curriculum.model.dto.CommentsPageParams;
import com.curriculum.model.po.VideoComments;
import com.curriculum.model.vo.PageResult;

/**
 * 视频评论 服务类
 */
public interface VideoCommentsService extends IService<VideoComments> {
	/**
	 * 视频评论区分页查询
	 * @param commentsPageParams
	 * @return
	 */
	PageResult<VideoComments> commentsPageQuery(CommentsPageParams commentsPageParams);

	/**
	 * 发表评论
	 * @param videoComments
	 */
	void commentsPublish(VideoComments videoComments);
}
