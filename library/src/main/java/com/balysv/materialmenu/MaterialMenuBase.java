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

import android.os.Bundle;
import android.view.animation.Interpolator;

/**
 * Base class for ActionBar implementations of {@link MaterialMenuDrawable}
 *
 * @see MaterialMenuIcon
 */
public abstract class MaterialMenuBase implements MaterialMenu {

    private static final String STATE_KEY = "material_menu_icon_state";

    protected MaterialMenuDrawable.IconState currentState = MaterialMenuDrawable.IconState.BURGER;

    public final void setState(MaterialMenuDrawable.IconState state) {
        currentState = state;
        getDrawable().setIconState(state);
    }

    public final void animateState(MaterialMenuDrawable.IconState state) {
        currentState = state;
        getDrawable().animateIconState(state, false);
    }

    public final void animatePressedState(MaterialMenuDrawable.IconState state) {
        currentState = state;
        getDrawable().animateIconState(state, true);
    }

    public final void setColor(int color) {
        getDrawable().setColor(color);
    }

    public final void setTransformationDuration(int duration) {
        getDrawable().setTransformationDuration(duration);
    }

    public final void setPressedDuration(int duration) {
        getDrawable().setPressedDuration(duration);
    }

    public final void setInterpolator(Interpolator interpolator) {
        getDrawable().setInterpolator(interpolator);
    }

    public abstract MaterialMenuDrawable getDrawable();

    /**
     * Overwrites behaviour of pressed state circle animation even when using {@link #animatePressedState(com.balysv.materialmenu.MaterialMenuDrawable.IconState)}
     *
     * @param neverDrawTouch true to never draw pressed state circle animation
     */
    public final void setNeverDrawTouch(boolean neverDrawTouch) {
        getDrawable().setNeverDrawTouch(neverDrawTouch);
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
