package com.bunizz.instapetts.utils.edittext;

import android.text.TextWatcher;

public interface SimpleTextChangedWatcher {
    void onTextChanged(String theNewText, boolean isError);
}
