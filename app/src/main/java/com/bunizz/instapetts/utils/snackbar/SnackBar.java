package com.bunizz.instapetts.utils.snackbar;

import android.view.View;


import com.bunizz.instapetts.utils.snackbar.definations.ColorInt;
import com.bunizz.instapetts.utils.snackbar.definations.DrawableRes;
import com.bunizz.instapetts.utils.snackbar.definations.Duration;
import com.bunizz.instapetts.utils.snackbar.definations.StringRes;
import com.bunizz.instapetts.utils.snackbar.utils.Type;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;

import static com.bunizz.instapetts.utils.snackbar.customization.Customize.customChar;
import static com.bunizz.instapetts.utils.snackbar.customization.Customize.customIcon;
import static com.bunizz.instapetts.utils.snackbar.customization.Customize.customRes;
import static com.bunizz.instapetts.utils.snackbar.customization.Customize.defaultChar;
import static com.bunizz.instapetts.utils.snackbar.customization.Customize.defaultRes;
import static com.bunizz.instapetts.utils.snackbar.utils.Constants.withIcon;
import static com.bunizz.instapetts.utils.snackbar.utils.Constants.withNoIcon;


public class SnackBar {

    public static final int LENGTH_INDEFINITE = -2;
    public static final int LENGTH_SHORT = -1;
    public static final int LENGTH_LONG = 0;

    @NonNull
    public static Snackbar success(@NonNull View view, CharSequence text, @Duration int length, boolean withIcon) {
        return defaultChar(view, text, length, Type.SUCCESS, withIcon);
    }

    @NonNull
    public static Snackbar success(@NonNull View view, @StringRes int resId, @Duration int length, boolean withIcon) {
        return defaultRes(view, resId, length, Type.SUCCESS, withIcon);
    }

    @NonNull
    public static Snackbar success(@NonNull View view, CharSequence text, @Duration int length,
                                   @DrawableRes int iconId) {
        return customIcon(view, text, length, Type.SUCCESS, iconId);
    }

    @NonNull
    public static Snackbar success(@NonNull View view, @StringRes int resId, @Duration int length,
                                   @DrawableRes int iconId) {
        return customIcon(view, resId, length, Type.SUCCESS, iconId);
    }

    @NonNull
    public static Snackbar success(@NonNull View view, CharSequence text, @Duration int length) {
        return defaultChar(view, text, length, Type.SUCCESS, withIcon);
    }

    @NonNull
    public static Snackbar success(@NonNull View view, @StringRes int resId, @Duration int length) {
        return defaultRes(view, resId, length, Type.SUCCESS, withIcon);
    }

    @NonNull
    public static Snackbar error(@NonNull View view, CharSequence text, @Duration int length, boolean withIcon) {
        return defaultChar(view, text, length, Type.ERROR, withIcon);
    }

    @NonNull
    public static Snackbar error(@NonNull View view, @StringRes int resId, @Duration int length, boolean withIcon) {
        return defaultRes(view, resId, length, Type.ERROR, withIcon);
    }

    @NonNull
    public static Snackbar error(@NonNull View view, CharSequence text, @Duration int length,
                                 @DrawableRes int iconId) {
        return customIcon(view, text, length, Type.ERROR, iconId);
    }

    @NonNull
    public static Snackbar error(@NonNull View view, @StringRes int resId, @Duration int length,
                                 @DrawableRes int iconId) {
        return customIcon(view, resId, length, Type.ERROR, iconId);
    }

    @NonNull
    public static Snackbar error(@NonNull View view, CharSequence text, @Duration int length) {
        return defaultChar(view, text, length, Type.ERROR, withIcon);
    }

    @NonNull
    public static Snackbar error(@NonNull View view, @StringRes int resId, @Duration int length) {
        return defaultRes(view, resId, length, Type.ERROR, withIcon);
    }

    @NonNull
    public static Snackbar wifi(@NonNull View view, @StringRes int resId, @Duration int length) {
        return defaultRes(view, resId, length, Type.WIFI, withIcon);
    }

