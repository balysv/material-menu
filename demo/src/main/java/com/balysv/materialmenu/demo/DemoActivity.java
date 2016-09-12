package com.balysv.materialmenu.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SeekBar;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;

import java.util.Random;

import static com.balysv.materialmenu.MaterialMenuDrawable.Stroke;


public class DemoActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private Toolbar          toolbar;
    private MaterialMenuView materialMenuView;
    private int              materialButtonState;
    private DrawerLayout     drawerLayout;
    private boolean          direction;
    private int              actionBarMenuState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup
        setContentView(R.layout.demo);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MaterialMenuDrawable materialMenu = new MaterialMenuDrawable(this, Color.WHITE, Stroke.THIN);
        toolbar.setNavigationIcon(materialMenu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // random state
                actionBarMenuState = generateState(actionBarMenuState);
                getMaterialMenu(toolbar).animateIconState(intToState(actionBarMenuState));
            }
        });

        // Demo view initialization
        initViews();
        drawerLayout.postDelayed(new Runnable() {
            @Override public void run() {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }, 1500);
    }

    private void initViews() {
        materialMenuView = (MaterialMenuView) findViewById(R.id.material_menu_button);
        materialMenuView.setOnClickListener(this);

        drawerLayout = ((DrawerLayout) findViewById(R.id.drawer_layout));
        drawerLayout.setScrimColor(Color.parseColor("#66000000"));
        drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                getMaterialMenu(toolbar).setTransformationOffset(
                    MaterialMenuDrawable.AnimationState.BURGER_ARROW,
                    direction ? 2 - slideOffset : slideOffset
                );
            }

            @Override
            public void onDrawerOpened(android.view.View drawerView) {
                direction = true;
            }

            @Override
            public void onDrawerClosed(android.view.View drawerView) {
                direction = false;
            }
        });

        SeekBar duration = (SeekBar) findViewById(R.id.item_animation_duration);
        duration.setMax(4600);
        duration.setProgress(2600);
        duration.setOnSeekBarChangeListener(this);

        findViewById(R.id.switch_item_arrow).setOnClickListener(this);
        findViewById(R.id.switch_item_menu).setOnClickListener(this);
        findViewById(R.id.switch_item_x).setOnClickListener(this);
        findViewById(R.id.switch_item_check).setOnClickListener(this);
        findViewById(R.id.switch_item_show).setOnClickListener(this);
        findViewById(R.id.switch_item_hide).setOnClickListener(this);
        findViewById(R.id.animate_item_arrow).setOnClickListener(this);
        findViewById(R.id.animate_item_menu).setOnClickListener(this);
        findViewById(R.id.animate_item_x).setOnClickListener(this);
        findViewById(R.id.animate_item_check).setOnClickListener(this);
    }

    @Override protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        refreshDrawerState();
    }

    @Override public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.animate_item_menu:
                materialMenuView.animateIconState(MaterialMenuDrawable.IconState.BURGER);
                break;
            case R.id.animate_item_arrow:
                materialMenuView.animateIconState(MaterialMenuDrawable.IconState.ARROW);
                break;
            case R.id.animate_item_x:
                materialMenuView.animateIconState(MaterialMenuDrawable.IconState.X);
                break;
            case R.id.animate_item_check:
                materialMenuView.animateIconState(MaterialMenuDrawable.IconState.CHECK);
                break;
            case R.id.switch_item_menu:
                materialMenuView.setIconState(MaterialMenuDrawable.IconState.BURGER);
                break;
            case R.id.switch_item_arrow:
                materialMenuView.setIconState(MaterialMenuDrawable.IconState.ARROW);
                break;
            case R.id.switch_item_x:
                materialMenuView.setIconState(MaterialMenuDrawable.IconState.X);
                break;
            case R.id.switch_item_check:
                materialMenuView.setIconState(MaterialMenuDrawable.IconState.CHECK);
                break;
            case R.id.switch_item_show:
                materialMenuView.setVisible(true);
                break;
            case R.id.switch_item_hide:
                materialMenuView.setVisible(false);
                break;
            case R.id.material_menu_button:
                setMainState();
                break;
        }
    }

    private void setMainState() {
        materialButtonState = generateState(materialButtonState);
        materialMenuView.animateIconState(intToState(materialButtonState));
    }

    private void refreshDrawerState() {
        this.direction = drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        materialMenuView.setTransformationDuration(400 + progress);
    }

    @Override public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override public void onStopTrackingTouch(SeekBar seekBar) {
    }

    private static MaterialMenuDrawable getMaterialMenu(Toolbar toolbar) {
        return (MaterialMenuDrawable) toolbar.getNavigationIcon();
    }

    private static int generateState(int previous) {
        int generated = new Random().nextInt(4);
        return generated != previous ? generated : generateState(previous);
    }

    private static MaterialMenuDrawable.IconState intToState(int state) {
        switch (state) {
            case 0:
                return MaterialMenuDrawable.IconState.BURGER;
            case 1:
                return MaterialMenuDrawable.IconState.ARROW;
            case 2:
                return MaterialMenuDrawable.IconState.X;
            case 3:
                return MaterialMenuDrawable.IconState.CHECK;
        }
        throw new IllegalArgumentException("Must be a number [0,3)");
    }
}
