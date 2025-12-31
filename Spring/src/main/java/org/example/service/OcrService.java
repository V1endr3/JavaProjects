package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.model.ExitentrypermitType;
import org.example.model.IdCardSideEnum;
import org.example.util.BaiduOcrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.Map;

@Slf4j
@Service
public class OcrService {

    @Autowired
    private ObjectMapper objectMapper;

    public Object ocrIdCard(MultipartFile file, IdCardSideEnum idCardSide) {
        try {
            String imgB64 = fileToBase64(file);
            String jsonRes = BaiduOcrUtil.ocrIdCardRecognize(imgB64, idCardSide);
            return objectMapper.readValue(jsonRes, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
//            throw new RuntimeException("识别身份证失败", e);
            return null;
        }
    }

    public Object ocrExitentrypermit(MultipartFile file, ExitentrypermitType exitentrypermitType) {
        try {
            String imgB64 = fileToBase64(file);
            String jsonRes = BaiduOcrUtil.ocrExitentrypermit(imgB64, exitentrypermitType);
            return objectMapper.readValue(jsonRes, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
//            throw new RuntimeException("识别港澳台通行证失败", e);
            return null;
        }
    }

    public Object ocrPassport(MultipartFile file) {
        try {
            String imgB64 = fileToBase64(file);
            String jsonRes = BaiduOcrUtil.ocrPassport(imgB64);
            return objectMapper.readValue(jsonRes, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            return null;
//            throw new RuntimeException("识别护照失败", e);
        }
    }

    private String fileToBase64(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            byte[] fileBytes = file.getBytes();
            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(fileBytes);
        } catch (Exception e) {
            throw new RuntimeException("文件解析出错", e);
        }
    }
}
