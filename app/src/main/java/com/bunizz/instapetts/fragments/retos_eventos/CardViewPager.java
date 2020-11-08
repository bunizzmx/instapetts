package com.bunizz.instapetts.fragments.retos_eventos;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.bunizz.instapetts.R;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

/**
 * Card ViewPager Created by farshid roohi on 12/13/17.
 */

public class CardViewPager extends LinearLayout {

    private ViewPager viewPager;
    private ShadowTransformer     cardShadowTransformer;
    private BaseCardViewPagerItem viewPagerAdapter;

    private boolean visibilityIndicator;
    private Integer dotSize = 15;

    private Integer paddingLeft   = 20;
    private Integer paddingTop    = 0;
    private Integer paddingRight  = 20;
    private Integer paddingBottom = 0;

    private Integer selectIndicatorColor;
    private Integer unSelectIndicatorColor;

    public CardViewPager(Context context) {
        super(context);
    }

    public CardViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CardViewPager);
        // attrs indicator
        this.dotSize = typedArray.getDimensionPixelSize(R.styleable.CardViewPager_indicatorSize,
                Utilities.dpToPx(getContext(), this.dotSize));
        this.visibilityIndicator = typedArray.getBoolean(R.styleable.CardViewPager_visibleIndicator, true);
        // attrs viewPager
        this.paddingLeft = typedArray.getDimensionPixelSize(R.styleable.CardViewPager_visibleLeftPadding, this.paddingLeft);
        this.paddingTop = typedArray.getDimensionPixelSize(R.styleable.CardViewPager_visibleTopPadding, this.paddingTop);
        this.paddingRight = typedArray.getDimensionPixelSize(R.styleable.CardViewPager_visibleRightPadding, this.paddingRight);
        this.paddingBottom = typedArray.getDimensionPixelSize(R.styleable.CardViewPager_visibleBottomPadding, this.paddingBottom);
        this.selectIndicatorColor = typedArray.getColor(R.styleable.CardViewPager_selectIndicatorColor,
                ContextCompat.getColor(getContext(), R.color.primary));
        this.unSelectIndicatorColor = typedArray.getColor(R.styleable.CardViewPager_unSelectIndicatorColor,
                ContextCompat.getColor(getContext(), R.color.primary));

        typedArray.recycle();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initializeView();
    }

    private void initializeView() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.card_view_pager, this);
        this.viewPager = view.findViewById(R.id.view_pager);
        this.viewPager.setPadding(this.paddingLeft, this.paddingTop, this.paddingRight, this.paddingBottom);

    }

    public void setAdapter(BaseCardViewPagerItem adapter) {
        if (this.viewPager == null) {
            return;
        }
        this.viewPagerAdapter = adapter;
        this.viewPager.setAdapter(this.viewPagerAdapter);
    }

    public void isShowShadowTransformer(boolean flag) {
        if (this.viewPager == null && this.viewPagerAdapter == null) {
            return;
        }
        if (!flag) {
            return;
        }
        this.cardShadowTransformer = new ShadowTransformer(this.viewPager, this.viewPagerAdapter);
        this.cardShadowTransformer.enableScaling(false);
        this.viewPager.setPageTransformer(false, this.cardShadowTransformer);
    }

    public void enableScaling(boolean flag) {
        if (this.cardShadowTransformer == null) {
            return;
        }
        this.cardShadowTransformer.enableScaling(flag);
    }

    public ViewPager getViewPager() {
        return viewPager;
    }
}
