package com.bunizz.instapetts.listeners;

import android.os.Bundle;

import com.bunizz.instapetts.beans.PetBean;

public interface change_instance {
    void change(int fragment_element);
    void onback();
    void open_sheet(PetBean petBean, int is_me);
    void open_sheetFragment(Bundle bundle,int instance);
    void open_wizard_pet();
}
