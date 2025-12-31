package org.example.controller;

import org.apache.commons.collections4.MapUtils;
import org.example.model.ExitentrypermitType;
import org.example.model.IdCardSideEnum;
import org.example.model.ResponseEntity;
import org.example.service.OcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ocr")
public class OcrController {

    @Autowired
    private OcrService ocrService;

    @PostMapping("/id_card")
    public ResponseEntity ocrIdCardRecognition(
            @RequestPart(name = "frontFile", required = false) MultipartFile frontFile,
            @RequestPart(name = "backFile", required = false) MultipartFile backFile
    ) {
        if ((frontFile == null || frontFile.isEmpty())
                && (backFile == null || backFile.isEmpty())
        ) {
            return ResponseEntity.failed(400, "未传入文件");
        }
        Object frontResp = ocrService.ocrIdCard(frontFile, IdCardSideEnum.FRONT);
        Object backResp = ocrService.ocrIdCard(backFile, IdCardSideEnum.BACK);
        Map<String, Object> resultMap = new HashMap<>();
        if (frontResp != null) {
            resultMap.put("front", frontResp);
        }
        if (backResp != null) {
            resultMap.put("back", backResp);
        }
        if (MapUtils.isEmpty(resultMap)) {
            return ResponseEntity.failed(400, "数据解析失败");
        }
        return ResponseEntity.success(resultMap);
    }

    @PostMapping("/exitentrypermit")
    public ResponseEntity ocrExitentrypermitRecognition(
            @RequestPart(name = "frontFile", required = false) MultipartFile frontFile,
            @RequestPart(name = "backFile", required = false) MultipartFile backFile,
            @RequestPart("exitentrypermitType") String exitentrypermitType
    ) {
        if ((frontFile == null || frontFile.isEmpty())
                && (backFile == null || backFile.isEmpty())
        ) {
            return ResponseEntity.failed(400, "未传入文件");
        }
        ExitentrypermitType frontType;
        ExitentrypermitType backType;
        switch (exitentrypermitType) {
            case "hk_mc_passport": {
                frontType = ExitentrypermitType.HK_MC_PASSPORT_FRONT;
                backType = ExitentrypermitType.HK_MC_PASSPORT_BACK;
                break;
            }
            case "tw_passport": {
                frontType = ExitentrypermitType.TW_PASSPORT_FRONT;
                backType = ExitentrypermitType.TW_PASSPORT_BACK;
                break;
            }
            case "tw_return_passport": {
                frontType = ExitentrypermitType.TW_RETURN_PASSPORT_FRONT;
                backType = ExitentrypermitType.TW_RETURN_PASSPORT_BACK;
                break;
            }
            case "hk_mc_return_passport": {
                frontType = ExitentrypermitType.HK_MC_RETURN_PASSPORT_FRONT;
                backType = ExitentrypermitType.HK_MC_RETURN_PASSPORT_BACK;
                break;
            }
            default: {
                throw new IllegalArgumentException("不支持的通行证类型: " + exitentrypermitType);
            }
        }
        Object frontResp = ocrService.ocrExitentrypermit(frontFile, frontType);
        Object backResp = ocrService.ocrExitentrypermit(backFile, backType);
        return ResponseEntity.success(Map.of(
                "front", frontResp,
                "back", backResp
        ));
    }

    @PostMapping("/passport")
    public ResponseEntity ocrPassportRecognition(
            @RequestPart("file") MultipartFile file
    ) {
        Object o = ocrService.ocrPassport(file);
        return ResponseEntity.success(o);
    }
}
