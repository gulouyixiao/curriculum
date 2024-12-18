package com.curriculum.service;

import com.alipay.api.domain.Video;
import com.baomidou.mybatisplus.extension.service.IService;
import com.curriculum.model.dto.CommentsPageParams;
import com.curriculum.model.dto.MovieDto;
import com.curriculum.model.dto.VideoPageParams;
import com.curriculum.model.dto.VideoPublishDto;
import com.curriculum.model.po.MediaFiles;
import com.curriculum.model.po.VideoBase;
import com.curriculum.model.po.VideoComments;
import com.curriculum.model.vo.PageResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 视频番剧
 */
public interface VideoBaseService extends IService<VideoBase> {

	/**
	 * 条件分页查询
	 * @param videoPageParams
	 * @return
	 */
	PageResult<VideoBase> PageQuery(VideoPageParams videoPageParams);


	/**
	 * 获取播放量排名前五的列表
	 * @param videoType 视频类型
	 * @param limit 获取记录的限制数量
	 * @return 播放量排名前x的信息列表
	 */
	 List<VideoBase> recommend(String videoType, int limit);


	void addAnime(MovieDto movieDto, String fileTime, MediaFiles mediaFiles);
	/**
	 * 发布视频
	 * @param videoPublishDto
	 */
	void videoPublish(VideoPublishDto videoPublishDto);


	/**
	 * 发布番剧
	 * @param videoPublishDto
	 */
	void animePublish(VideoPublishDto videoPublishDto);



	PageResult show();
}
