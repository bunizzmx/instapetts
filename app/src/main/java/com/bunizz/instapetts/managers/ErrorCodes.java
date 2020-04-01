package com.bunizz.instapetts.managers;


import com.bunizz.instapetts.R;

/**
 * this class represents a ired WEB Error
 */

public enum ErrorCodes {

    //region ired Web Error codes
    ERROR_E999(999, R.string.exit, R.string.exit, null);


    //endregion
    //region Global Variables
    private int errorCode;
    private int titleResource;
    private int messageResource;
    private Exception exception;
    //endregion

    ErrorCodes(int errorCode, int titleResource, int messageResource, Exception exception) {
        this.errorCode = errorCode;
        this.titleResource = titleResource;
        this.messageResource = messageResource;
        this.exception = exception;
    }

    //region Getters
    public int getErrorCode() {
        return errorCode;
    }

    public int getTitleResource() {
        return titleResource;
    }

    public int getMessageResource() {
        return messageResource;
    }

    public Exception getException() {
        return exception;
    }

    //endregion

}
