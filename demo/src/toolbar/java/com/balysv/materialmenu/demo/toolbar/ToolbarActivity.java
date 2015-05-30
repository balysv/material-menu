package com.balysv.materialmenu.demo.toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.balysv.materialmenu.demo.BaseActivityHelper;
import com.balysv.materialmenu.demo.R;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar;

import static com.balysv.materialmenu.MaterialMenuDrawable.Stroke;
import static com.balysv.materialmenu.demo.BaseActivityHelper.generateState;
import static com.balysv.materialmenu.demo.BaseActivityHelper.intToState;


public class ToolbarActivity extends AppCompatActivity {

    private MaterialMenuIconToolbar materialMenu;

    private int actionBarMenuState;

    protected BaseActivityHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // random state
                actionBarMenuState = generateState(actionBarMenuState);
                materialMenu.animatePressedState(intToState(actionBarMenuState));
            }
        });
        materialMenu = new MaterialMenuIconToolbar(this, Color.WHITE, Stroke.THIN) {
            @Override public int getToolbarViewId() {
                return R.id.toolbar;
            }
        };
        helper = new BaseActivityHelper();
        helper.init(getWindow().getDecorView(), materialMenu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        helper.refreshDrawerState();
        materialMenu.syncState(savedInstanceState);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        materialMenu.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
}
