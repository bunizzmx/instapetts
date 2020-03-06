package com.bunizz.instapetts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.utils.dilogs.DialogPermision;
import com.google.firebase.analytics.FirebaseAnalytics;

import net.sqlcipher.database.SQLiteDatabase;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.RequiresApi;

import iknow.android.utils.BaseUtils;



public class App extends Application {
    private static final String MONSERRAT_MEDIUM = "avenir_book.otf";
    private static final String AVENIR_BLACK = "avenir_black.otf";
    public static SoundPool soundPool, refresh_sound, chat, cuack;
    public static int idsend, id_refresh, id_chat, id_cuack;
    public static Typeface roboto;
    public static Typeface popin_subs,monserrat_black,monserrat_medium;
    String PREF = "com.bunizz.instapetts";
    private static SharedPreferences pref;
    private static App application;
    private static FirebaseAnalytics mFirebaseAnalytics;
    private Vibrator vibrator;
    static long milisegundos_dia = 24 * 60 * 60 * 1000;
    public static final String DATE_FORMAT_FOR_GET_DATE = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_FORMAT_SIMPLE = "yyyy-MM-dd";
    DialogPermision dialogPermision;
    DecimalFormat dfs = new DecimalFormat("#,###,##0");
    public static Typeface fuente,avenir_black;


    @Override
    public void onCreate() {
        super.onCreate();
        initTypeface();
        SQLiteDatabase.loadLibs(getApplicationContext());
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        if(pref == null){
            pref = this.getSharedPreferences(PREF, Activity.MODE_PRIVATE);
        }else{

        }
        application = this;
        String idioma = Locale.getDefault().getLanguage();
        Log.e("IDIOMA","-->" + idioma);
        write("IDIOMA",idioma);
        BaseUtils.init(this);
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



    private void initTypeface() {

      /*  monserrat_medium = Typeface.createFromAsset(getAssets(), MONSERRAT_MEDIUM);
        roboto = Typeface.createFromAsset(getAssets(), CANARO_EXTRA_BOLD_PATH);
        popin_subs = Typeface.createFromAsset(getAssets(), POPIN_SUBS);
        monserrat_black = Typeface.createFromAsset(getAssets(),MONSERRAT_BLACK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            refresh_sound = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .build();
        } else {
            refresh_sound = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }*/

      //  id_refresh = refresh_sound.load(this, R.raw.sound_refresh, 1);

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

    public static String formatDateSimple(Date date) {
        if (date != null) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_SIMPLE);
//            formatter.setTimeZone(TimeZone.getDefault());
            return formatter.format(date);
        } else {
            return "default";
        }
    }



    public static String fecha_lenguaje_humano(String fecha){
        Date fechaInicial= StringToDate(fecha, "-", 0);
        Calendar calFechaInicial= Calendar.getInstance();
        Calendar calFechaFinal= Calendar.getInstance();
        calFechaInicial.setTime(fechaInicial);
        calFechaFinal.setTime(new Date());
        return cantidadTotalMinutos(calFechaInicial,calFechaFinal);
    }

    public static String diferenciaHorasDias(long horas){
          if((horas/24)>6){

              return "Hace " + ((horas/24)/4)  + " semanas";
          }else{
              return "Hace " + (horas/24)  + " dias";
          }

    }

    public static long horas_restantes_top(String fecha){
        Log.e("FECHA_TOP","-->" + fecha);
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

    public static long  cantidadTotalHoras_top(Calendar fechaInicial , Calendar fechaFinal){
        long totalMinutos=0;
        totalMinutos=((fechaFinal.getTimeInMillis()-fechaInicial.getTimeInMillis())/1000/60/60);
        return  totalMinutos;
    }

    public static String cantidadTotalMinutos(Calendar fechaInicial , Calendar fechaFinal){
        long totalMinutos=0;
        totalMinutos=((fechaFinal.getTimeInMillis()-fechaInicial.getTimeInMillis())/1000/60);

        if(totalMinutos > 0 && totalMinutos< 60) {
            if(totalMinutos==1){
                return "Hace " + totalMinutos + " Minuto";
            }else{
                return "Hace " + totalMinutos + " Minutos";
            }

        }else if(totalMinutos ==0){
            return "Justo Ahora";
        }else{
            return cantidadTotalHoras(fechaInicial,fechaFinal);
        }
    }

    public static String cantidadTotalHoras(Calendar fechaInicial , Calendar fechaFinal){
        long totalMinutos=0;
        totalMinutos=((fechaFinal.getTimeInMillis()-fechaInicial.getTimeInMillis())/1000/60/60);
        if(totalMinutos ==1){
            return " Hace " + totalMinutos + " Hora";
        }else{
            if(totalMinutos < 24){
                return " Hace " + totalMinutos + " Horas";
            }else{
                return  diferenciaHorasDias(totalMinutos);
            }

        }
    }



    public static Date StringToDate(String fecha, String caracter, int op){
        String formatoHora=" HH:mm:ss";
        String formato="yyyy"+caracter+"MM"+caracter+"dd"+formatoHora;
        if(op==1)
//
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


}
