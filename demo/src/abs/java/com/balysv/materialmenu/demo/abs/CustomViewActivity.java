package com.balysv.materialmenu.demo.abs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.balysv.materialmenu.MaterialMenuView;
import com.balysv.materialmenu.demo.R;

import static com.balysv.materialmenu.demo.BaseActivityHelper.generateState;
import static com.balysv.materialmenu.demo.BaseActivityHelper.intToState;

public class CustomViewActivity extends BaseActivity implements View.OnClickListener {

    private MaterialMenuView materialMenu;
    private int              actionBarMenuState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCustomActionBar();
        helper.init(getWindow().getDecorView(), materialMenu);
    }

    private void initCustomActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.action_bar);
        materialMenu = (MaterialMenuView) actionBar.getCustomView().findViewById(R.id.action_bar_menu);
        materialMenu.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        if (v.getId() == R.id.action_bar_menu) {
            // random state on click
            actionBarMenuState = generateState(actionBarMenuState);
            materialMenu.animatePressedState(intToState(actionBarMenuState));
            return;
        }
        helper.onClick(v);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.custom_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, HomeIconActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
