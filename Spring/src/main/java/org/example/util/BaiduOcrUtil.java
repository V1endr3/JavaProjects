package org.example.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import kong.unirest.core.ContentType;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import org.example.model.ExitentrypermitType;
import org.example.model.IdCardSideEnum;

import java.util.concurrent.TimeUnit;

public class BaiduOcrUtil {

    private static final Long CACHE_KEY = 0L;

    private static final String CLIENT_ID = "public";

    private static final String CLIENT_SECRET = "secret";

    private static final String BASE_URL = "https://aip.baidubce.com";

    private static final String TOKEN_URI = "/oauth/2.0/token";

    private static final String IDCARD_URI = "/rest/2.0/ocr/v1/idcard";

    private static final String EXITENTRYPERMIT_URI = "/rest/2.0/ocr/v1/hk_macau_taiwan_exitentrypermit";

    private static final String PASSPORT_URI = "/rest/2.0/ocr/v1/passport";

    private static LoadingCache<Long, String> tokenCache = CacheBuilder.newBuilder()
            .maximumSize(1)
            .expireAfterWrite(2592000L, TimeUnit.SECONDS)
            .build(new CacheLoader<>() {
                @Override
                public String load(Long key) {
                    HttpResponse<JsonNode> response = Unirest.post(BASE_URL + TOKEN_URI)
                            .contentType(ContentType.APPLICATION_FORM_URLENCODED)
                            .field("client_id", CLIENT_ID)
                            .field("client_secret", CLIENT_SECRET)
                            .field("grant_type", "client_credentials")
                            .asJson();
                    System.out.println("根据HTTP请求获取token");
                    if (!response.isSuccess()) {
                        throw new RuntimeException(response.getStatus() + ": " + response.getBody());
                    }
                    return response.getBody().getObject().getString("access_token");
                }
            });

    /**
     *
     * @param imageB64   身份证图片
     * @param idCardSide 身份证正反面
     * @return
     * @throws Exception
     */
    public static String ocrIdCardRecognize(String imageB64, IdCardSideEnum idCardSide) throws Exception {
        HttpResponse<JsonNode> response = Unirest.post(BASE_URL + IDCARD_URI)
                .contentType(ContentType.APPLICATION_FORM_URLENCODED)
                .field("access_token", tokenCache.get(CACHE_KEY))
                .field("image", imageB64)
                .field("id_card_side", idCardSide.getValue())
                .asJson();

        return response.getBody().toPrettyString();
    }

    /**
     *
     * @param imageB64            图片Base64
     * @param exitentrypermitType 港澳台通行证正反面
     * @return
     * @throws Exception
     */
    public static String ocrExitentrypermit(String imageB64, ExitentrypermitType exitentrypermitType) throws Exception {
        HttpResponse<JsonNode> response = Unirest.post(BASE_URL + EXITENTRYPERMIT_URI)
                .contentType(ContentType.APPLICATION_FORM_URLENCODED)
                .field("access_token", tokenCache.get(CACHE_KEY))
                .field("image", imageB64)
                .field("exitentrypermit_type", exitentrypermitType.getValue())
                .asJson();

        return response.getBody().toPrettyString();
    }

    /**
     *
     * @param imageB64 图片Base64
     * @return
     * @throws Exception
     */
    public static String ocrPassport(String imageB64) throws Exception {
        HttpResponse<JsonNode> response = Unirest.post(BASE_URL + PASSPORT_URI)
                .contentType(ContentType.APPLICATION_FORM_URLENCODED)
                .field("access_token", tokenCache.get(CACHE_KEY))
                .field("image", imageB64)
                .asJson();

        return response.getBody().toPrettyString();
    }

    public static void main(String[] args) throws Exception {
        String token = tokenCache.get(CACHE_KEY);
        System.out.println(token);
    }
}
