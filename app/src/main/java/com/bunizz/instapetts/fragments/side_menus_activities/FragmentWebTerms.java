package com.bunizz.instapetts.fragments.side_menus_activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.utils.loadings.SpinKitView;
import com.bunizz.instapetts.utils.loadings.SpriteFactory;
import com.bunizz.instapetts.utils.loadings.Style;
import com.bunizz.instapetts.utils.loadings.sprite.Sprite;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentWebTerms extends Fragment {

    @BindView(R.id.web_instapets)
    WebView web_instapets;

    @BindView(R.id.progres_image)
    SpinKitView progres_image;


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
        Style style = Style.values()[12];
        Sprite drawable = SpriteFactory.create(style);
        progres_image.setIndeterminateDrawable(drawable);
        progres_image.setColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        web_instapets.getSettings().setJavaScriptEnabled(true);
        web_instapets.getSettings().setPluginState(WebSettings.PluginState.ON);
        web_instapets.loadUrl("http://instapetts.com/");
        web_instapets.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view, WebResourceRequest request) {
                web_instapets.loadUrl("http://instapetts.com/");
                return true;
            }

            @Override
            public void onPageStarted(
                    WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progres_image.setVisibility(View.GONE);
            }
        });

    }
}
