package com.bunizz.instapetts.fragments.side_menus_activities.countries;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.CodesCountryBean;
import com.bunizz.instapetts.utils.fastScroll.FastScrollItemIndicator;
import com.bunizz.instapetts.utils.fastScroll.FastScrollerThumbView;
import com.bunizz.instapetts.utils.fastScroll.FastScrollerView;
import com.bunizz.instapetts.utils.loadings.SpinKitView;
import com.bunizz.instapetts.utils.loadings.SpriteFactory;
import com.bunizz.instapetts.utils.loadings.Style;
import com.bunizz.instapetts.utils.loadings.sprite.Sprite;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentCodesCountry extends Fragment implements  CodesCountryContract.View {



    @BindView(R.id.title_no_internet)
    TextView title_no_internet;

    @BindView(R.id.body_no_data)
    TextView body_no_data;

    @BindView(R.id.icon_no_internet)
    ImageView icon_no_internet;


    @BindView(R.id.spin_kit)
    SpinKitView spin_kit;

    @BindView(R.id.root_no_internet)
    RelativeLayout root_no_internet;


    @BindView(R.id.sample_basic_fastscroller)
    FastScrollerView sample_basic_fastscroller;

    @BindView(R.id.sample_basic_fastscroller_thumb)
    FastScrollerThumbView sample_basic_fastscroller_thumb;

    @BindView(R.id.list_codes)
    RecyclerView list_codes;

    codesCountriesAdapter adapter;

    CodesCountryPresenter presenter;


    public static FragmentCodesCountry newInstance() {
        return new FragmentCodesCountry();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_config_code_country, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new codesCountriesAdapter(getContext());
        presenter = new CodesCountryPresenter(this,getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        list_codes.setLayoutManager(new LinearLayoutManager(getContext()));
        list_codes.setAdapter(adapter);

        sample_basic_fastscroller.setUseDefaultScroller(false);
        sample_basic_fastscroller.getItemIndicatorSelectedCallbacks().add(
                new FastScrollerView.ItemIndicatorSelectedCallback() {
                    @Override
                    public void onItemIndicatorSelected(
                            FastScrollItemIndicator indicator,
                            int indicatorCenterY,
                            int itemPosition
                    ) {
                        // Handle scrolling
                    }
                }
        );
        Style style = Style.values()[12];
        Sprite drawable = SpriteFactory.create(style);
        spin_kit.setIndeterminateDrawable(drawable);
        spin_kit.setColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        spin_kit.setVisibility(View.VISIBLE);

        sample_basic_fastscroller_thumb.setupWithFastScroller(sample_basic_fastscroller);
        presenter.getCodesCountry();

    }

    @Override
    public void showCodesCountry(ArrayList<CodesCountryBean> countryBeans) {
        if(countryBeans.size()>0){
            Log.e("CODES_EXISTENTES","-->" + countryBeans.size());
            spin_kit.setVisibility(View.GONE);
            root_no_internet.setVisibility(View.GONE);
            adapter.setData(countryBeans);
                sample_basic_fastscroller.setupWithRecyclerView(list_codes,  (position) -> {
                    CodesCountryBean item = countryBeans.get(position);
                    return new FastScrollItemIndicator.Text(
                            item.getName().substring(0, 1).toUpperCase()
                    );
                });
        }else{
            Log.e("CODES_EXISTENTES","--> SHOW NO DATA");
            body_no_data.setText("Cuando sigas 1 o mas cuentas apareceran aqui");
            title_no_internet.setText(getString(R.string.no_sigues));
            icon_no_internet.setVisibility(View.GONE);
            spin_kit.setVisibility(View.GONE);
            root_no_internet.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void noInternet() {
        Log.e("CODES_EXISTENTES","no internet");
        spin_kit.setVisibility(View.GONE);
        root_no_internet.setVisibility(View.VISIBLE);
    }

    @Override
    public void Error() {
      presenter.getCodesCountry();
    }

    public class codesCountriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        ArrayList<CodesCountryBean> data = new ArrayList<>();


        Context context;

        public codesCountriesAdapter(Context context) {
            this.context = context;
        }

        public ArrayList<CodesCountryBean> getData() {
            return data;
        }

        public void setData(ArrayList<CodesCountryBean> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_code_country,parent,false);
            return new CountriesHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            CountriesHolder h =(CountriesHolder)holder;
            h.name_country.setText(data.get(position).getName());
            h.num_country.setText("+ " +data.get(position).getNum());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class CountriesHolder extends RecyclerView.ViewHolder{
       TextView name_country,num_country;
            public CountriesHolder(@NonNull View itemView) {
                super(itemView);
                name_country = itemView.findViewById(R.id.name_country);
                num_country = itemView.findViewById(R.id.num_country);
            }
        }
    }
}
