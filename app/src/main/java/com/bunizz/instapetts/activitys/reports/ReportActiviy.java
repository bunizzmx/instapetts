package com.bunizz.instapetts.activitys.reports;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.reports.FinalReportFragment;
import com.bunizz.instapetts.fragments.reports.ReportsListFragment;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;

import java.util.Stack;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportActiviy extends AppCompatActivity  implements changue_fragment_parameters_listener {

    private FragmentElement mCurrentFragment;
    private FragmentElement mOldFragment;
    private Stack<FragmentElement> stack_list_reports;
    private Stack<FragmentElement> stack_final_reports;

    int ID_RECURSO=0;
    int TYPO_RECURSO =0;

    @SuppressLint("MissingPermission")
    @OnClick(R.id.back_to_main)
    void back_to_main() {
        onBackPressed();
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_activity);
        ButterKnife.bind(this);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            ID_RECURSO = b.getInt("ID_RECURSO");
            TYPO_RECURSO = b.getInt("TYPO_RECURSO");
        }
        changeStatusBarColor(R.color.white);
        stack_final_reports = new Stack<>();
        stack_list_reports = new Stack<>();
        setupFirstFragment();
    }

    private void setupFirstFragment() {
        Bundle b = new Bundle();
        changeOfInstance(FragmentElement.INSTANCE_REPORTS_LIST,b,false);
    }

    private void changue_list_reports(FragmentElement fragment,Bundle data,boolean onback) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            mCurrentFragment.getFragment().setArguments(data);
            if (stack_list_reports.size() <= 0) {
                stack_list_reports.push(mCurrentFragment);
            }
        }
        //((FollowsFragment) mCurrentFragment.getFragment()).updateInfo(data);
        inflateFragment(onback);
    }

    private void changue_to_final_reports(FragmentElement fragment,Bundle data,boolean onback) {
        data.putInt("ID_RECURSO",ID_RECURSO);
        data.putInt("TYPO_RECURSO",TYPO_RECURSO);
        if (fragment != null) {
            mCurrentFragment = fragment;
            mCurrentFragment.getFragment().setArguments(data);
            if (stack_final_reports.size() <= 0) {
                stack_final_reports.push(mCurrentFragment);
            }
        }
        ((FinalReportFragment) mCurrentFragment.getFragment()).refresh_info();
        inflateFragment(onback);
    }



    private void saveFragment() {
        mOldFragment = mCurrentFragment;
    }

    private synchronized void changeOfInstance(int intanceType,Bundle bundle,boolean onback) {
        saveFragment();
        if (intanceType == FragmentElement.INSTANCE_REPORTS_LIST) {
            if (stack_list_reports.size() == 0) {
                changue_list_reports(new FragmentElement<>("", ReportsListFragment.newInstance(), FragmentElement.INSTANCE_REPORTS_LIST),bundle,onback);
            } else {
                changue_list_reports(stack_list_reports.pop(),bundle,onback);
            }
        } else if (intanceType == FragmentElement.INSTANCE_FINAL_REPORT) {
            if (stack_final_reports.size() == 0) {
                changue_to_final_reports(new FragmentElement<>("", FinalReportFragment.newInstance(), FragmentElement.INSTANCE_FINAL_REPORT),bundle,onback);
            } else {
                changue_to_final_reports(stack_final_reports.pop(),bundle,onback);
            }
        }
    }



    @SuppressLint("RestrictedApi")
    private synchronized void inflateFragment(boolean is_back) {

        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if(mOldFragment!=null) {
                Log.e("LOGINGS_LOGS","1 : " + mOldFragment.getInstanceType() + "/" + mCurrentFragment.getInstanceType());
                if (mCurrentFragment.getFragment().isAdded()) {
                    Log.e("LOGINGS_LOGS","1");
                    if(is_back){
                        Log.e("LOGINGS_LOGS","2");
                        fragmentManager
                                .beginTransaction()
                                .addToBackStack(null)
                                .setCustomAnimations(R.anim.enter_left, R.anim.exit_right)
                                .hide(mOldFragment.getFragment())
                                .show(mCurrentFragment.getFragment()).commit();
                    }else{
                        Log.e("LOGINGS_LOGS","3");
                        fragmentManager
                                .beginTransaction()
                                .addToBackStack(null)
                                .setCustomAnimations(R.anim.enter_right, R.anim.exit_left)
                                .hide(mOldFragment.getFragment())
                                .show(mCurrentFragment.getFragment()).commit();
                    }
                } else {
                    Log.e("LOGINGS_LOGS","4");
                    if(is_back){
                        Log.e("LOGINGS_LOGS","5");
                        fragmentManager
                                .beginTransaction()
                                .addToBackStack(null)
                                .setCustomAnimations(R.anim.enter_left, R.anim.exit_right)
                                .hide(mOldFragment.getFragment())
                                .add(R.id.root_reports, mCurrentFragment.getFragment()).commit();
                    }else{
                        Log.e("LOGINGS_LOGS","6");
                        fragmentManager
                                .beginTransaction()
                                .addToBackStack(null)
                                .setCustomAnimations(R.anim.enter_right, R.anim.exit_left)
                                .hide(mOldFragment.getFragment())
                                .add(R.id.root_reports, mCurrentFragment.getFragment()).commit();

                    }
                }

            }else{
                Log.e("LOGINGS_LOGS","1 : " + mCurrentFragment.getInstanceType());
                fragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.root_reports, mCurrentFragment.getFragment()).commit();
            }
        } catch (IllegalStateException ignored) {}
    }

    @Override
    public void change_fragment_parameter(int type_fragment, Bundle data) {
        Log.e("CAMBIAR_LIST_RWPORT","-->" + type_fragment);
        changeOfInstance(type_fragment,data,false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
         finish();

    }

    public void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), color));
            window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), color));
        }
    }
}
