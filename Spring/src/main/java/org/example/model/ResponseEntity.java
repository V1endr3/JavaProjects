package org.example.model;

import lombok.Data;

@Data
public class ResponseEntity {

    private Integer code;

    private String message;

    private Object data;

    public static ResponseEntity success() {
        ResponseEntity response = new ResponseEntity();
        response.setCode(0);
        response.setMessage("success");
        return response;
    }

    public static ResponseEntity success(Object data) {
        ResponseEntity response = new ResponseEntity();
        response.setCode(0);
        response.setMessage("success");
        response.setData(data);
        return response;
    }

    public static ResponseEntity failed(Integer code, String message) {
        ResponseEntity response = new ResponseEntity();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

}
