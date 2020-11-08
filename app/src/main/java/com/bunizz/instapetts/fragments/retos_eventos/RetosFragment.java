package com.bunizz.instapetts.fragments.retos_eventos;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.EventBean;
import com.bunizz.instapetts.beans.FilterBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.db.Utilities;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.post.adapters.PostsAdapter;
import com.bunizz.instapetts.fragments.retos_eventos.adapters.FilterAdapter;
import com.bunizz.instapetts.utils.AnimatedTextViews.HTextView;
import com.bunizz.instapetts.utils.sliders_cards.CardSliderLayoutManager;
import com.bunizz.instapetts.utils.sliders_cards.CardSnapHelper;
import com.bunizz.instapetts.utils.videoCrop.util.Utils;

import java.util.ArrayList;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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


    PostsAdapter adapter;

    EventosPresenter presenter;

    ArrayList<EventBean> eventos;
    Adapter adapter_eventos ;
    private int currentPosition;
    public static RetosFragment newInstance() {
        return new RetosFragment();
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.filter_ranking)
    void filter_ranking() {
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
        filter_recent.setBackground(getActivity().getResources().getDrawable(R.drawable.filter_enabled));
        filter_recent.setTextColor(Color.WHITE);
        filter_votados.setBackground(getActivity().getResources().getDrawable(R.drawable.filter_disabled));
        filter_votados.setTextColor(Color.BLACK);
        filter_ranking.setBackground(getActivity().getResources().getDrawable(R.drawable.filter_disabled));
        filter_ranking.setTextColor(Color.BLACK);
        //filter_ranking.selle
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PostsAdapter(getActivity());
        presenter = new EventosPresenter(this,getActivity());
        adapter_eventos = new Adapter();
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
        adapter_eventos.setElevation(0.2f);
        mViewPager.setAdapter(adapter_eventos);
        mViewPager.isShowShadowTransformer(true);
        title_toolbar.setTextSize(Utilities.convertDpToPixel(10,getActivity()));
        title_toolbar.setText("Retos y Eventos");
        presenter.getEventos(0,-999,0,0);
        presenter.getEventosPosts(0,-999,0,0);
        mViewPager.getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
              String color  = eventos.get(position).getColor();
                card_participar.setCardBackgroundColor(Color.parseColor(color));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        filter_recent.setBackground(getActivity().getResources().getDrawable(R.drawable.filter_enabled));
        filter_recent.setTextColor(Color.WHITE);
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
    public void showEventosPost(ArrayList<PostBean> posts) {
        ArrayList<Object> data = new ArrayList<>();
        data.addAll(posts);
        adapter.setData(data);
    }

    @Override
    public void showEventos(ArrayList<EventBean> events) {
        eventos.addAll(events);
        if(events.size()>0) {
            String color = eventos.get(0).getColor();
            card_participar.setCardBackgroundColor(Color.parseColor(color));
        }
        Log.e("EVENTSXX","-->xxx");
        for (int i =0;i<eventos.size();i++){
            Log.e("EVENTSXX","-->" + i);
            adapter_eventos.addCardItem(eventos.get(i));
        }

    }

    private class OnCardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

        }
    }

}
