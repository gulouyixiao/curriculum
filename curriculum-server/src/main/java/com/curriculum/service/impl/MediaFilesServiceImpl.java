package com.curriculum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.context.AuthenticationContext;
import com.curriculum.mapper.MediaFilesMapper;
import com.curriculum.mapper.VideoBaseMapper;
import com.curriculum.model.dto.MovieDto;
import com.curriculum.model.po.MediaFiles;
import com.curriculum.model.po.User;
import com.curriculum.service.MediaFilesService;
import com.curriculum.service.UserService;
import com.curriculum.service.VideoBaseService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Autowired
    private VideoBaseService videoBaseService;
    @Override
    public List<String> getTags() {
        return mediaFilesMapper.getTags();
    }

    @Override
    public void addImage(String fileurl, String originName, long fileSize) {
        MediaFiles mediaFiles = new MediaFiles();
        Long userId = AuthenticationContext.getContext();
        userId = 1L;
        User user = userService.getById(userId);
//        String filePath = fileurl.replace("http://127.0.0.1:9000/", "");

        String fileName = fileurl.substring(fileurl.lastIndexOf("/") + 1);
        String fileId = fileName.substring(0, fileName.lastIndexOf("."));
        mediaFiles.setId(fileId);
        mediaFiles.setUserId(user.getId());
        mediaFiles.setFilename(originName);
        mediaFiles.setUsername(user.getUsername());
        mediaFiles.setFileType("001001");
        mediaFiles.setBucket("luimage");
        mediaFiles.setFilePath(fileurl);
        mediaFiles.setFileId(fileId);
        mediaFiles.setUrl(fileurl);
        mediaFiles.setStatus("1");
        mediaFiles.setFileSize(fileSize);
        mediaFiles.setChangeDate(LocalDateTime.now());
        mediaFiles.setCreateDate(LocalDateTime.now());
        mediaFilesMapper.insert(mediaFiles);
    }

    @Override
    public void addMovie(String fileurl, String originName, long fileSize, String fileTime) {
        MediaFiles mediaFiles = new MediaFiles();
        Long userId = AuthenticationContext.getContext();
        userId = 1L;
        User user = userService.getById(userId);
        String filePath = fileurl.replace("http://127.0.0.1:9000/", "");

        String fileName = fileurl.substring(fileurl.lastIndexOf("/") + 1);
        String fileId = fileName.substring(0, fileName.lastIndexOf("."));
        mediaFiles.setId(fileId);
        mediaFiles.setUserId(user.getId());
        mediaFiles.setFilename(originName);
        mediaFiles.setUsername(user.getUsername());
        mediaFiles.setFileType("001002");
        mediaFiles.setBucket("luvideo");
        mediaFiles.setFilePath(filePath);
        mediaFiles.setFileId(fileId);
        mediaFiles.setUrl(fileurl);
        mediaFiles.setStatus("1");
        mediaFiles.setFileSize(fileSize);
        mediaFiles.setChangeDate(LocalDateTime.now());
        mediaFiles.setCreateDate(LocalDateTime.now());
        mediaFiles.setTimelength(fileTime);
        mediaFilesMapper.insert(mediaFiles);
    }

    @Override
    public void addAnime(String fileurl, String originName, long fileSize, MovieDto movieDto, String fileTime) {
        MediaFiles mediaFiles = new MediaFiles();
        Long userId = AuthenticationContext.getContext();
        userId = 1L;
        User user = userService.getById(userId);
        String filePath = fileurl.replace("http://127.0.0.1:9000/", "");

        String fileName = fileurl.substring(fileurl.lastIndexOf("/") + 1);
        String fileId = fileName.substring(0, fileName.lastIndexOf("."));
        mediaFiles.setId(fileId);
        mediaFiles.setUserId(user.getId());
        mediaFiles.setFilename(originName);
        mediaFiles.setUsername(user.getUsername());
        mediaFiles.setFileType("001003");
        mediaFiles.setBucket("luvideo");
        mediaFiles.setFilePath(filePath);
        mediaFiles.setFileId(fileId);
        mediaFiles.setUrl(fileurl);
        mediaFiles.setStatus("1");
        mediaFiles.setFileSize(fileSize);
        mediaFiles.setChangeDate(LocalDateTime.now());
        mediaFiles.setCreateDate(LocalDateTime.now());
        mediaFiles.setTimelength(fileTime);
        mediaFilesMapper.insert(mediaFiles);

        videoBaseService.addAnime(movieDto, fileTime, mediaFiles);
    }

    @Override
    public boolean selectById(String md5) {
        MediaFiles mediaFiles = mediaFilesMapper.selectById(md5);
        if (mediaFiles == null)
        {
            return false;
        }
        return true;
    }
}
