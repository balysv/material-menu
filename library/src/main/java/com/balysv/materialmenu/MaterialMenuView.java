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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

import static com.balysv.materialmenu.MaterialMenuDrawable.DEFAULT_COLOR;
import static com.balysv.materialmenu.MaterialMenuDrawable.DEFAULT_PRESSED_DURATION;
import static com.balysv.materialmenu.MaterialMenuDrawable.DEFAULT_SCALE;
import static com.balysv.materialmenu.MaterialMenuDrawable.DEFAULT_TRANSFORM_DURATION;
import static com.balysv.materialmenu.MaterialMenuDrawable.IconState;
import static com.balysv.materialmenu.MaterialMenuDrawable.Stroke;

/**
 * A basic View wrapper of {@link MaterialMenuDrawable}. Used
 * for custom view ActionBar or other layouts
 */
public class MaterialMenuView extends View implements MaterialMenu {

    private MaterialMenuDrawable drawable;

    private IconState currentState = IconState.BURGER;

    private int    color;
    private int    scale;
    private int    transformDuration;
    private int    pressedDuration;
    private Stroke stroke;

    public MaterialMenuView(Context context) {
        this(context, null);
    }

    public MaterialMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(context, attrs);

        drawable = new MaterialMenuDrawable(context, color, stroke, scale, transformDuration, pressedDuration);
        drawable.setCallback(this);
    }

    private void initAttributes(Context context, AttributeSet attributeSet) {
        TypedArray attr = getTypedArray(context, attributeSet, com.balysv.materialmenu.R.styleable.MaterialMenuView);

        if (attr == null) {
            return;
        }

        try {
            color = attr.getColor(com.balysv.materialmenu.R.styleable.MaterialMenuView_color, DEFAULT_COLOR);
            scale = attr.getInteger(com.balysv.materialmenu.R.styleable.MaterialMenuView_scale, DEFAULT_SCALE);
            transformDuration = attr.getInteger(com.balysv.materialmenu.R.styleable.MaterialMenuView_transformDuration, DEFAULT_TRANSFORM_DURATION);
            pressedDuration = attr.getInteger(com.balysv.materialmenu.R.styleable.MaterialMenuView_pressedDuration, DEFAULT_PRESSED_DURATION);
            stroke = Stroke.valueOf(attr.getInteger(R.styleable.MaterialMenuView_strokeWidth, 0));
        } finally {
            attr.recycle();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawable.draw(canvas);
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return who == drawable || super.verifyDrawable(who);
    }

    @Override
    public void setState(IconState state) {
        currentState = state;
        drawable.setIconState(state);
    }

    @Override
    public void animateState(IconState state) {
        currentState = state;
        drawable.animateIconState(state, false);
    }

    @Override
    public void animatePressedState(IconState state) {
        currentState = state;
        drawable.animateIconState(state, true);
    }

    @Override
    public void setColor(int color) {
        drawable.setColor(color);
    }

    @Override
    public void setTransformationDuration(int duration) {
        drawable.setTransformationDuration(duration);
    }

    @Override
    public void setPressedDuration(int duration) {
        drawable.setPressedDuration(duration);
    }

    @Override
    public void setInterpolator(Interpolator interpolator) {
        drawable.setInterpolator(interpolator);
    }

    @Override
    public MaterialMenuDrawable getDrawable() {
        return drawable;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(drawable.getIntrinsicWidth(), MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(drawable.getIntrinsicHeight(), MeasureSpec.EXACTLY);
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        } else {
            setMeasuredDimension(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.state = currentState;
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        setState(savedState.state);
    }

    private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    private static class SavedState extends BaseSavedState {
        protected IconState state;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            state = IconState.valueOf(in.readString());
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(state.name());
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}