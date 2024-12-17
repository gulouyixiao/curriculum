package com.curriculum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.context.AuthenticationContext;
import com.curriculum.mapper.MediaFilesMapper;
import com.curriculum.model.po.MediaFiles;
import com.curriculum.model.po.User;
import com.curriculum.service.MediaFilesService;
import com.curriculum.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 媒资信息 服务实现类
 * </p>
 *
 * @author gulouyixiao
 */
@Slf4j
@Service
public class MediaFilesServiceImpl extends ServiceImpl<MediaFilesMapper, MediaFiles> implements MediaFilesService {

    @Autowired
    private MediaFilesMapper mediaFilesMapper;

    @Autowired
    private UserService userService;
    @Override
    public List<String> getTags() {
        return mediaFilesMapper.getTags();
    }

    @Override
    public void addImage(String fileurl, String fileId, String filename) {
        MediaFiles mediaFiles = new MediaFiles();
        User user = userService.getById(AuthenticationContext.getContext());

        mediaFiles.setUrl(fileurl);
        //暂时文件id和fileid相同
        mediaFiles.setFileId(fileId);
        mediaFiles.setId(fileId);
        mediaFiles.setFileType("图片");
        mediaFiles.setFilename(filename);
        mediaFiles.setUsername(user.getUsername());
        mediaFiles.setBucket("luimage");
    }
}
