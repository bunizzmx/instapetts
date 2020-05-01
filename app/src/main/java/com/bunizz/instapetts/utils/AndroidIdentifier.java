package com.bunizz.instapetts.utils;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.constantes.PREFERENCES;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.UUID;

/**
 * Clase encargada de generar un identificador único del dispositivo móvil para
 * poder asociarlo con la licencia
 *
 * @author rcarventepc
 */
public class AndroidIdentifier {

    private static final String TAG = "AndroidIdentifier";

    private Context context;
    private BluetoothAdapter bluetoothAdapter = null;

    /**
     * Constructor de la clase
     *
     * @param context -
     */
    public AndroidIdentifier(Context context) {
        this.context = context;
    }


    @SuppressLint({"MissingPermission", "HardwareIds"})
    @Deprecated
    public String getImeiID() {
        String thelephonyId = "";
        TelephonyManager telephonyManager;

        telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);


        try {
            if (telephonyManager != null) {
                thelephonyId = telephonyManager.getSubscriberId();
            }

            if (thelephonyId == null) {
                thelephonyId = telephonyManager.getDeviceId();

            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return thelephonyId;
    }

    /**
     * Método encargado de obtener el device ID a partir del objeto Build del
     * hardware
     * Funciona para API < 9
     *
     * @return -
     */
    @Deprecated
    public String getDevID() {
        String devId = "35"   // se construye como una IMEI
                + Build.BOARD.length() % 10 + Build.DEVICE.length() % 10
                + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10
                + Build.PRODUCT.length() % 10; // 5 valores
        return devId;
    }

    /**
     * Método encargado de obtener el Android id del dispositivo
     *
     */
    private static String androidID;
    @SuppressLint("HardwareIds")
    private String getAndroidID() {
        ContentResolver contentResolver = null;
        if (context != null) {
            try {
                contentResolver = context.getContentResolver();
            }catch (Exception e){
                androidID = App.read(PREFERENCES.ANDROID_ID,"INVALID");
            }
        }
        if (contentResolver != null) {
            androidID = Secure.getString(contentResolver, Secure.ANDROID_ID);
        }
        return androidID;
    }

    /**
     * Método encargado de generar un ID por combinación de varios factores
     * aplicando una funcion hash MD5 al resultado
     *
     */
    public String generateCombinationID() {

		/*String m_szDevIDShort = getDevID();

		String m_szAndroidID = getAndroidID();

		String m_szBTMAC = getBluetoothAddress();

		String m_szLongID = m_szDevIDShort + m_szAndroidID + m_szBTMAC;*/

        /*
         * Corrección de la generación del Android ID
         * Funciona para API>9 con efectividad del 98%
         */
        String m_szDevID = getUniquePsuedoID();

        /* Bug en ZTE Blade L2  **/
        String m_szAndroidID = getAndroidID();

        String m_szLongID = m_szDevID + m_szAndroidID;

        // generamos una instancia de la funcion hash MD5
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (m != null) {
            m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
        }
        // obtenemos los bytes del MD5
        byte[] p_md5Data = new byte[0];
        if (m != null) {
            p_md5Data = m.digest();
        }
        // creamos una cadena hexadecimal
        StringBuilder m_szUniqueID = new StringBuilder();
        for (byte p_md5Datum : p_md5Data) {
            int b = (0xFF & p_md5Datum);
            // si se tiene un solo dígito, realizamos el pading agregando un 0
            if (b <= 0xF)
                m_szUniqueID.append("0");
            // agregamos el número a un string
            m_szUniqueID.append(Integer.toHexString(b));
        }
        // convertimos el hexadecimal a mayusculas
        m_szUniqueID = new StringBuilder(m_szUniqueID.toString().toUpperCase(Locale.getDefault()));
        return m_szUniqueID.toString();
    }

    /**
     * Return pseudo unique ID
     * Regresa un Identificador pseudoaleatorio para API => 9 excepto root
     *
     * @return ID
     */
    private static String getUniquePsuedoID() {
        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their phone or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Thanks http://www.pocketmagic.net/?p=1662!
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) +
                (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) +
                (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) +
                (Build.PRODUCT.length() % 10);

        // Thanks to @Roman SL!
        // http://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their phone, there will be a duplicate entry
        String serial;
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception e) {
            // String needs to be initialized
            serial = "serial"; // some value
        }

        // Thanks @Joe!
        // http://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

}
