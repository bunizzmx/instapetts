package com.bunizz.instapetts.fragments.reports;

import com.bunizz.instapetts.beans.ReportListBean;
import com.bunizz.instapetts.beans.SearchPetBean;
import com.bunizz.instapetts.beans.SearchUserBean;

import java.util.ArrayList;

public interface ReportsContract {

    interface Presenter {
        void getList();
    }

    interface View{
       void showListReports(ArrayList<ReportListBean> reportListBeans);
    }
}
