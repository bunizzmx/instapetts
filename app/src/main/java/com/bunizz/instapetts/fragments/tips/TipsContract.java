package com.bunizz.instapetts.fragments.tips;

import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.TipsBean;

import java.util.ArrayList;

public interface TipsContract {
    interface Presenter {

        public void getTips();

    }

    interface View{
        void showTips(ArrayList<TipsBean> tips_list,ArrayList<PostBean> helps);
        void noInternet();
        void peticionError();
    }
}
