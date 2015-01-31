package com.balysv.materialmenu.demo.abc_v21;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconCompat;

import static com.balysv.materialmenu.MaterialMenuDrawable.Stroke;
import static com.balysv.materialmenu.demo.BaseActivityHelper.generateState;
import static com.balysv.materialmenu.demo.BaseActivityHelper.intToState;


public class HomeIconActivity extends BaseActivity {

    private MaterialMenuIconCompat materialMenu;
    private int                    actionBarMenuState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        materialMenu = new MaterialMenuIconCompat(this, Color.BLACK, Stroke.THIN);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // random state
            actionBarMenuState = generateState(actionBarMenuState);
            materialMenu.animatePressedState(intToState(actionBarMenuState));
        }
        return super.onOptionsItemSelected(item);
    }
}
