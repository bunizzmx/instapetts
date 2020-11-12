package com.bunizz.instapetts.fragments.retos_eventos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.EventBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.RankingBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.db.Utilities;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.post.adapters.PostsAdapter;
import com.bunizz.instapetts.fragments.retos_eventos.adapters.Adapter;
import com.bunizz.instapetts.fragments.retos_eventos.adapters.AdapterRanking;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.utils.retos_viewpager.CardViewPager;
import com.bunizz.instapetts.utils.sliders_cards.CardSliderLayoutManager;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RetosFragment extends Fragment implements  EventosContract.View{

    private final int[][] dotCoords = new int[5][2];
    private final int[] pics = {R.drawable.intro_1, R.drawable.intro_2, R.drawable.intro_3, R.drawable.intro_2, R.drawable.intro_1};
    private final int[] descriptions = {R.string.account, R.string.account, R.string.account, R.string.account, R.string.account};
    private final String[] countries = {"PARIS", "SEOUL", "LONDON", "BEIJING", "THIRA"};
    private final String[] places = {"The Louvre", "Gwanghwamun", "Tower Bridge", "Temple of Heaven", "Aegeana Sea"};
    private final String[] temperatures = {"21°C", "19°C", "17°C", "23°C", "20°C"};
    private final String[] times = {"Aug 1 - Dec 15    7:00-18:00", "Sep 5 - Nov 10    8:00-16:00", "Mar 8 - May 21    7:00-18:00"};


    private CardSliderLayoutManager layoutManger;

    @BindView(R.id.recycler_view)
    CardViewPager mViewPager;

    @BindView(R.id.card_participar)
    CardView card_participar;


    @BindView(R.id.title_toolbar)
    TextView title_toolbar;

    @BindView(R.id.list_photos_eventos)
    RecyclerView list_photos_eventos;

    @BindView(R.id.list_ranking)
    RecyclerView list_ranking;

    @BindView(R.id.refresh_profile_item)
    RelativeLayout refresh_profile_item;

    @BindView(R.id.open_side_menu)
    RelativeLayout open_side_menu;

    @BindView(R.id.filter_recent)
    Button filter_recent;

    @BindView(R.id.filter_votados)
    Button filter_votados;

    @BindView(R.id.filter_ranking)
    Button filter_ranking;

    @BindView(R.id.refresh_events)
    SwipeRefreshLayout refresh_events;


    PostsAdapter adapter;
    change_instance listener;
    EventosPresenter presenter;

    ArrayList<EventBean> eventos;
    Adapter adapter_eventos ;
    AdapterRanking adapter_ranking;
    private int currentPosition;
    boolean IS_IN_RANKING=false;
    public static RetosFragment newInstance() {
        return new RetosFragment();
    }
int POSITION =0;
    @SuppressLint("MissingPermission")
    @OnClick(R.id.filter_ranking)
    void filter_ranking() {
        IS_IN_RANKING = true;
        presenter.getCurrentRanking();
        list_ranking.setVisibility(View.VISIBLE);
        list_photos_eventos.setVisibility(View.GONE);
        filter_ranking.setBackground(getActivity().getResources().getDrawable(R.drawable.filter_enabled));
        filter_ranking.setTextColor(Color.WHITE);
        filter_votados.setBackground(getActivity().getResources().getDrawable(R.drawable.filter_disabled));
        filter_votados.setTextColor(Color.BLACK);
        filter_recent.setBackground(getActivity().getResources().getDrawable(R.drawable.filter_disabled));
        filter_recent.setTextColor(Color.BLACK);
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.filter_votados)
    void filter_votados() {
        IS_IN_RANKING =false;
        list_ranking.setVisibility(View.GONE);
        list_photos_eventos.setVisibility(View.VISIBLE);
        filter_votados.setBackground(getActivity().getResources().getDrawable(R.drawable.filter_enabled));
        filter_votados.setTextColor(Color.WHITE);
        filter_ranking.setBackground(getActivity().getResources().getDrawable(R.drawable.filter_disabled));
        filter_ranking.setTextColor(Color.BLACK);
        filter_recent.setBackground(getActivity().getResources().getDrawable(R.drawable.filter_disabled));
        filter_recent.setTextColor(Color.BLACK);
        //filter_ranking.selle
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.filter_recent)
    void filter_recent() {
        IS_IN_RANKING = false;
        list_ranking.setVisibility(View.GONE);
        list_photos_eventos.setVisibility(View.VISIBLE);
        filter_recent.setBackground(getActivity().getResources().getDrawable(R.drawable.filter_enabled));
        filter_recent.setTextColor(Color.WHITE);
        filter_votados.setBackground(getActivity().getResources().getDrawable(R.drawable.filter_disabled));
        filter_votados.setTextColor(Color.BLACK);
        filter_ranking.setBackground(getActivity().getResources().getDrawable(R.drawable.filter_disabled));
        filter_ranking.setTextColor(Color.BLACK);
        //filter_ranking.selle
    }
    @SuppressLint("MissingPermission")
    @OnClick(R.id.card_participar)
    void card_participar() {
        Bundle b = new Bundle();
        b.putString("URL_RESOURCE",eventos.get(POSITION).getUrl_resource());
        b.putString("TITLE_EVENT",eventos.get(POSITION).getTitle());
        b.putString("DESCRIPTION_EVENT",eventos.get(POSITION).getDescription());
        listener.open_sheetFragment(b, FragmentElement.INSTANCE_DETAIL_EVENTO);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PostsAdapter(getActivity());
        presenter = new EventosPresenter(this,getActivity());
        adapter_eventos = new Adapter(getContext());
        adapter_ranking = new AdapterRanking(getContext());
        eventos = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_retos_eventos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        refresh_profile_item.setVisibility(View.GONE);
        open_side_menu.setVisibility(View.GONE);
        list_photos_eventos.setLayoutManager(new LinearLayoutManager(getActivity()));
        list_photos_eventos.setAdapter(adapter);
        list_ranking.setAdapter(adapter_ranking);
        list_ranking.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_eventos.setElevation(0.2f);
        mViewPager.setAdapter(adapter_eventos);
        mViewPager.isShowShadowTransformer(true);
        title_toolbar.setTextSize(Utilities.convertDpToPixel(10,getActivity()));
        title_toolbar.setText("Retos y Eventos");
        presenter.getEventos(0,-999,0,0);
        mViewPager.getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                POSITION = position;
                presenter.getEventosPosts(eventos.get(position).getId(),-999,0,eventos.get(position).getId());
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        filter_recent.setBackground(getActivity().getResources().getDrawable(R.drawable.filter_enabled));
        filter_recent.setTextColor(Color.WHITE);
        refresh_events.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(IS_IN_RANKING)
                    presenter.getCurrentRanking();
                else
                   presenter.getEventosPosts(eventos.get(POSITION).getId(),-999,0,eventos.get(POSITION).getId());
            }
        });
    }



    private void onActiveCardChange() {
        final int pos = layoutManger.getActiveCardPosition();
        if (pos == RecyclerView.NO_POSITION || pos == currentPosition) {
            return;
        }
        onActiveCardChange(pos);
    }

    private void onActiveCardChange(int pos) {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (change_instance) context;
    }

    @Override
    public void showEventosPost(ArrayList<PostBean> posts) {
        refresh_events.setRefreshing(false);
        ArrayList<Object> data = new ArrayList<>();
        data.addAll(posts);
        adapter.setData(data);

    }

    @Override
    public void showEventos(ArrayList<EventBean> events) {
        eventos.addAll(events);
         if(events.size()>0)
              presenter.getEventosPosts(eventos.get(0).getId(),-999,0,eventos.get(0).getId());

        for (int i =0;i<eventos.size();i++)
            adapter_eventos.addCardItem(eventos.get(i));

    }

    @Override
    public void showRanking(ArrayList<RankingBean> rankings) {
        refresh_events.setRefreshing(false);
        adapter_ranking.setData(rankings);
    }

    private class OnCardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

        }
    }

}
