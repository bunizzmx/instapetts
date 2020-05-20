package com.bunizz.instapetts.web.responses;

import com.bunizz.instapetts.beans.ReportListBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class ResponseListReports {

    @SerializedName("list_reports")
    @Expose
    ArrayList<ReportListBean> list_reports;

    @SerializedName("code_response")
    @Expose
    int code_response;

    public ResponseListReports() {
    }

    public ResponseListReports(ArrayList<ReportListBean> list_reports, int code_response) {
        this.list_reports = list_reports;
        this.code_response = code_response;
    }

    public ArrayList<ReportListBean> getList_reports() {
        return list_reports;
    }

    public void setList_reports(ArrayList<ReportListBean> list_reports) {
        this.list_reports = list_reports;
    }

    public int getCode_response() {
        return code_response;
    }

    public void setCode_response(int code_response) {
        this.code_response = code_response;
    }
}
