package com.bunizz.instapetts.listeners;

import com.bunizz.instapetts.beans.PetBean;

public interface open_sheet_listener {
    void open(PetBean petBean, int is_me);
    void open_wizard_pet();
}
