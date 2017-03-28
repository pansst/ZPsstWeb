package com.psst.common.ftp;

public class FTPClientException extends Exception {

    public FTPClientException() {
        super();
    }

    public FTPClientException(String message) {
        super(message);
    }

    public FTPClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public FTPClientException(Throwable cause) {
        super(cause);
    }

}
