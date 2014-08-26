package com.balysv.material.drawable.menu.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;

import com.balysv.materialmenu.MaterialMenuView;

import java.util.Random;

import static com.balysv.materialmenu.MaterialMenuDrawable.IconState;

public abstract class BaseActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private MaterialMenuView materialMenuView;
    private int              materialButtonState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);

        SeekBar duration = (SeekBar) findViewById(R.id.item_animation_duration);
        duration.setMax(4600);
        duration.setProgress(2600);
        duration.setOnSeekBarChangeListener(this);

        materialMenuView = (MaterialMenuView) findViewById(R.id.material_menu_button);
        materialMenuView.setOnClickListener(this);

        findViewById(R.id.switch_item_arrow).setOnClickListener(this);
        findViewById(R.id.switch_item_menu).setOnClickListener(this);
        findViewById(R.id.switch_item_x).setOnClickListener(this);
        findViewById(R.id.switch_item_check).setOnClickListener(this);
        findViewById(R.id.animate_item_arrow).setOnClickListener(this);
        findViewById(R.id.animate_item_menu).setOnClickListener(this);
        findViewById(R.id.animate_item_x).setOnClickListener(this);
        findViewById(R.id.animate_item_check).setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.animate_item_menu:
                materialMenuView.animateState(IconState.BURGER);
                break;
            case R.id.animate_item_arrow:
                materialMenuView.animateState(IconState.ARROW);
                break;
            case R.id.animate_item_x:
                materialMenuView.animateState(IconState.X);
                break;
            case R.id.animate_item_check:
                materialMenuView.animateState(IconState.CHECK);
                break;
            case R.id.switch_item_menu:
                materialMenuView.setState(IconState.BURGER);
                break;
            case R.id.switch_item_arrow:
                materialMenuView.setState(IconState.ARROW);
                break;
            case R.id.switch_item_x:
                materialMenuView.setState(IconState.X);
                break;
            case R.id.switch_item_check:
                materialMenuView.setState(IconState.CHECK);
                break;
            case R.id.material_menu_button:
                setMainState();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_icon, menu);
        return true;
    }

    @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        materialMenuView.setTransformationDuration(400 + progress);
    }

    @Override public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override public void onStopTrackingTouch(SeekBar seekBar) {
    }

    private void setMainState() {
        materialButtonState = generateState(materialButtonState);
        materialMenuView.animatePressedState(intToState(materialButtonState));
    }

    protected int generateState(int previous) {
        int generated = new Random().nextInt(4);
        return generated != previous ? generated : generateState(previous);
    }

    protected IconState intToState(int state) {
        switch (state) {
            case 0:
                return IconState.BURGER;
            case 1:
                return IconState.ARROW;
            case 2:
                return IconState.X;
            case 3:
                return IconState.CHECK;
        }
        throw new IllegalArgumentException("Must be a number [0,3)");
    }
}
