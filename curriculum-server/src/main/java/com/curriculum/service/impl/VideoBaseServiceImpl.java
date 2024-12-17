package com.curriculum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.mapper.VideoBaseMapper;
import com.curriculum.model.dto.VideoPageParams;
import com.curriculum.model.po.VideoBase;
import com.curriculum.model.vo.PageResult;
import com.curriculum.model.vo.VideoToMain;
import com.curriculum.service.VideoBaseService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public PageResult pageQuery(VideoPageParams videoPageParams){
		LambdaQueryWrapper<VideoBase> queryWrapper = new LambdaQueryWrapper<>();

		//分离多个关键字
		String tags = videoPageParams.getTags();
		for (String s : tags.split(",")) {
			queryWrapper.like(VideoBase::getTags,s);
		}

		Page<VideoBase> ipage = new Page<>(videoPageParams.getPage(),videoPageParams.getPageSize());

		Page<VideoBase> videoBasePage = videoBaseMapper.selectPage(ipage, queryWrapper);


		return new PageResult(videoBasePage.getRecords(),videoBasePage.getTotal(), videoPageParams.getPage(), videoPageParams.getPageSize());
	}

    @Override
    public PageResult<VideoToMain> getVideoBasePage(int page, int size, String tags) {
        PageHelper.startPage(page, size);
        List<VideoBase> videoBaseList = videoBaseMapper.getAllVideoByTags(tags);
        videoBaseList.forEach(videoBase -> {
            log.info("videoBase:{}", videoBase);
        });

        List<VideoToMain> collect = videoBaseList.stream()
                .map(videoBase -> {
                    VideoToMain videoToMain = new VideoToMain();
                    BeanUtils.copyProperties(videoBase, videoToMain);
                    return videoToMain;
                })
                .collect(Collectors.toList());
        PageResult<VideoToMain> videoToMainPageResult = new PageResult<>(collect, (long) collect.size(), page, size);
        return videoToMainPageResult;
    }
}
