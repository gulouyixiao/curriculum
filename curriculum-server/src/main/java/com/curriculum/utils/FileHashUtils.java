package com.curriculum.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Component
@Slf4j
public class FileHashUtils {

    public static String calculateFileHash(MultipartFile file) {
        try {
            String algorithm = "MD5";
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] fileBytes = file.getBytes();
            byte[] hashBytes = digest.digest(fileBytes);
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException | java.io.IOException e) {
            log.error("Error calculating file hash: " + e.getMessage());
            return null;
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

