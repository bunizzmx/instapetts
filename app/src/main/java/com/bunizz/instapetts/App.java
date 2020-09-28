package com.bunizz.instapetts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.activitys.login.LoginActivity;
import com.bunizz.instapetts.beans.AspectBean;
import com.bunizz.instapetts.constantes.FIRESTORE;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.Utilities;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.utils.AndroidIdentifier;
import com.bunizz.instapetts.utils.dilogs.DialogPermision;
import com.bunizz.instapetts.web.CONST;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import androidx.annotation.RequiresApi;

import static com.bunizz.instapetts.constantes.PREFERENCES.IS_INTRO_COMPLETED;


public class App extends Application {
    private static final String MONSERRAT_MEDIUM = "avenir_book.otf";
    private static final String AVENIR_BLACK = "avenir_black.otf";
    public static SoundPool soundPool, refresh_sound, chat, cuack;
    public static int idsend, id_refresh, id_chat, id_cuack;
    String PREF = "com.bunizz.instapetts";
    private static SharedPreferences pref;
    private static App application;
    private static FirebaseAnalytics mFirebaseAnalytics;
    private Vibrator vibrator;
    static long milisegundos_dia = 24 * 60 * 60 * 1000;
    public static final String DATE_FORMAT_FOR_GET_DATE = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_SIMPLE = "yyyy-MM-dd";
    DialogPermision dialogPermision;
    DecimalFormat dfs = new DecimalFormat("#,###,##0");
    public static Typeface fuente,avenir_black;
    private static FirebaseFirestore db;

    public static final String DATE_FROMAT_FOR_COMMENTS = "yyyy-MM-dd-HH-mm-ss";
    private AdLoader adLoader;
    ArrayList<UnifiedNativeAd> ads = new ArrayList<>();
    private static final String AD_UNIT_ID = BuildConfig.ADS_NATIVO;

    @Override
    public void onCreate() {
        super.onCreate();
        SQLiteDatabase.loadLibs(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        db = FirebaseFirestore.getInstance();
        if(pref == null)
            pref = this.getSharedPreferences(PREF, Activity.MODE_PRIVATE);
        application = this;
        String idioma = Locale.getDefault().getLanguage();
        Log.e("IDIOMA","-->" + idioma);
        write("IDIOMA",idioma);
        write(PREFERENCES.ANDROID_ID, Utilities.Md5Hash(new AndroidIdentifier(this).generateCombinationID()));

         new Handler().postDelayed(() ->App.getInstance().inicializar_ads(), 2000);
    }

    public void inicializar_ads(){
            generateItemsForAds();
    }

   public  void clear_preferences(){
        pref.edit().clear().apply();
       App.write(IS_INTRO_COMPLETED,true);
    }



    public static App getInstance() {
        return application;
    }


    public void show_dialog_permision(Activity context, String title_permision, String body_permision, int tipo){
        dialogPermision = new DialogPermision(context);
        dialogPermision.setBody_permision(body_permision);
        dialogPermision.setTitle_permision(title_permision);
        dialogPermision.setTipo_permision(tipo);
        dialogPermision.show();
    }

    public static FirebaseFirestore getIntanceFirestore(){
        if(db!=null)
            return db;
        else
            return db = FirebaseFirestore.getInstance();
    }

    public static  void sonar(int tipo){

        switch (tipo){
            case 1:soundPool.play(idsend,1,1,1,0,1); break;
            case 2:refresh_sound.play(id_refresh,1,1,1,0,1); break;
            case 3:cuack.play(id_cuack,1,1,1,0,1); break;
            case 4:chat.play(id_chat,1,1,1,0,1); break;
            default:break;
        }
    }




    public void postEventFirebase(String event, Bundle bundle) {
            bundle.putString("NAME_USER", read("nombre","generico"));
            bundle.putString("ID_USER", read("id_usuario_auth","generico"));
            if (mFirebaseAnalytics == null)
                mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
            //if (!BuildConfig.DEBUG)
                mFirebaseAnalytics.logEvent(event, bundle);
    }




    public void vibrate(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (vibrator.hasVibrator()) {
                    createOneShotVibrationUsingVibrationEffect();
                }
            } else {
                if (vibrator.hasVibrator()) {
                    vibrator.vibrate(500);
                }
            }
        }catch (Exception e){

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createOneShotVibrationUsingVibrationEffect() {
        // 1000 : Vibrate for 1 sec
        // VibrationEffect.DEFAULT_AMPLITUDE - would perform vibration at full strength
        VibrationEffect effect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE);
        vibrator.vibrate(effect);
    }


