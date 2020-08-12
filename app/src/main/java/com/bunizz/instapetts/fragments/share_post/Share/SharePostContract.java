package com.bunizz.instapetts.fragments.share_post.Share;

import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;

import java.util.ArrayList;

public interface SharePostContract {
    interface Presenter {
         void sendPost(PostBean post);
         void sendPostHelpPet(PostBean post);
         void getLocation();
         void savePostVideo(PostBean postBean);

    }

    interface View{
        void postStatus(boolean status);
        void showLocation(String corrdenadas);
    }
}
