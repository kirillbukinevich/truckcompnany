package com.truckcompany.service.util;

/**
 * Created by Vladimir on 04.11.2016.
 */
public class UploadException extends Exception {

    private UploadImageErrors error;

    public UploadException(){};
    public UploadException(String message){
        super(message);
    }

    public UploadException(UploadImageErrors error){
        super(error.name());
        this.error = error;
    }
    public UploadException(UploadImageErrors error, Throwable exception){
        super(error.name(),exception);
        this.error = error;
    }

    public UploadImageErrors getError() {
        return error;
    }
}
