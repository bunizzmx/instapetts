package com.bunizz.instapetts.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.BatteryManager;
import android.util.DisplayMetrics;

import com.bunizz.instapetts.utils.UtilsFormat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Clase con diferentes utilizades para el manejo de bytes
 *
 * @author rcarventepc
 */
public class Utilities {

    private static final String TAG = "Utilities";

    public static final String CODE_DISPATCHER_PATTERN = "((?=.*[a-zA-Z])(?=.*\\d).{4,8})";
    public static final String DATE_FORMAT_IRED_WEB_LONG = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_SQLITE = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_SENTINEL_CAR = "HHmmssddMMyy";


    public static byte[] getBytes(Object obj) {


        ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            bos.close();
            byte[] data = bos.toByteArray();
            return data;
        } catch (IOException ex) { } finally {
            try {
                oos.close();
            } catch (IOException ex) {}
        }
        return null;
    }



    /**
     * this method is a opposite to {@code formatDateGMT}, this parse a string (date on format
     * {@code DATE_FORMAT_SQLITE}) to date.
     *
     * @param stringDate formatted date
     * @return {@link Date} parsed
     */
    public static Date parseDateGMT(String stringDate, String dateFormat) {
        Date date = null;
        DateFormat formatter = new SimpleDateFormat(dateFormat);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT:+0500"));
        try {
            date = formatter.parse(stringDate);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date;
    }



    @SuppressLint("TimberExceptionLogging")
    public static Object toObject(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        Object object = null;
        try {
            object = new java.io.ObjectInputStream(
                    new java.io.ByteArrayInputStream(bytes)).readObject();
        } catch (IOException ioe) {

        } catch (ClassNotFoundException cnfe) {

        }
        return object;
    }

    public static String unformatTelephoneNumber(String telephoneNumber) {
        return telephoneNumber.replace("(", "").replace(")", "").replace(" ", "");
    }



    /**
     * Método que convierte de un arreglo de bytes a un entero
     *
     * @param b
     * @return
     */
    public static int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16
                | (b[0] & 0xFF) << 24;
    }

    /**
     * Método que convierte de un entero a un arreglo de bytes
     *
     * @param a
     * @return
     */
    public static byte[] intToByteArray(int a) {
        return new byte[]{(byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF), (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)};
    }





    public static String Md5Hash(String pass) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            byte[] data = pass.getBytes("UTF-8");
            m.update(data, 0, data.length);
            BigInteger i = new BigInteger(1, m.digest());
            String hash = i.toString(16);
            hash = UtilsFormat.padLeft(hash, 32).replace(' ', '0');
            return hash;
        } catch (NoSuchAlgorithmException e1) {}
        catch (UnsupportedEncodingException e) {}
        return pass;
    }

    /**
     * Método encargado de obtener el nivel de la bateria actual
     *
     * @return
     */
    public static float getBatteryLevel(Context context) {
        Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but I added just in case.
        if (level == -1 || scale == -1) {
            return 50.0f;
        }

        return ((float) level / (float) scale) * 100.0f;
    }


    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }


}
