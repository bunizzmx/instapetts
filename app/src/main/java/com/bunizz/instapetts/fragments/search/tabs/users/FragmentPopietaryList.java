package com.bunizz.instapetts.fragments.search.tabs.users;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.SearcRecentBean;
import com.bunizz.instapetts.beans.SearchPetBean;
import com.bunizz.instapetts.beans.SearchUserBean;
import com.bunizz.instapetts.db.helpers.SearchResentHelper;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.searchRecentListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentPopietaryList  extends Fragment implements  UserListContract.View {



    @BindView(R.id.list_propietary_search_result)
    RecyclerView list_propietary_search_result;

    @BindView(R.id.list_propietary_search_recent)
    RecyclerView list_propietary_search_recent;


    @BindView(R.id.root_no_data)
    RelativeLayout root_no_data;

    @BindView(R.id.title_no_data)
    TextView title_no_data;

    @BindView(R.id.body_no_data)
    TextView body_no_data;


    ArrayList<SearcRecentBean> searcRecentBeans = new ArrayList<>();
    changue_fragment_parameters_listener listener;
    SearchUserAdapter adapter;
    UserListPresenter presenter;
    SearchResentHelper helper;

    ArrayList<Object> data = new ArrayList<>();

    public ArrayList<Object> getData() {
        return data;
    }

    public void setData(ArrayList<Object> data) {
        this.data.clear();
        this.data.addAll(data);
        Log.e("SIZEDATAX","-->" + this.data.size());
        if(root_no_data!=null) {
            Log.e("SIZEDATAX","1-->" + this.data.size());
            if (this.data.size() > 0) {
                Log.e("SIZEDATAX","2-->" + this.data.size());
                adapter.is_recent(false);
                root_no_data.setVisibility(View.GONE);
                list_propietary_search_recent.setVisibility(View.GONE);
                list_propietary_search_result.setVisibility(View.VISIBLE);
                adapter.setHIDE_LABEL(true);
                adapter.setData(data);
            } else {
                Log.e("SIZEDATAX","3-->" + this.data.size());
                title_no_data.setText(getString(R.string.no_recent_search_title));
                body_no_data.setText(getString(R.string.no_recent_search_body));
                root_no_data.setVisibility(View.VISIBLE);
                list_propietary_search_recent.setVisibility(View.GONE);
                list_propietary_search_result.setVisibility(View.GONE);
            }
        }else{
            Log.e("TODO_ES_NULO","SI");
        }
    }

    public static FragmentPopietaryList newInstance() {
        return new FragmentPopietaryList();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("CRE_FRAGMENT","oncreate");
        presenter = new UserListPresenter(this,getContext());
        adapter = new SearchUserAdapter(getContext());
        helper = new SearchResentHelper(getContext());
        adapter.setData(data);
        adapter.setListener((type_fragment, data) -> {
            if(listener!=null) {
                listener.change_fragment_parameter(type_fragment, data);
            }
        });
        adapter.setListener_recent(new searchRecentListener() {
            @Override
            public void onSearch(SearchPetBean searchPetBean, int type_search) {

            }

            @Override
            public void onSearchUser(SearchUserBean searchUserBean, int type_search) {
                SearcRecentBean searcRecentBean = new SearcRecentBean();
                searcRecentBean.setId_usuario(Integer.parseInt(searchUserBean.getId_user()));
                if(type_search == 1) {
                    searcRecentBean.setType_mascota(0);
                    searcRecentBean.setName(searchUserBean.getName_user());
                }
                else {
                    searcRecentBean.setName_pet("-");
                    searcRecentBean.setType_mascota(0);
                }
                searcRecentBean.setType_save(type_search);
                searcRecentBean.setUrl_photo(searchUserBean.getUrl_photo());
                searcRecentBean.setUuid_usuario(searchUserBean.getUudi());
                searcRecentBean.setName_tag(searchUserBean.getUser_tag());
                presenter.saveSearch(searcRecentBean);
            }

            @Override
            public void deleteRecent(int id) {
                   presenter.deleteRecent(id);
                if(adapter.get_size()==0)
                    hide_list();

            }
        });
        searcRecentBeans.clear();
        searcRecentBeans.addAll(helper.getMySearchRecent(1));
            for (int i = 0; i < searcRecentBeans.size(); i++) {
                data.add(new SearchUserBean(
                        String.valueOf(searcRecentBeans.get(i).getId_usuario()),
                        searcRecentBeans.get(i).getUuid_usuario(),
                        searcRecentBeans.get(i).getUrl_photo(),
                        searcRecentBeans.get(i).getName(),
                        searcRecentBeans.get(i).getName_tag()
                ));
            }



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pet_propietary_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        Log.e("CRE_FRAGMENT","VISTA");
        list_propietary_search_result.setLayoutManager(new LinearLayoutManager(getContext()));
        list_propietary_search_recent.setLayoutManager(new LinearLayoutManager(getContext()));
        list_propietary_search_result.setAdapter(adapter);
        list_propietary_search_recent.setAdapter(adapter);
        if(data.size()>0){
            adapter.is_recent(true);
            root_no_data.setVisibility(View.GONE);
            list_propietary_search_recent.setVisibility(View.VISIBLE);
            list_propietary_search_result.setVisibility(View.GONE);
        }else{
            hide_list();
        }
        adapter.setHIDE_LABEL(false);
        adapter.setData(data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (changue_fragment_parameters_listener) context;
    }

    @Override
    public void searchSaved() {

    }

    @Override
    public void deleteSucess(){
        adapter.notifyDataSetChanged();
    }

    public void showRecent(){
        data.clear();
        adapter.clear();
        searcRecentBeans.clear();
        searcRecentBeans.addAll(helper.getMySearchRecent(1));
        for (int i = 0; i < searcRecentBeans.size(); i++) {
            data.add(new SearchUserBean(
                    String.valueOf(searcRecentBeans.get(i).getId_usuario()),
                    searcRecentBeans.get(i).getUuid_usuario(),
                    searcRecentBeans.get(i).getUrl_photo(),
                    searcRecentBeans.get(i).getName(),
                    searcRecentBeans.get(i).getName_tag()
            ));
        }
        if(data.size()>0){
            Log.e("SIZEDATAX","7-->" + this.data.size());
            adapter.is_recent(true);
            adapter.setData(data);
            root_no_data.setVisibility(View.GONE);
            list_propietary_search_recent.setVisibility(View.VISIBLE);
            list_propietary_search_result.setVisibility(View.GONE);
        }else{
            Log.e("SIZEDATAX","6-->" + this.data.size());
            root_no_data.setVisibility(View.VISIBLE);
            title_no_data.setText(getString(R.string.no_recent_search_title));
            body_no_data.setText("" + getString(R.string.no_recent_search_body));
            list_propietary_search_recent.setVisibility(View.GONE);
            list_propietary_search_result.setVisibility(View.GONE);

        }
    }

    void hide_list(){
        Log.e("SIZEDATAX","5-->" + this.data.size());
        title_no_data.setText(getString(R.string.no_recent_search_title));
        body_no_data.setText(""+getString(R.string.no_recent_search_body));
        root_no_data.setVisibility(View.VISIBLE);
        list_propietary_search_recent.setVisibility(View.GONE);
        list_propietary_search_result.setVisibility(View.GONE);
    }
}