package com.balysv.materialmenu.demo.abs;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.balysv.materialmenu.demo.BaseActivityHelper;
import com.balysv.materialmenu.demo.R;

public abstract class BaseActivity extends SherlockActivity {

    protected BaseActivityHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
        helper = new BaseActivityHelper();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        helper.refreshDrawerState();
    }
}
