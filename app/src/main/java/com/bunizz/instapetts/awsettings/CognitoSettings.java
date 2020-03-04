package com.bunizz.instapetts.awsettings;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;

public class CognitoSettings {

    private String userPoolId ="us-east-1_07WbAFxOz";
    private String clientId="AKIAJL6HIQKIMQEN6OVQ";
    private String clientSecret="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";
    private Regions cognitoRegion = Regions.US_EAST_1;

    private String  identityPoolId ="us-east-1:e164de17-c332-4d52-ae4c-53e964555d8c";
     private Context context;

     public CognitoUserPool getUserPool(){
         return new CognitoUserPool(context,userPoolId,clientId,clientSecret,cognitoRegion);
     }

     public CognitoCachingCredentialsProvider getCredentialsProvider(){
         return  new CognitoCachingCredentialsProvider(context.getApplicationContext(),identityPoolId,cognitoRegion);
     }

    public String getUserPoolId() {
        return userPoolId;
    }

    public void setUserPoolId(String userPoolId) {
        this.userPoolId = userPoolId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public Regions getCognitoRegion() {
        return cognitoRegion;
    }

    public void setCognitoRegion(Regions cognitoRegion) {
        this.cognitoRegion = cognitoRegion;
    }

    public String getIdentityPoolId() {
        return identityPoolId;
    }

    public void setIdentityPoolId(String identityPoolId) {
        this.identityPoolId = identityPoolId;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
