package org.example.model;

import lombok.Data;

@Data
public class Response {

    private Integer code;

    private String message;

    private Object data;

    public Response() {
    }

    public static Response success(Object data) {
        Response response = new Response();
        response.setCode(200);
        response.setData(data);

        return response;
    }
}
