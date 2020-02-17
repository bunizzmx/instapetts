package com.bunizz.instapetts.managers;

/**
 * Created by sgcmovil on 11/08/16.
 */
public class ErrorUtilities {

    public static ErrorCodes getError(int errorCode) {
        if (errorCode != -1) {
            switch (errorCode) {
                case 999:
                    return ErrorCodes.ERROR_E999;

                default:
                    return ErrorCodes.ERROR_E999;
            }
        } else {
            return null;
        }
    }

    public static ErrorCodes getError(String errorCodeString) {
        errorCodeString = errorCodeString.replace("E", "");
        int errorCode;
        try {
            errorCode = Integer.parseInt(errorCodeString);
            return getError(errorCode);
        } catch (NumberFormatException ex) {
            return getError(-1);
        }
    }

}
