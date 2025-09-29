package org.example.model;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class Response {

    private Integer code;

    private String message;

    private Object data;

    public Response() {
    }

    public static Response success(Object data) {
        Response response = new Response();
        response.setCode(HttpStatus.OK.value());
        response.setData(data);

        return response;
    }

    public static Response fail(Integer code, String message) {
        Response response = new Response();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    public ResponseEntity<Object> toResponseEntity() {
        return ResponseEntity.status(code).body(this);
    }
}
