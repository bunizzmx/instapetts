package com.bunizz.instapetts.listeners;

import android.os.Bundle;

public interface uploads {
    void onImageProfileUpdated();
    void setResultForOtherChanges(String url);
    void UpdateProfile(Bundle bundle);
}
