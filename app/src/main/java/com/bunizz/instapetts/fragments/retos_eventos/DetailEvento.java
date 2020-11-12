package com.bunizz.instapetts.fragments.retos_eventos;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.fragments.post.adapters.PostsAdapter;
import com.bunizz.instapetts.fragments.retos_eventos.adapters.Adapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailEvento extends Fragment implements  DetailEventosContract.View{

    @BindView(R.id.preview_image)
    ImageView preview_image;

    @BindView(R.id.title_event)
    TextView title_event;

    @BindView(R.id.description_event)
    TextView description_event;

    @BindView(R.id.html_politica)
    TextView html_politica;


    DetailEventosPresenter presenter;

    public static DetailEvento newInstance() {
        return new DetailEvento();
    }
    String URL_RESOURCE;
    String TITLE;
    String DESCRIPTION;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if(bundle!=null){
            URL_RESOURCE = bundle.getString("URL_RESOURCE");
            TITLE = bundle.getString("TITLE_EVENT");
            DESCRIPTION  = bundle.getString("DESCRIPTION_EVENT");
        }
        presenter = new DetailEventosPresenter(this,getContext());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_evento, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        Glide.with(getContext()).load(URL_RESOURCE).into(preview_image);
        description_event.setText(DESCRIPTION);
        title_event.setText(TITLE);
        presenter.getPoliticasDetailReto(1);
    }

    @Override
    public void showPolitica(String politica) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            html_politica.setText(Html.fromHtml(politica, Html.FROM_HTML_MODE_COMPACT));
        } else {
            html_politica.setText(Html.fromHtml(politica));
        }

    }
}
