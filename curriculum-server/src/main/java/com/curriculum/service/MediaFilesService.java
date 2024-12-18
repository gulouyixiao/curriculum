package com.curriculum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.curriculum.model.dto.MovieDto;
import com.curriculum.model.po.MediaFiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 媒资信息 服务类
 * </p>
 *
 * @author gulouyixiao
 * @since 2024-12-14
 */
public interface MediaFilesService extends IService<MediaFiles> {

    List<String> getTags();

    void addImage(String fileurl, String originName, long fileSize);

    void addMovie(String fileurl, String name, long fileSizeKB, String fileTime);

    void addAnime(String fileurl, String name, long fileSizeKB, MovieDto movieDto, String fileTime);

    boolean selectById(String md5);
}
