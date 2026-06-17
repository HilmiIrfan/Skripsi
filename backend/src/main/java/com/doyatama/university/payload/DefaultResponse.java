package com.doyatama.university.payload;

public class DefaultResponse<T> {

    private T content;
    private long totalElements;
    private String message;

    public DefaultResponse() {

    }

    public DefaultResponse(T content, long totalElements, String message) {
        this.content = content;
        this.totalElements = totalElements;
        this.message = message;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
