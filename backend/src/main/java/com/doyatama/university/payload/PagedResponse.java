package com.doyatama.university.payload;

import java.util.List;

public class PagedResponse<T> {

    private long totalElements;
    private String message;
    private long statusCode;
    private List<T> content;

    public PagedResponse() {

    }

    public PagedResponse(List<T> content,  long totalElements, String message, long statusCode) {
        this.totalElements = totalElements;
        this.message = message;
        this.statusCode = statusCode;
        this.content = content;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
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

    public long getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(long statusCode) {
        this.statusCode = statusCode;
    }
}
