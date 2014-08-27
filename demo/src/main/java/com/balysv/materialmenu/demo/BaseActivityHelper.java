package com.balysv.materialmenu.demo;

import android.view.View;
import android.widget.SeekBar;

import com.balysv.materialmenu.MaterialMenuView;

import java.util.Random;

import static com.balysv.materialmenu.MaterialMenuDrawable.IconState;

public class BaseActivityHelper implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private MaterialMenuView materialMenuView;
    private int              materialButtonState;

    public void init(View parent) {
        SeekBar duration = (SeekBar) parent.findViewById(R.id.item_animation_duration);
        duration.setMax(4600);
        duration.setProgress(2600);
        duration.setOnSeekBarChangeListener(this);

        materialMenuView = (MaterialMenuView) parent.findViewById(R.id.material_menu_button);
        materialMenuView.setOnClickListener(this);

        parent.findViewById(R.id.switch_item_arrow).setOnClickListener(this);
        parent.findViewById(R.id.switch_item_menu).setOnClickListener(this);
        parent.findViewById(R.id.switch_item_x).setOnClickListener(this);
        parent.findViewById(R.id.switch_item_check).setOnClickListener(this);
        parent.findViewById(R.id.animate_item_arrow).setOnClickListener(this);
        parent.findViewById(R.id.animate_item_menu).setOnClickListener(this);
        parent.findViewById(R.id.animate_item_x).setOnClickListener(this);
        parent.findViewById(R.id.animate_item_check).setOnClickListener(this);
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

    public static int generateState(int previous) {
        int generated = new Random().nextInt(4);
        return generated != previous ? generated : generateState(previous);
    }

    public static IconState intToState(int state) {
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
