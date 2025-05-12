package com.reliaquest.api.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    private T data;
    private Status status;
    private String error;

    public T getData() {
        return data;
    }

    public Status getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public enum Status {
        HANDLED("Successfully processed request."),
        ERROR("Failed to process request.");

        @JsonValue
        @Getter
        private final String value;

        Status(String value) {
            this.value = value;
        }
    }
}
