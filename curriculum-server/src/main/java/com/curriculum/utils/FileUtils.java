package com.curriculum.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaInfo;
import ws.schild.jave.MultimediaObject;

import java.io.File;
import java.io.IOException;


@Component
@Slf4j
public class FileUtils {


        public static String getVideoTime(MultipartFile multipartFile) {
            String[] length = new String[2];
            try {
                // 创建临时文件并保存 multipartFile
                File tempFile = File.createTempFile("temp", ".tmp");
                multipartFile.transferTo(tempFile);  // 将 MultipartFile 转为 File

                // 使用 JAVE 获取视频时长
                MultimediaObject instance = new MultimediaObject(tempFile);
                MultimediaInfo result = instance.getInfo();
                Long ls = result.getDuration() / 1000;  // 获取时长并转换为秒
                length[0] = String.valueOf(ls);

                // 计算时、分、秒
                Integer hour = (int) (ls / 3600);
                Integer minute = (int) (ls % 3600) / 60;
                Integer second = (int) (ls - hour * 3600 - minute * 60);
                String hr = hour.toString();
                String mi = minute.toString();
                String se = second.toString();

                // 补充0，确保时、分、秒都是两位数
                if (hr.length() < 2) {
                    hr = "0" + hr;
                }
                if (mi.length() < 2) {
                    mi = "0" + mi;
                }
                if (se.length() < 2) {
                    se = "0" + se;
                }

                // 如果小时是00，则只返回分:秒格式
                length[1] = hr + ":" + mi + ":" + se;

                // 删除临时文件
                tempFile.delete();

            } catch (IOException | EncoderException e) {
                e.printStackTrace();
                return "无法获取视频时长";
            }
            return length[1];
        }
    }


