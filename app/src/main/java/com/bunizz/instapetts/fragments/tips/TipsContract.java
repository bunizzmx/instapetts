package com.bunizz.instapetts.fragments.tips;

import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.TipsBean;

import java.util.ArrayList;

public interface TipsContract {
    interface Presenter {
        void getTips();
        void getMoreTips(int paginador);
    }

    interface View{
        void showTips(ArrayList<TipsBean> tips_list,ArrayList<PostBean> helps);
        void showMoreTips(ArrayList<TipsBean> tips_list);
        void noInternet();
        void peticionError();
    }
}