    @NonNull
    public static Snackbar warning(@NonNull View view, CharSequence text, @Duration int length, boolean withIcon) {
        return defaultChar(view, text, length, Type.WARNING, withIcon);
    }

    @NonNull
    public static Snackbar warning(@NonNull View view, @StringRes int resId, @Duration int length, boolean withIcon) {
        return defaultRes(view, resId, length, Type.WARNING, withIcon);
    }

    @NonNull
    public static Snackbar warning(@NonNull View view, CharSequence text, @Duration int length,
                                   @DrawableRes int iconId) {
        return customIcon(view, text, length, Type.WARNING, iconId);
    }

    @NonNull
    public static Snackbar warning(@NonNull View view, @StringRes int resId, @Duration int length,
                                   @DrawableRes int iconId) {
        return customIcon(view, resId, length, Type.WARNING, iconId);
    }


    @NonNull
    public static Snackbar warning(@NonNull View view, CharSequence text, @Duration int length) {
        return defaultChar(view, text, length, Type.WARNING, withIcon);
    }

    @NonNull
    public static Snackbar warning(@NonNull View view, @StringRes int resId, @Duration int length) {
        return defaultRes(view, resId, length, Type.WARNING, withIcon);
    }

    @NonNull
    public static Snackbar info(@NonNull View view, CharSequence text, @Duration int length) {
        return defaultChar(view, text, length, Type.INFO, withIcon);
    }

    @NonNull
    public static Snackbar info(@NonNull View view, @StringRes int resId, @Duration int length) {
        return defaultRes(view, resId, length, Type.INFO, withIcon);
    }

    @NonNull
    public static Snackbar info(@NonNull View view, CharSequence text, @Duration int length,
                                @DrawableRes int iconId) {
        return customIcon(view, text, length, Type.INFO, iconId);
    }

    @NonNull
    public static Snackbar info(@NonNull View view, @StringRes int resId, @Duration int length,
                                @DrawableRes int iconId) {
        return customIcon(view, resId, length, Type.INFO, iconId);
    }

    @NonNull
    public static Snackbar info(@NonNull View view, CharSequence text, @Duration int length, boolean withIcon) {
        return defaultChar(view, text, length, Type.INFO, withIcon);
    }


    @NonNull
    public static Snackbar info(@NonNull View view, @StringRes int resId, @Duration int length, boolean withIcon) {
        return defaultRes(view, resId, length, Type.INFO, withIcon);
    }

    @NonNull
    public static Snackbar normal(@NonNull View view, CharSequence text, @Duration int length) {
        return defaultChar(view, text, length, Type.DEFAULT, withNoIcon);
    }

    @NonNull
    public static Snackbar normal(@NonNull View view, @StringRes int resId, @Duration int length) {
        return defaultRes(view, resId, length, Type.DEFAULT, withNoIcon);
    }

    @NonNull
    public static Snackbar normal(@NonNull View view, CharSequence text, @Duration int length,
                                  @DrawableRes int iconId) {
        return customChar(iconId, view, text, length, Type.DEFAULT.getBackColor(), Type.DEFAULT.getTextColor());
    }

    @NonNull
    public static Snackbar normal(@NonNull View view, @StringRes int resId, @Duration int length,
                                  @DrawableRes int iconId) {
        return customRes(iconId, view, resId, length, Type.DEFAULT.getBackColor(), Type.DEFAULT.getTextColor());
    }

    @NonNull
    public static Snackbar custom(@NonNull View view, CharSequence text, @Duration int length,
                                  @DrawableRes int iconId, @ColorInt int backgroundColor,
                                  @ColorInt int textColor) {
        return customChar(iconId, view, text, length, backgroundColor, textColor);
    }

    @NonNull
    public static Snackbar custom(@NonNull View view, @StringRes int resId, @Duration int length,
                                  @DrawableRes int iconId, @ColorInt int backgroundColor,
                                  @ColorInt int textColor) {
        return customRes(iconId, view, resId, length, backgroundColor, textColor);
    }

}