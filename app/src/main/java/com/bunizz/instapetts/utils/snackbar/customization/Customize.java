package com.bunizz.instapetts.utils.snackbar.customization;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.bunizz.instapetts.R;
import com.bunizz.instapetts.utils.snackbar.definations.ColorInt;
import com.bunizz.instapetts.utils.snackbar.definations.DrawableRes;
import com.bunizz.instapetts.utils.snackbar.definations.Duration;
import com.bunizz.instapetts.utils.snackbar.definations.StringRes;
import com.bunizz.instapetts.utils.snackbar.utils.Constants;
import com.bunizz.instapetts.utils.snackbar.utils.Type;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;

import static com.bunizz.instapetts.utils.snackbar.utils.Constants.corner;
import static com.bunizz.instapetts.utils.snackbar.utils.Constants.margin;
import static com.bunizz.instapetts.utils.snackbar.utils.Constants.maxLines;
import static com.bunizz.instapetts.utils.snackbar.utils.Constants.noVal;


/** Last Updated: 04\12\2019
*  @author: Ankush Yerawar
*/

public class Customize {

    private static Snackbar makeSnack(@NonNull View view, @StringRes int resId, @Duration int length) {
        return Snackbar.make(view, resId, length);
    }

    private static Snackbar makeSnack(@NonNull View view, CharSequence text, @Duration int length) {
        return Snackbar.make(view, text, length);
    }

    public static Snackbar defaultRes(@NonNull View view, @StringRes int resId, @Duration int length,
                                      Type type, boolean withIcon) {

        return modify(makeSnack(view, resId, length), type, withIcon);
    }

    public static Snackbar defaultChar(@NonNull View view, CharSequence text, @Duration int length,
                                       Type type, boolean withIcon) {

        return modify(makeSnack(view, text, length), type, withIcon);
    }

    public static Snackbar customIcon(@NonNull View view, CharSequence text, @Duration int length,
                                      Type type, @DrawableRes int iconId) {

        return modifyIcon(makeSnack(view, text, length),type,iconId);
    }

    public static Snackbar customIcon(@NonNull View view, @StringRes int resId, @Duration int length,
                                      Type type, @DrawableRes int iconId) {

        return modifyIcon(makeSnack(view, resId, length),type,iconId);
    }

    public static Snackbar customChar(@DrawableRes int iconId, @NonNull View view, CharSequence text,
                                      @Duration int length, @ColorInt int backgroundColor,
                                      @ColorInt int textColor) {

        return customModify(makeSnack(view, text, length), iconId, textColor, backgroundColor);
    }

    public static Snackbar customRes(@DrawableRes int iconId, @NonNull View view, @StringRes int resId,
                                     @Duration int length, @ColorInt int backgroundColor,
                                     @ColorInt int textColor) {

        return customModify(makeSnack(view, resId, length), iconId, textColor, backgroundColor);
    }

    private static Snackbar modify(Snackbar snackbar, Type type, boolean withIcon) {
        try {

            final View snackBarView = snackbar.getView();

            setTextStyle(snackBarView, type.getIcon(), withIcon);

            snackBarView.setLayoutParams(setMargins(snackBarView));

            snackBarView.setBackground(snackBarView.getContext().getDrawable(type.getBackground()));

            return snackbar;

        } catch (NullPointerException | ClassCastException exception) {
           // Log.e(TAG,"Exception" + exception.getMessage());
        }
        return snackbar;
    }

    private static Snackbar modifyIcon(Snackbar snackbar, Type type, @DrawableRes int iconId) {
        try {

            final View snackBarView = snackbar.getView();

            setTextStyle(snackBarView, iconId, true);

            snackBarView.setLayoutParams(setMargins(snackBarView));

            snackBarView.setBackground(snackBarView.getContext().getDrawable(type.getBackground()));

            return snackbar;

        } catch (NullPointerException | ClassCastException exception) {
            //Log.e(TAG,"Exception" + exception.getMessage());
        }
        return snackbar;
    }

    private static Snackbar customModify(Snackbar snackbar, @DrawableRes int iconId, @ColorInt int textColor,
                                         @ColorInt int backgroundColor) {
        try {

            final View snackBarView = snackbar.getView();

            setCustomTextStyle(snackBarView, iconId, textColor);

            snackBarView.setLayoutParams(setMargins(snackBarView));

            snackBarView.setBackground(setBackground(backgroundColor));

            return snackbar;

        } catch (NullPointerException | ClassCastException exception) {
          //  Log.e(TAG,"Exception" + exception.getMessage());
        }
        return snackbar;
    }

    private static void setTextStyle(View view, @DrawableRes int resId, boolean withIcon) {
        TextView textView = view.findViewById(R.id.snackbar_text);
        textView.setTextSize(Constants.textSize);
        textView.setMaxLines(maxLines);

        textView.setTextColor(Color.WHITE);
        if (withIcon) {
            textView.setCompoundDrawablesWithIntrinsicBounds(resId, noVal, noVal, noVal);
            textView.setCompoundDrawablePadding(textView.getResources().getDimensionPixelOffset(R.dimen.icon_padding));
        }
    }

    private static void setCustomTextStyle(View view, @DrawableRes int resId, @ColorInt int textColor) {
        TextView textView = view.findViewById(R.id.snackbar_text);
        textView.setTextSize(Constants.textSize);
        textView.setMaxLines(maxLines);
        textView.setTextColor(textColor);
        textView.setCompoundDrawablesWithIntrinsicBounds(resId, noVal, noVal, noVal);
        textView.setCompoundDrawablePadding(textView.getResources().getDimensionPixelOffset(R.dimen.icon_padding));
    }

    private static ViewGroup.MarginLayoutParams setMargins(View view) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)
                view.getLayoutParams();

        marginLayoutParams.setMargins(marginLayoutParams.leftMargin + margin,
                marginLayoutParams.topMargin, marginLayoutParams.rightMargin + margin,
                marginLayoutParams.bottomMargin + margin);

        return marginLayoutParams;
    }

    private static GradientDrawable setBackground(int backgroundColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(corner);
        shape.setColor(backgroundColor);
        return shape;
    }
}
