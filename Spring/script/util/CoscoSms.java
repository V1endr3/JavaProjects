package org.example.util;

import kong.unirest.core.ContentType;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.collections4.KeyValue;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;

public class CoscoSms {
    private static final String SMS_API_KEY = "a3a6d66509faf2cb85020e0e333b155f";
    private static final String SMS_SECRET_KEY = "4be87a463b4bb17f53ef73f1305d4416625f98213d3816252cde7b8d8c96b20f";
    private static final String SMS_URL = "https://opassapi.infocloud.cc/message/send";
    private static final String TEMPLATE_CODE = "987392066367008768";

    public static void sendSms(List<String> phoneList, List<KeyValue<String, Object>> smsParams) {
        Map<String, String> headers = new HashMap<>();
        headers.put("x-api-key", SMS_API_KEY);
        headers.put("x-sign-method", HmacAlgorithms.HMAC_SHA_224.getName());
        headers.put("x-nonce", generateRandomString(10));
        headers.put("x-timestamp", Long.toString(System.currentTimeMillis()));
        Map<String, String> body = new HashMap<>();
        body.put("templateCode", TEMPLATE_CODE);
        body.put("phones", String.join(",", phoneList));
        List<String> extractParams = smsParams.stream().map(param -> "\"" + param.getValue() + "\"").toList();
        String params = "[" + String.join(",", extractParams) + "]";
        body.put("templateParam", params);
        String signature = computeSignature(headers, body);
        headers.put("x-sign", signature);
        HttpResponse<String> response = Unirest.post(SMS_URL)
                .contentType(ContentType.APPLICATION_JSON)
                .headers(headers)
                .body(body)
                .asString();
        System.out.println(response.getBody());
    }

    private static String generateRandomString(int length) {
        return RandomStringUtils.secureStrong().next(length, true, true);
    }

    private static String computeSignature(Map<String, String> headers, Map<String, String> body) {
        SortedMap<String, Object> sortedMap = new TreeMap<>();
        sortedMap.putAll(headers);
        sortedMap.putAll(body);
        List<String> params = new ArrayList<>();
        for (Map.Entry<String, Object> entry : sortedMap.entrySet()) {
            params.add(entry.getKey() + "=" + entry.getValue());
        }
        String paramString = String.join("&", params);
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_224, SMS_SECRET_KEY).hmacHex(paramString);
    }

    public static void main(String[] args) {
        sendSms(List.of("15001741103"), List.of(new DefaultKeyValue<>("code", "123123")));
    }
}