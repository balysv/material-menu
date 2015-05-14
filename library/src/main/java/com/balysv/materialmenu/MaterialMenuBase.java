/*
 * Copyright (C) 2014 Balys Valentukevicius
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.balysv.materialmenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

import com.nineoldandroids.animation.Animator;

import static com.balysv.materialmenu.MaterialMenuDrawable.DEFAULT_SCALE;
import static com.balysv.materialmenu.MaterialMenuDrawable.DEFAULT_TRANSFORM_DURATION;

/**
 * Base class for ActionBar implementations of {@link MaterialMenuDrawable}
 *
 * @see MaterialMenuIcon
 */
public abstract class MaterialMenuBase implements MaterialMenu {

    private static final String STATE_KEY = "material_menu_icon_state";
    private MaterialMenuDrawable.IconState currentState = MaterialMenuDrawable.IconState.BURGER;

    private MaterialMenuDrawable drawable;

    public MaterialMenuBase(Activity activity, int color, MaterialMenuDrawable.Stroke stroke) {
        this(activity, color, stroke, DEFAULT_TRANSFORM_DURATION);
    }

    public MaterialMenuBase(Activity activity, int color, MaterialMenuDrawable.Stroke stroke, int transformDuration) {
        drawable = new MaterialMenuDrawable(activity, color, stroke, DEFAULT_SCALE, transformDuration);
        setActionBarSettings(activity);
        if (providesActionBar()) {
            setupActionBar(activity);
        }
    }

    private void setupActionBar(Activity activity) {
        final View iconView = getActionBarHomeView(activity);
        final View upView = getActionBarUpView(activity);

        if (iconView == null || upView == null) {
            throw new IllegalStateException("Could not find ActionBar views");
        }

        // need no margins so that clicked state would work nicely
        ViewGroup.MarginLayoutParams iconParams = (ViewGroup.MarginLayoutParams) iconView.getLayoutParams();
        iconParams.bottomMargin = 0;
        iconParams.topMargin = 0;
        iconParams.leftMargin = 0;
        iconView.setLayoutParams(iconParams);

        // remove up arrow margins
        ViewGroup.MarginLayoutParams upParams = (ViewGroup.MarginLayoutParams) upView.getLayoutParams();
        upParams.leftMargin = activity.getResources().getDimensionPixelSize(R.dimen.mm_up_arrow_margin);
        upParams.rightMargin = 0;
        upView.setLayoutParams(upParams);
    }

    protected abstract void setActionBarSettings(Activity activity);

    protected abstract View getActionBarHomeView(Activity activity);

    protected abstract View getActionBarUpView(Activity activity);

    protected abstract boolean providesActionBar();

    @Override
    public final void setState(MaterialMenuDrawable.IconState state) {
        currentState = state;
        getDrawable().setIconState(state);
    }

    @Override
    public final MaterialMenuDrawable.IconState getState() {
        return getDrawable().getIconState();
    }

    @Override
    public final void animateState(MaterialMenuDrawable.IconState state) {
        currentState = state;
        getDrawable().animateIconState(state);
    }

    @Override
    public final void animatePressedState(MaterialMenuDrawable.IconState state) {
        animateState(state);
    }

    @Override
    public final void setColor(int color) {
        getDrawable().setColor(color);
    }

    @Override
    public final void setVisible(boolean visible) {
        getDrawable().setVisible(visible);
    }

    @Override
    public final void setTransformationDuration(int duration) {
        getDrawable().setTransformationDuration(duration);
    }

    @Override
    public final void setInterpolator(Interpolator interpolator) {
        getDrawable().setInterpolator(interpolator);
    }

    @Override
    public final void setAnimationListener(Animator.AnimatorListener listener) {
        getDrawable().setAnimationListener(listener);
    }

    @Override
    public final void setRTLEnabled(boolean rtlEnabled) {
        getDrawable().setRTLEnabled(rtlEnabled);
    }

    @Override
    public final void setTransformationOffset(MaterialMenuDrawable.AnimationState animationState, float value) {
        currentState = getDrawable().setTransformationOffset(animationState, value);
    }

    @Override
    public final MaterialMenuDrawable getDrawable() {
        return drawable;
    }

    /**
     * Call from {@link android.app.Activity#onSaveInstanceState(android.os.Bundle)} to store current icon state
     *
     * @param outState outState
     */
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_KEY, currentState.name());
    }

    /**
     * Call from {@link android.app.Activity#onPostCreate(android.os.Bundle)} to restore icon state
     *
     * @param state state
     */
    public void syncState(Bundle state) {
        if (state != null) {
            String iconStateName = state.getString(STATE_KEY);
            if (iconStateName == null) {
                iconStateName = MaterialMenuDrawable.IconState.BURGER.name();
            }
            setState(MaterialMenuDrawable.IconState.valueOf(iconStateName));
        }
    }
}
