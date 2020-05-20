package com.bunizz.instapetts.fragments.wizardPets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.main.Main;
import com.bunizz.instapetts.activitys.wizardPets.WizardPetActivity;
import com.bunizz.instapetts.beans.RazaBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.wizardPets.adapters.SearchRazaAdapter;
import com.bunizz.instapetts.listeners.change_instance_wizard;
import com.bunizz.instapetts.listeners.process_save_pet_listener;
import com.bunizz.instapetts.utils.loadings.SpinKitView;
import com.bunizz.instapetts.utils.loadings.SpriteFactory;
import com.bunizz.instapetts.utils.loadings.Style;
import com.bunizz.instapetts.utils.loadings.sprite.Sprite;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentSearchPet extends Fragment  implements SearchPetContract.View{


    @BindView(R.id.list_words_search)
    RecyclerView list_words_search;

    @BindView(R.id.spin_kit)
    SpinKitView spin_kit;

    @BindView(R.id.serach_raza)
    EditText serach_raza;
    String texto_buscado="";
    process_save_pet_listener listener_pet_config;

    Style style = Style.values()[12];
    Sprite drawable = SpriteFactory.create(style);

    @SuppressLint("MissingPermission")
    @OnClick(R.id.layout_no_se_raza)
    void layout_no_se_raza()
    {
        if(listener!=null){
            Bundle b = new Bundle();
            listener.onchange(FragmentElement.INSTANCE_DATA_PET,b);
        }
        //changeOfInstance(FragmentElement.INSTANCE_PROFILE_PET);
    }
  ArrayList<RazaBean> razasfor_database = new ArrayList<>();
    SearchRazaAdapter adapter;
    SearchPetPresenter presenter;
    change_instance_wizard listener;
    int TYPE_PET =1;

    public static FragmentSearchPet newInstance() {
        return new FragmentSearchPet();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SearchRazaAdapter(getContext());
        presenter = new SearchPetPresenter(this,getContext());
        Bundle bundle=getArguments();
        if(bundle!=null){
            Log.e("PET_PARAMETER","-->" +TYPE_PET);
            TYPE_PET = bundle.getInt(BUNDLES.TYPE_PET);
        }
        else Log.e("PET_PARAMETER","--> nullo");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_raza_pet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        spin_kit.setIndeterminateDrawable(drawable);
        spin_kit.setColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        list_words_search.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setListener(new change_instance_wizard() {
            @Override
            public void onchange(int type_fragment, Bundle data) {
                listener_pet_config.SaveDataPet(data,2);
                listener.onchange(type_fragment,data);
            }

            @Override
            public void onpetFinish(boolean pet_saved) {

            }
        });
        list_words_search.setAdapter(adapter);
        presenter.downloadCatalogo(TYPE_PET);
        serach_raza.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()>0) {
                    texto_buscado = charSequence.toString().toLowerCase();
                    if(texto_buscado.length() > 0)
                    presenter.search_query(charSequence.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


   public  void research_raza(){
        if(presenter !=null && list_words_search!=null) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                Log.e("PET_PARAMETER", "-->" + TYPE_PET);
                TYPE_PET = bundle.getInt(BUNDLES.TYPE_PET);
            }
            adapter.clear();
            spin_kit.setVisibility(View.VISIBLE);
            presenter.downloadCatalogo(TYPE_PET);
        }else{
            Log.e("AUN_NO_SE","CONSTRUYE");
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (change_instance_wizard) context;
        listener_pet_config  =(process_save_pet_listener )context;
    }

    @Override
    public void showCatalogo(ArrayList<RazaBean> data,String query) {
        spin_kit.setVisibility(View.GONE);
      adapter.setData(data,query);
    }

    @Override
    public void saveRazas(ArrayList<RazaBean> data) {
        spin_kit.setVisibility(View.GONE);
        razasfor_database.clear();
        razasfor_database.addAll(data);
        SaveDictionaryTask task =new  SaveDictionaryTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        adapter.setData(data,"");
    }

    @Override
    public void onCatalogoError() {
        presenter.downloadCatalogo(TYPE_PET);
    }


    private class SaveDictionaryTask extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            presenter.clean_table();
            presenter.clean_table();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            synchronized (this) {
                try {
                    if (razasfor_database != null){
                        for (int i = 0; i < razasfor_database.size(); i++) {
                                presenter.saveRaza(razasfor_database.get(i));
                        }
                    }
                }catch (Exception e) {e.printStackTrace();}
                finally {}
                return true;
            }
        }
        @Override
        protected void onPostExecute(Boolean result) { }
    }
}
