package com.bunizz.instapetts.listeners;

import android.os.Bundle;

public interface uploads {
    void onImageProfileUpdated(String from);
    void setResultForOtherChanges(String url);
    void UpdateProfile(Bundle bundle);
    void new_pet();
}
