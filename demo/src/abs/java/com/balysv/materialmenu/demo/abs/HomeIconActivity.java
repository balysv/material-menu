package com.balysv.materialmenu.demo.abs;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.balysv.materialmenu.demo.R;
import com.balysv.materialmenu.extras.abs.MaterialMenuIconSherlock;

import static com.balysv.materialmenu.MaterialMenuDrawable.Stroke;
import static com.balysv.materialmenu.demo.BaseActivityHelper.generateState;
import static com.balysv.materialmenu.demo.BaseActivityHelper.intToState;


public class HomeIconActivity extends BaseActivity {

    private MaterialMenuIconSherlock materialMenu;
    private int                    actionBarMenuState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        materialMenu = new MaterialMenuIconSherlock(this, Color.WHITE, Stroke.THIN);
        helper.init(getWindow().getDecorView(), materialMenu);
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
        getSupportMenuInflater().inflate(R.menu.home_icon, menu);
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
