package com.bunizz.instapetts.managers;



/**
 * Created by sgcmovil on 11/08/16.
 */

public class ErrorManager {

    private ErrorCodes error;

    /**
     * this method set a code error and initialize the {@link ErrorCodes} error variable
     *
     * @param errorCode code error
     */
    public void setError(int errorCode) {
        this.error = ErrorUtilities.getError(errorCode);
    }

    /**
     * this method set a code error and initialize the {@link ErrorCodes} error variable
     *
     * @param errorCode codeError
     */
    public void setError(String errorCode) {
        this.error = ErrorUtilities.getError(errorCode);
    }

    //endregion

    //region OtherMethods

    public void cleanError() {
        this.error = null;
    }

    public boolean hasErrorRequest() {
        return (error != null);
    }

    public ErrorCodes getError() {
        return error;
    }

}
