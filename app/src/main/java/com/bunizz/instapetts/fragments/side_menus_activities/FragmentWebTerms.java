package com.bunizz.instapetts.fragments.side_menus_activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bunizz.instapetts.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentWebTerms extends Fragment {

    @BindView(R.id.web_instapets)
    WebView web_instapets;

    public static FragmentWebTerms newInstance() {
        return new FragmentWebTerms();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web_terms, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        web_instapets.getSettings().setJavaScriptEnabled(true);
        web_instapets.getSettings().setPluginState(WebSettings.PluginState.ON);
        web_instapets.loadUrl("http://instapetts.com/");

    }
}
