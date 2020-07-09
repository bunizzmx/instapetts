package com.bunizz.instapetts.utils.snackbar.definations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.annotation.RestrictTo;

import static com.bunizz.instapetts.utils.snackbar.SnackBar.LENGTH_INDEFINITE;
import static com.bunizz.instapetts.utils.snackbar.SnackBar.LENGTH_LONG;
import static com.bunizz.instapetts.utils.snackbar.SnackBar.LENGTH_SHORT;


@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@IntDef({LENGTH_INDEFINITE, LENGTH_SHORT, LENGTH_LONG})
@Retention(RetentionPolicy.SOURCE)
public @interface Duration {
}


