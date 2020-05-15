package com.bunizz.instapetts.fragments.tips.detail;

import com.bunizz.instapetts.beans.TipsBean;

import java.util.ArrayList;

public interface DetailContract {
    interface Presenter {
         void like(int id_tip);
         void view(int id_tip);
         boolean is_liked(int id_tip);
    }

    interface View{

    }
}
