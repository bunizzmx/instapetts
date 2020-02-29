package com.bunizz.instapetts.fragments.share_post.Share;

import com.bunizz.instapetts.beans.PetBean;

import java.util.ArrayList;

public interface SharePostContract {
    interface Presenter {

        public void getLocation();

    }

    interface View{
        void showLocation(String corrdenadas);
    }
}
