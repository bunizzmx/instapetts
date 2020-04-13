package com.bunizz.instapetts.fragments.side_menus_activities.countries;

import com.bunizz.instapetts.beans.CodesCountryBean;
import com.bunizz.instapetts.beans.FollowsBean;
import com.bunizz.instapetts.beans.PostBean;

import java.util.ArrayList;

public interface CodesCountryContract {

    interface Presenter {
        void getCodesCountry();
    }

    interface View{
        void showCodesCountry(ArrayList<CodesCountryBean> countryBeans);
        void noInternet();
        void Error();
    }
}
