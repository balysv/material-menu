package com.balysv.materialmenu.extras.toolbar;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.balysv.materialmenu.MaterialMenuBase;
import com.balysv.materialmenu.MaterialMenuDrawable;

public class MaterialMenuIconCompat extends MaterialMenuBase {

    public MaterialMenuIconCompat(AppCompatActivity activity, int color, MaterialMenuDrawable.Stroke stroke) {
        super(activity, color, stroke);
    }

    public MaterialMenuIconCompat(AppCompatActivity activity, int color, MaterialMenuDrawable.Stroke stroke, int transformDuration) {
        super(activity, color, stroke, transformDuration);
    }

    @Override
    protected View getActionBarHomeView(Activity activity) {
        return null;
    }

    @Override
    protected View getActionBarUpView(Activity activity) {
        return null;
    }

    @Override
    protected boolean providesActionBar() {
        return false;
    }

    @Override
    protected void setActionBarSettings(Activity activity) {
        ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(getDrawable());
    }
}