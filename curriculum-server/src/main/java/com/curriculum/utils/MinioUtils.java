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
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: 哼唧兽
 * @date: 9999/9/21
 **/
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
            return null;
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
    public String upload(String bucketName,MultipartFile multipartFile) {
        String fileName = multipartFile.getName();
        if (!bucketExists(bucketName)) {
            makeBucket(bucketName);
        }
        try {
            InputStream inputStream = multipartFile.getInputStream();
            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(inputStream, multipartFile.getSize(), -1)
                    .contentType(multipartFile.getContentType())
                    .build();
            //文件名称相同会覆盖
            minioClient.putObject(objectArgs);
            inputStream.close();
            String fileUrl = preview(fileName, bucketName);
            return fileUrl.substring(0, fileUrl.indexOf("?"));
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
}
