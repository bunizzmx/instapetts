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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import butterknife.OnClick;

public class ReportActiviy extends AppCompatActivity  implements changue_fragment_parameters_listener {

    private FragmentElement mCurrentFragment;
    private FragmentElement mOldFragment;
    private Stack<FragmentElement> stack_list_reports;
    private Stack<FragmentElement> stack_final_reports;

    int ID_RECURSO=0;
    int TYPO_RECURSO =0;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_activity);
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
        data.putInt("ID_RECURSO",ID_RECURSO);
        data.putInt("TYPO_RECURSO",TYPO_RECURSO);
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
