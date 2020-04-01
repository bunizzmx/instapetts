package com.bunizz.instapetts.fragments.search.tabs.pets;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.searchRecentListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentPetList extends Fragment implements  PetListContract.View{



    @BindView(R.id.list_pets_search_result)
    RecyclerView list_pets_search_result;

    @BindView(R.id.list_Search_recents_pets)
    RecyclerView list_Search_recents_pets;

    @BindView(R.id.root_no_data)
    RelativeLayout root_no_data;


    changue_fragment_parameters_listener listener;
    SearchPetAdapter adapter;
    PetListPresenter presenter;
    ArrayList<Object> data = new ArrayList<>();
    SearchResentHelper helper;
    ArrayList<SearcRecentBean> searcRecentBeans = new ArrayList<>();

    public void setData(ArrayList<Object> data) {
        Log.e("REFRESH_DATA_SEARCH","--> data:" + data.size());
        this.data = data;
        if(this.data.size()>0) {
            list_Search_recents_pets.setVisibility(View.GONE);
            root_no_data.setVisibility(View.GONE);
            list_pets_search_result.setVisibility(View.VISIBLE);
            adapter.setHIDE_LABEL(true);
            adapter.setData(data);
        }else{
            list_Search_recents_pets.setVisibility(View.GONE);
            root_no_data.setVisibility(View.VISIBLE);
            list_pets_search_result.setVisibility(View.GONE);
        }
    }

    public static FragmentPetList newInstance() {
        return new FragmentPetList();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SearchPetAdapter(getContext());
        presenter = new PetListPresenter(this,getContext());
        helper = new SearchResentHelper(getContext());
        adapter.setListener(new changue_fragment_parameters_listener() {
            @Override
            public void change_fragment_parameter(int type_fragment, Bundle data) {
                if(listener!=null) {
                    listener.change_fragment_parameter(type_fragment, data);
                }
            }
        });
        adapter.setListener_recent(new searchRecentListener() {
            @Override
            public void onSearch(SearchPetBean searchPetBean, int type_search) {
                SearcRecentBean searcRecentBean = new SearcRecentBean();
                searcRecentBean.setId_usuario(searchPetBean.getId_user());
                if(type_search == 1) {
                    searcRecentBean.setType_mascota(0);
                    searcRecentBean.setName(searchPetBean.getName_user());
                }
                else {
                    searcRecentBean.setName(searchPetBean.getName_pet());
                    searcRecentBean.setType_mascota(searchPetBean.getType_raza());
                }
                searcRecentBean.setType_save(type_search);
                searcRecentBean.setUrl_photo(searchPetBean.getUrl_photo());
                searcRecentBean.setUuid_usuario(searchPetBean.getUuid());
                presenter.saveSearch(searcRecentBean);
            }

            @Override
            public void onSearchUser(SearchUserBean searchUserBean, int type_search) {

            }

            @Override
            public void deleteRecent(int id) {

            }
        });
        searcRecentBeans.clear();
        searcRecentBeans.addAll(helper.getMySearchRecent(2));
            for (int i = 0; i < searcRecentBeans.size(); i++) {
                data.add(new SearchPetBean(
                        searcRecentBeans.get(i).getId_mascota(),
                        searcRecentBeans.get(i).getId_usuario(),
                        searcRecentBeans.get(i).getUuid_usuario(),
                        searcRecentBeans.get(i).getUrl_photo(),
                        searcRecentBeans.get(i).getName(),
                        searcRecentBeans.get(i).getName_pet(),
                        searcRecentBeans.get(i).getName_raza(),
                        searcRecentBeans.get(i).getType_mascota()
                ));
            }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fargment_pet_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        list_pets_search_result.setLayoutManager(new LinearLayoutManager(getContext()));
        list_Search_recents_pets.setLayoutManager(new LinearLayoutManager(getContext()));
        list_pets_search_result.setAdapter(adapter);
        list_Search_recents_pets.setAdapter(adapter);
        if(data.size()>0){
            list_Search_recents_pets.setVisibility(View.VISIBLE);
            root_no_data.setVisibility(View.GONE);
            list_pets_search_result.setVisibility(View.GONE);
        }else{
            list_Search_recents_pets.setVisibility(View.GONE);
            root_no_data.setVisibility(View.VISIBLE);
            list_pets_search_result.setVisibility(View.GONE);
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
}