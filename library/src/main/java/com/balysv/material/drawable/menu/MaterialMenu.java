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

package com.balysv.material.drawable.menu;

import android.view.animation.Interpolator;

import static com.balysv.material.drawable.menu.MaterialMenuDrawable.IconState;

/**
 * API for interaction with {@link MaterialMenuDrawable}
 */
public interface MaterialMenu {
    /**
     * Change icon without animation
     *
     * @param state new icon state
     */
    public void setState(IconState state);

    /**
     * Animate icon to given state.
     *
     * @param state new icon state
     */
    public void animateState(IconState state);

    /**
     * Animate icon to given state and draw touch circle
     *
     * @param state new icon state
     */
    public void animatePressedState(IconState state);

    /**
     * Set color of icon
     *
     * @param color new icon color
     */
    public void setColor(int color);

    /**
     * Set duration of transformation animations
     *
     * @param duration new animation duration
     */
    public void setTransformationDuration(int duration);

    /**
     * Set duration of pressed state circle animation
     *
     * @param duration new animation duration
     */
    public void setPressedDuration(int duration);

    /**
     * Set interpolator for transformation animations
     *
     * @param interpolator new interpolator
     */
    public void setInterpolator(Interpolator interpolator);

    /**
     * @return {@link MaterialMenuDrawable} to be used for the menu
     */
    public abstract MaterialMenuDrawable getDrawable();
}