    public static String read(String key, String defValue) {
        return pref.getString(key, defValue);
    }

    public static void write(String key, String value) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public static boolean read(String key, boolean defValue) {
        return pref.getBoolean(key, defValue);
    }

    public static void write(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    public static Integer read(String key, int defValue) {
        return pref.getInt(key, defValue);
    }



    public static Float read(String key, float defValue) {
        return pref.getFloat(key, defValue);
    }

    public static void write(String key, Float value) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putFloat(key, value).commit();
    }


    public static void write(String key, Integer value) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putInt(key, value).commit();
    }

    public  static void deletAllPreferences(){
        pref.edit().clear().commit();
    }


    public static String formatDateGMT(Date date) {
        if (date != null) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_FOR_GET_DATE);
//            formatter.setTimeZone(TimeZone.getDefault());
            return formatter.format(date);
        } else {
            return "N/A";
        }
    }


    public static String formatDateComments(Date date) {
        if (date != null) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(DATE_FROMAT_FOR_COMMENTS);
//            formatter.setTimeZone(TimeZone.getDefault());
            return formatter.format(date);
        } else {
            return "N/A";
        }
    }



    public static String formatDateSimple(Date date) {
        if (date != null) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_SIMPLE);
//            formatter.setTimeZone(TimeZone.getDefault());
            formatter.setTimeZone(TimeZone.getTimeZone("gmt"));
            return formatter.format(date);
        } else {
            return "default";
        }
    }



    public  String fecha_lenguaje_humano(String fecha){
        try {

            Date fechaInicial = StringToDate(fecha, "-", 0);
            Calendar calFechaInicial = Calendar.getInstance();
            Calendar calFechaFinal = Calendar.getInstance();
            calFechaInicial.setTime(fechaInicial);
            calFechaFinal.setTime(new Date());
            return cantidadTotalMinutos(calFechaInicial,calFechaFinal);
        }catch (Exception e){
            Log.e("Exceptiocccc","-->" + e.getMessage());
            return  getApplicationContext().getResources().getString(R.string.now) ;
        }

    }

    public  String diferenciaHorasDias(long horas){
        int meses =0;
        if((horas/24)>6){
               if(horas > 8064) {
                   int years = (int) (horas / 8064);
                   int residuo = (int) horas % 8064;
                   if(residuo > 672){
                        meses  = residuo / 872;
                        if(years==1) {
                            if(meses > 1)
                                return years +  " " + getApplicationContext().getResources().getString(R.string.years) + " y  " + meses + " " + getApplicationContext().getResources().getString(R.string.months);
                            else {
                                if(meses ==0)
                                   return years + " " + getApplicationContext().getResources().getString(R.string.year);
                                else
                                    return years + " " +  getApplicationContext().getResources().getString(R.string.year)+ " y  " + meses + " " + getApplicationContext().getResources().getString(R.string.month);
                            }
                        }
                        else {
                            if(meses > 1)
                                return years + " " +getApplicationContext().getResources().getString(R.string.years) + " y  " + meses + " " + getApplicationContext().getResources().getString(R.string.months);
                            else {
                                 if(meses==0)
                                     return years + " " +getApplicationContext().getResources().getString(R.string.years);
                                 else
                                     return years + " "  + getApplicationContext().getResources().getString(R.string.years) + " y  " + meses + " " + getApplicationContext().getResources().getString(R.string.months);
                            }
                        }
                   }else{
                       if(years==1)
                         return  years + " " +  getApplicationContext().getResources().getString(R.string.year);
                       else
                           return  years + " " + getApplicationContext().getResources().getString(R.string.years);
                   }
               }
               else{
                   if(((horas/24)/4) > 1)
                      return  "" + ((horas/24)/4)  + " " + getApplicationContext().getResources().getString(R.string.weeks);
                   else
                       return  "" + ((horas/24)/4)  + " " + getApplicationContext().getResources().getString(R.string.week);
               }

          }else{
            if(horas/24 > 1)
               return " " + (horas/24)  + " " + getApplicationContext().getResources().getString(R.string.days);
            else
                return " " + (horas/24)  + " " +  getApplicationContext().getResources().getString(R.string.day);

          }

    }

    public static int horas_restantes_top(String fecha){
        if(fecha.equals("null")){return 999;}else {
            try {
                Date fechaInicial = StringToDate(fecha, "-", 0);
                Calendar calFechaInicial = Calendar.getInstance();
                Calendar calFechaFinal = Calendar.getInstance();
                calFechaInicial.setTime(fechaInicial);
                calFechaFinal.setTime(new Date());
                return cantidadTotalHoras_top(calFechaInicial, calFechaFinal);
            }catch (Exception e){
                Log.e("DATE_ERROR","--" + e.getMessage());
                return  0;
            }

        }
    }

    public static int  cantidadTotalHoras_top(Calendar fechaInicial , Calendar fechaFinal){
        int totalMinutos=0;
        totalMinutos= (int) ((fechaFinal.getTimeInMillis()-fechaInicial.getTimeInMillis())/1000/60/60);
        return  totalMinutos;
    }

    public  String cantidadTotalMinutos(Calendar fechaInicial , Calendar fechaFinal){
        long totalMinutos=0;
        totalMinutos=((fechaFinal.getTimeInMillis()-fechaInicial.getTimeInMillis())/1000/60);

        if(totalMinutos > 0 && totalMinutos< 60) {
            if(totalMinutos==1){
                if(read(PREFERENCES.IDIOMA,"es").equals("es"))
                  return getApplicationContext().getResources().getString(R.string.hace)  + " " + totalMinutos + " " + getApplicationContext().getResources().getString(R.string.minute);
                else
                    return  totalMinutos + " " + getApplicationContext().getResources().getString(R.string.minute) + " ago";

            }else{
                if(read(PREFERENCES.IDIOMA,"es").equals("es"))
                  return getApplicationContext().getResources().getString(R.string.hace) + " " + totalMinutos + " " + getApplicationContext().getResources().getString(R.string.minutes);
                else
                    return  totalMinutos + " " + getApplicationContext().getResources().getString(R.string.minutes) + " ago";
            }

        }else if(totalMinutos ==0){
            return getApplicationContext().getResources().getString(R.string.now);
        }else{
            return cantidadTotalHoras(fechaInicial,fechaFinal);
        }
    }

    public  String cantidadTotalHoras(Calendar fechaInicial , Calendar fechaFinal){
        long totalMinutos=0;
        totalMinutos=((fechaFinal.getTimeInMillis()-fechaInicial.getTimeInMillis())/1000/60/60);
        if(totalMinutos ==1){
            if(read(PREFERENCES.IDIOMA,"es").equals("es"))
              return getApplicationContext().getResources().getString(R.string.hace) + " "+ totalMinutos + " "+ getApplicationContext().getResources().getString(R.string.hour);
            else
                return  totalMinutos + " "+ getApplicationContext().getResources().getString(R.string.hour)  + " ago";
        }else{
            if(totalMinutos < 24){
                if(read(PREFERENCES.IDIOMA,"es").equals("es"))
                  return getApplicationContext().getResources().getString(R.string.hace) + " "+ totalMinutos + " "+ getApplicationContext().getResources().getString(R.string.hours);
                 else
                   return  totalMinutos + " "+ getApplicationContext().getResources().getString(R.string.hours)  +  " ago";
            }else{
                return  diferenciaHorasDias(totalMinutos);
            }

        }
    }



    public static Date StringToDate(String fecha, String caracter, int op){
        String formatoHora=" HH:mm:ss";
        String formato="yyyy"+caracter+"MM"+caracter+"dd"+formatoHora;
        if(op==1)
            formato="yyyy"+caracter+"dd"+caracter+"MM"+formatoHora;
        else if(op==2)
            formato="MM"+caracter+"yyyy"+caracter+"dd"+formatoHora;
        else if(op==3)
            formato="MM"+caracter+"dd"+caracter+"yyyy"+formatoHora;
        else if(op==4)
            formato="dd"+caracter+"yyyy"+caracter+"MM"+formatoHora;
        else if(op==5)
            formato="dd"+caracter+"MM"+caracter+"yyyy"+formatoHora;
        SimpleDateFormat sdf = new SimpleDateFormat(formato, Locale.getDefault());
        Date fechaFormato=null;
        try {

            sdf.setLenient(false);

            fechaFormato=sdf.parse(fecha);
        } catch (ParseException ex) {
           Log.e("FORAMT_ERROR","--->" + ex.getMessage());
        }
        return fechaFormato;
    }

    @SuppressLint("StaticFieldLeak")
    public  void delete_cache(){
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Glide.get(App.this).clearDiskCache();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public String to_number(int number){
        String t="";
        try {
            return t = dfs.format(number);
        }catch (NumberFormatException err){
            return "0";
        }
    }

    public String  make_uri_bucket_profile(){
        return CONST.BASE_URL_BUCKET_FIRESTORE + "bucket_profile/o/" + read(PREFERENCES.UUID,"INVALID") +"%2FPROFILE%2F"+ read(PREFERENCES.UUID,"INVALID")+
        ".jpg?alt=media&token=1c4cec2c-d8ba-48d2-9e44-9ce384ddaffa";
    }
    public String  make_uri_bucket_profile_tumbh(){
        return CONST.BASE_URL_BUCKET_FIRESTORE + "bucket_profile/o/" + read(PREFERENCES.UUID,"INVALID") +"%2FPROFILE%2Fthumb_"+ read(PREFERENCES.UUID,"INVALID")+
                ".jpg?alt=media&token=1c4cec2c-d8ba-48d2-9e44-9ce384ddaffa";
    }

    public String  make_uri_bucket_posts(String URI){
        return CONST.BASE_URL_BUCKET_FIRESTORE + "instapetts-posts/o/" + read(PREFERENCES.UUID,"INVALID") +"%2FPOSTS%2F"+ URI +
                "?alt=media&token=1c4cec2c-d8ba-48d2-9e44-9ce384ddaffa";
    }

    public String  make_uri_bucket_posts_thumbh(String URI){
        return CONST.BASE_URL_BUCKET_FIRESTORE + "instapetts-posts/o/" + read(PREFERENCES.UUID,"INVALID") +"%2FPOSTS%2Fthumb_"+ URI +
                "?alt=media&token=1c4cec2c-d8ba-48d2-9e44-9ce384ddaffa";
    }
    public String  make_uri_video_hls(String URI,String  aspect){
        return CONST.BASE_URL_HLS_VIDEO  + App.read(PREFERENCES.UUID,"INVALID") + "/" + formatDateSimple(new Date()) + "/" +  URI.replace(".mp4","") + "1280x720x6500.m3u8";
    }

    public String  make_uri_bucket_history(String URI){
        return read(PREFERENCES.UUID,"INVALID") +"%2FSTORIES%2F" +URI + "?alt=media&token=1c4cec2c-d8ba-48d2-9e44-9ce384ddaffa";
    }
    public String getBucketUriHistorie(String URI){
        return CONST.BASE_URL_BUCKET_FIRESTORE + "bucket_profile/o/"+ URI ;
    }
    public String  make_uri_bucket_thumbh_video(String URI){
        String splits[] = URI.split("/");
        String file_name = splits[splits.length-1];
        return CONST.BASE_URL_BUCKET_FIRESTORE + "bucket_tumbh_video/o/" + read(PREFERENCES.UUID,"INVALID") +"%2F"+ file_name +
                "?alt=media&token=1c4cec2c-d8ba-48d2-9e44-9ce384ddaffa";
    }

    public String  make_uri_bucket_for_pet(String ID_PET){
        return CONST.BASE_URL_BUCKET_FIRESTORE + "bucket_profile/o/" + read(PREFERENCES.UUID,"INVALID") +"%2FPETS%2F"+ID_PET+
                ".jpg?alt=media&token=1c4cec2c-d8ba-48d2-9e44-9ce384ddaffa";
    }
    public String  make_uri_bucket_for_pet_thumbh(String ID_PET){
        return CONST.BASE_URL_BUCKET_FIRESTORE + "bucket_profile/o/" + read(PREFERENCES.UUID,"INVALID") +"%2FPETS%2Fthumb_"+ID_PET+
                ".jpg?alt=media&token=1c4cec2c-d8ba-48d2-9e44-9ce384ddaffa";
    }


    public AspectBean getAspect(String aspect){
        int width=0;
        int height =0;
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        if(aspect.equals("16_9"))
        {
          width=metrics.widthPixels;
          height = width/2;
        }else if(aspect.equals("4_3")){
            width=metrics.widthPixels;
            height = width - (width/4);
        }
        else if(aspect.equals("4_4")){
            width=metrics.widthPixels;
            height = width +( width/3);
        }
        else if(aspect.equals("4_5")){
            width=metrics.widthPixels;
            height = width;
        }else{
            width=metrics.widthPixels;
            height = width;
        }
      return new AspectBean(width,height);
    }

    public String aspectMobile(){
        String ascpetc="";
        int width=0;
        int height =0;
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        width=metrics.widthPixels;
        height = metrics.heightPixels;

        int num = height / width ;
        Log.e("VECES_HEIGT","-->" + num  + "/" + width + "/" + height);

        return ascpetc;
    }


    public void generateItemsForAds(){
        Log.e("GENERAMOS_NUEVOS","NUEVOS ADS");
        AdLoader.Builder builder = new AdLoader.Builder(this,AD_UNIT_ID );
        adLoader = builder.forUnifiedNativeAd(
                unifiedNativeAd -> {
                    if(ads.size() > 5)
                        ads.clear();
                    ads.add(unifiedNativeAd);
                    if (!adLoader.isLoading()) {}
                }).withAdListener(
                new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        if (!adLoader.isLoading()) {}
                    }
                }).build();
        adLoader.loadAds(new AdRequest.Builder().build(), 2);
    }

    public ArrayList<UnifiedNativeAd> getMoreAds() {
        return ads;
    }

    public void setAds(ArrayList<UnifiedNativeAd> ads) {
        this.ads = ads;
    }


    public  String  saveImage(Bitmap bitmap, String folderName, String filename, Bitmap.CompressFormat compressFormat,int from) {
        File file =null;
        String fileex="";
        String ruta_final ="error";
        if(from == 1){
            fileex = filename + App.read(PREFERENCES.UUID,"INVALID");
        }else{
            fileex = filename + UUID.randomUUID();
        }
        if(isExternalStorageWritable()){
            Log.e("MEMORIA_EXTERNA","si");
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + folderName);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + folderName + File.separator + fileex + ".jpg");

            if(!file.exists()) {
                Log.e("MEMORIA_EXTERNA","el archivo no se creo");
                file = new File(this.getFilesDir() + File.separator + folderName);
                if (!file.exists()) {
                    file.mkdir();
                }
                file = new File(this.getFilesDir() + File.separator + folderName + File.separator + fileex + ".jpg");
            }

        }else{
            Log.e("MEMORIA_EXTERNA","no");
            file = new File(this.getFilesDir() + File.separator + folderName);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(this.getFilesDir() + File.separator + folderName + File.separator + fileex + ".jpg");
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(compressFormat, 90, out);
            out.flush();
            out.close();
            ruta_final = file.getPath();
            try {
                Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri fileContentUri = Uri.fromFile(file);
                mediaScannerIntent.setData(fileContentUri);
                this.sendBroadcast(mediaScannerIntent);
                return ruta_final;
            }catch (Exception e){
                Log.e("ERROR_BROADCAST",":)");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            Log.e("ImageViewZoom", exception.getMessage());

        }
        return  ruta_final;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public void sendNotification(Map<String, Object> data_notification,int id_user){
        db.collection(FIRESTORE.COLLECTION_NOTIFICATIONS).document(""+id_user)
                .collection(FIRESTORE.COLLECTION_NOTIFICATIONS)
                .document()
                .set(data_notification)
                .addOnFailureListener(e -> {
                    Log.e("ESTATUS_NOTIFICACION","FALLA EN NOTIFICACION");
                })
                .addOnCompleteListener(task -> {
                    Log.e("ESTATUS_NOTIFICACION","COMPLETADO");
                })
                .addOnSuccessListener(aVoid -> {
                    Log.e("ESTATUS_NOTIFICACION","VOID");
                });
    }

}
