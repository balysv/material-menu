package com.balysv.materialmenu.demo.abc;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.balysv.materialmenu.demo.BaseActivityHelper;
import com.balysv.materialmenu.demo.R;

public abstract class BaseActivity extends ActionBarActivity {

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
