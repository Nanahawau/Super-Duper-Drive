package com.udacity.jwdnd.course1.cloudstorage.utilities.models;

public class Response {
    private String responseCode;
    private boolean responseSuccess = false;
    private boolean responseFailed = false;
    private String responseMessage;
    private Object data;


    public Response() {
    }

    public Response(String responseCode, boolean responseSuccess, boolean responseFailed, String responseMessage, Object data) {
        this.responseCode = responseCode;
        this.responseSuccess = responseSuccess;
        this.responseFailed = responseFailed;
        this.responseMessage = responseMessage;
        this.data = data;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public boolean isResponseSuccess() {
        return responseSuccess;
    }

    public void setResponseSuccess(boolean responseSuccess) {
        this.responseSuccess = responseSuccess;
    }

    public boolean isResponseFailed() {
        return responseFailed;
    }

    public void setResponseFailed(boolean responseFailed) {
        this.responseFailed = responseFailed;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
