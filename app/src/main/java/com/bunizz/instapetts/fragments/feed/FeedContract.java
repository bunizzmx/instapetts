package com.bunizz.instapetts.fragments.feed;

import com.bunizz.instapetts.beans.PetBean;

import java.util.ArrayList;

public interface FeedContract {
    interface Presenter {

        public void get_feed();

    }

    interface View{
        void show_feed(ArrayList<PetBean> data);
    }
}
