package com.curriculum.utils;


import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class MinioUtils {

    @Resource
    private MinioClient minioClient;

    /**
     * 查看存储bucket是否存在
     * @param bucketName
     * @return
     */
    public Boolean bucketExists(String bucketName) {
        Boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error("Minio异常 : {}", e.getMessage());
            return false;
        }
        return found;
    }

    /**
     * 创建存储bucket
     * @param bucketName
     * @return
     */
    public boolean makeBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error("Minio创建存储bucket异常 : {}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 删除存储bucket
     * @param bucketName
     * @return
     */
    public boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error("Minio删除存储bucket异常 : {}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 获取全部bucket
     * @return
     */
    public List<Bucket> getAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error("Minio获取全部bucket异常 : {}", e.getMessage());
        }
        return null;
    }

    /**
     * 文件上传
     * @param bucketName
     * @param multipartFile
     * @return
     */
    public String upload(String bucketName, MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String fileName = FileHashUtils.calculateFileHash(multipartFile) + originalFilename.substring(originalFilename.lastIndexOf("."));

        Date date = new Date();
        int year = date.getYear() + 1900;
        String month = String.format("%02d", date.getMonth() + 1);
        String day = String.format("%02d", date.getDate());

        String filePath = year + "/" + month + "/" + day + "/" + fileName;

        try {

            if (!bucketExists(bucketName)) {
                log.info("Bucket '{}' 不存在，正在创建...", bucketName);
                boolean created = makeBucket(bucketName);
                if (!created) {
                    log.error("创建 Bucket '{}' 失败", bucketName);
                    return null; // 如果创建桶失败，返回 null
                }
                log.info("Bucket '{}' 创建成功", bucketName);
            }

            // 文件不存在，上传文件
            InputStream inputStream = multipartFile.getInputStream();
            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)  // 使用传入的桶名
                    .object(filePath)    // 使用日期结构的路径
                    .stream(inputStream, multipartFile.getSize(), -1)
                    .contentType(multipartFile.getContentType())
                    .build();

            minioClient.putObject(objectArgs);
            inputStream.close();

            // 上传后获取文件的预览 URL

            return bucketName + "/" + filePath;
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error("Minio文件上传异常 : {}", e.getMessage());
        }
        return null;
    }




    /**
     * 预览图片
     * @param fileName
     * @param bucketName
     * @return
     */
    public String preview(String fileName, String bucketName){
        // 查看文件地址
        GetPresignedObjectUrlArgs build = GetPresignedObjectUrlArgs
                .builder()
                .bucket(bucketName)
                .object(fileName)
                .method(Method.GET)
                .build();
        try {
            return minioClient.getPresignedObjectUrl(build);
        } catch (Exception e) {
            log.error("Minio预览图片异常 : {}", e.getMessage());
        }
        return null;
    }

    /**
     * 文件下载
     * @param fileName
     * @param bucketName
     * @param httpServletResponse
     */
    public void download(String fileName, String bucketName, HttpServletResponse httpServletResponse) {
        GetObjectArgs objectArgs = GetObjectArgs
                .builder()
                .bucket(bucketName)
                .object(fileName)
                .build();
        try (GetObjectResponse response = minioClient.getObject(objectArgs)){
            byte[] buf = new byte[1024];
            int len;
            try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()){
                while ((len = response.read(buf)) != -1){
                    os.write(buf, 0, len);
                }
                os.flush();
                byte[] bytes = os.toByteArray();
                httpServletResponse.setCharacterEncoding("utf-8");
                //设置强制下载不打开
                //res.setContentType("application/force-download");
                httpServletResponse.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
                try (ServletOutputStream stream = httpServletResponse.getOutputStream()){
                    stream.write(bytes);
                    stream.flush();
                }
            }
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error("Minio文件下载异常 : {}", e.getMessage());
        }
    }
    public byte[] downloadTest(String fileName, String bucketName, HttpServletResponse httpServletResponse) {
        GetObjectArgs objectArgs = GetObjectArgs
                .builder()
                .bucket(bucketName)
                .object(fileName)
                .build();
        try (GetObjectResponse response = minioClient.getObject(objectArgs)){
            byte[] buf = new byte[1024];
            int len;
            try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()){
                while ((len = response.read(buf)) != -1){
                    os.write(buf, 0, len);
                }
                os.flush();
                return os.toByteArray();
            }
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error("Minio文件下载异常 : {}", e.getMessage());
        }
        return new byte[0];
    }

    /**
     * 查看文件对象
     * @param bucketName
     * @return
     */
    public List<Item> listObjects(String bucketName) {
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
        List<Item> items = new ArrayList<>();
        try {
            for (Result<Item> result : results) {
                items.add(result.get());
            }
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error("Minio查看文件对象异常 : {}", e.getMessage());
            return items;
        }
        return items;
    }

    /**
     * 创建当前天日期的文件夹
     */
    public String createFolder(String bucketName) {
        // 获取当前日期
        LocalDateTime now = LocalDateTime.now();
        String month = String.valueOf(now.getMonth().getValue());
        String day = String.valueOf(now.getDayOfMonth());
        if (!bucketExists(bucketName)) {
            makeBucket(bucketName);
        }
        if (!this.bucketExists(month)) {
            makeBucket(month);
        }
        if (!this.bucketExists(day)) {
            makeBucket(day);
        }

        return month + "/" + day; // 返回文件夹路径
    }

    /**
    *文件已经存在
     */

    public Boolean checkFileIsExist(String bucketName, String objectName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder().bucket(bucketName).object(objectName).build()
            );
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
