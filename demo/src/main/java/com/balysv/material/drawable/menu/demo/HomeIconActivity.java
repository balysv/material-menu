package com.balysv.material.drawable.menu.demo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

import com.balysv.materialmenu.MaterialMenuIcon;


public class HomeIconActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private MaterialMenuIcon materialMenu;
    private int              actionBarMenuState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        materialMenu = new MaterialMenuIcon(this, Color.WHITE);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        materialMenu.syncState(savedInstanceState);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        materialMenu.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, CustomViewActivity.class));
            finish();
            return true;
        }
        if (id == android.R.id.home) {
            // random state
            actionBarMenuState = generateState(actionBarMenuState);
            materialMenu.animatePressedState(intToState(actionBarMenuState));
        }
        return super.onOptionsItemSelected(item);
    }
}
