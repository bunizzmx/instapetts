package com.bunizz.instapetts.web;

public interface CONST {
    String BASE_URL ="https://o7n5rft4zd.execute-api.us-east-1.amazonaws.com/";
    String BASE_URL_BUCKET ="https://d1jsxczq22wovt.cloudfront.net/";

    String BASE_URL_BUCKET_FIRESTORE ="https://firebasestorage.googleapis.com/v0/b/";
     String BASE_URL_HLS_VIDEO ="https://d3oaqf2eoweuy3.cloudfront.net/";

    String FOLDER_PROFILE ="PROFILE";
    String FOLDER_PETS = "PETS";
    String FOLDER_POSTS ="POSTS";
    String FOLDER_STORIES ="STORIES";



    String BUCKET_PROFILE ="gs://bucket_profile/";
    String BUCKET_POSTS="gs://instapetts-posts/";
    String BUCKET_FILES_BACKUP="gs://instapetts_backup_files/";
    String BUCKET_THUMBS_VIDEO="gs://bucket_tumbh_video/";



    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME =
            "com.google.android.gms.location.sample.locationaddress";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA_LAT = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA_LAT";
    public static final String LOCATION_DATA_EXTRA_LON = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA_LON";
}
