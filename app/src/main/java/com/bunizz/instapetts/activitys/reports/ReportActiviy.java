package com.bunizz.instapetts.activitys.reports;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.comentarios.ComentariosFragment;
import com.bunizz.instapetts.fragments.feed.FeedFragment;
import com.bunizz.instapetts.fragments.follows.FollowsFragment;
import com.bunizz.instapetts.fragments.notifications.NotificationsFragment;
import com.bunizz.instapetts.fragments.post.FragmentListOfPosts;
import com.bunizz.instapetts.fragments.previewProfile.FragmentProfileUserPetPreview;
import com.bunizz.instapetts.fragments.profile.FragmentEditProfileUser;
import com.bunizz.instapetts.fragments.profile.FragmentProfileUserPet;
import com.bunizz.instapetts.fragments.reports.FinalReportFragment;
import com.bunizz.instapetts.fragments.reports.ReportsListFragment;
import com.bunizz.instapetts.fragments.search.FragmentSearchPet;
import com.bunizz.instapetts.fragments.search.posts.FragmentPostPublics;
import com.bunizz.instapetts.fragments.tips.FragmentTips;
import com.bunizz.instapetts.fragments.tips.detail.FragmentTipDetail;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;

import java.util.Stack;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class ReportActiviy extends AppCompatActivity  implements changue_fragment_parameters_listener {

    private FragmentElement mCurrentFragment;
    private FragmentElement mOldFragment;
    private Stack<FragmentElement> stack_list_reports;
    private Stack<FragmentElement> stack_final_reports;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_activity);
        stack_final_reports = new Stack<>();
        stack_list_reports = new Stack<>();
        setupFirstFragment();
    }

    private void setupFirstFragment() {
        Bundle b = new Bundle();
        changeOfInstance(FragmentElement.INSTANCE_REPORTS_LIST,b);
    }

    private void changue_list_reports(FragmentElement fragment,Bundle data) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            mCurrentFragment.getFragment().setArguments(data);
            if (stack_list_reports.size() <= 0) {
                stack_list_reports.push(mCurrentFragment);
            }
        }
        //((FollowsFragment) mCurrentFragment.getFragment()).updateInfo(data);
        inflateFragment();
    }

    private void changue_to_final_reports(FragmentElement fragment,Bundle data) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            mCurrentFragment.getFragment().setArguments(data);
            if (stack_final_reports.size() <= 0) {
                stack_final_reports.push(mCurrentFragment);
            }
        }
        //((FollowsFragment) mCurrentFragment.getFragment()).updateInfo(data);
        inflateFragment();
    }



    private void saveFragment() {
        mOldFragment = mCurrentFragment;
    }

    private synchronized void changeOfInstance(int intanceType,Bundle bundle) {
        saveFragment();
        if (intanceType == FragmentElement.INSTANCE_REPORTS_LIST) {
            if (stack_list_reports.size() == 0) {
                changue_list_reports(new FragmentElement<>("", ReportsListFragment.newInstance(), FragmentElement.INSTANCE_REPORTS_LIST),bundle);
            } else {
                changue_list_reports(stack_list_reports.pop(),bundle);
            }
        } else if (intanceType == FragmentElement.INSTANCE_FINAL_REPORT) {
            if (stack_final_reports.size() == 0) {
                changue_to_final_reports(new FragmentElement<>("", FinalReportFragment.newInstance(), FragmentElement.INSTANCE_FINAL_REPORT),bundle);
            } else {
                changue_to_final_reports(stack_final_reports.pop(),bundle);
            }
        }
    }



    @SuppressLint("RestrictedApi")
    private synchronized void inflateFragment() {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (mOldFragment != null) {
                if (mCurrentFragment.getFragment().isAdded()) {
                    fragmentManager
                            .beginTransaction()
                            .addToBackStack(null)
                            .hide(mOldFragment.getFragment())
                            .show(mCurrentFragment.getFragment()).commit();
                } else {
                    fragmentManager
                            .beginTransaction()
                            .addToBackStack(null)
                            .hide(mOldFragment.getFragment())
                            .add(R.id.root_reports, mCurrentFragment.getFragment()).commit();
                }

            } else {
                fragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.root_reports, mCurrentFragment.getFragment()).commit();
            }
        } catch (IllegalStateException ignored) {
        }
    }

    @Override
    public void change_fragment_parameter(int type_fragment, Bundle data) {
        changeOfInstance(type_fragment,data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_REPORTS_LIST){
            finish();
        }else{
            changeOfInstance(FragmentElement.INSTANCE_REPORTS_LIST,null);
        }
    }
}
