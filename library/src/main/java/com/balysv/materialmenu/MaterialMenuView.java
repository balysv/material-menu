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

import android.animation.Animator;
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
import static com.balysv.materialmenu.MaterialMenuDrawable.DEFAULT_SCALE;
import static com.balysv.materialmenu.MaterialMenuDrawable.DEFAULT_TRANSFORM_DURATION;
import static com.balysv.materialmenu.MaterialMenuDrawable.DEFAULT_VISIBLE;
import static com.balysv.materialmenu.MaterialMenuDrawable.IconState;
import static com.balysv.materialmenu.MaterialMenuDrawable.Stroke;

/**
 * A basic View wrapper of {@link MaterialMenuDrawable}. Used
 * for custom view ActionBar or other layouts
 */
public class MaterialMenuView extends View implements MaterialMenu {

    private MaterialMenuDrawable drawable;

    private IconState currentState = IconState.BURGER;

    public MaterialMenuView(Context context) {
        this(context, null);
    }

    public MaterialMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray attr = getTypedArray(context, attributeSet, com.balysv.materialmenu.R.styleable.MaterialMenuView);

        try {
            int color = attr.getColor(R.styleable.MaterialMenuView_mm_color, DEFAULT_COLOR);
            boolean visible = attr.getBoolean(R.styleable.MaterialMenuView_mm_visible, DEFAULT_VISIBLE);
            int scale = attr.getInteger(R.styleable.MaterialMenuView_mm_scale, DEFAULT_SCALE);
            int transformDuration = attr.getInteger(R.styleable.MaterialMenuView_mm_transformDuration, DEFAULT_TRANSFORM_DURATION);
            Stroke stroke = Stroke.valueOf(attr.getInteger(R.styleable.MaterialMenuView_mm_strokeWidth, 0));
            boolean rtlEnabled = attr.getBoolean(R.styleable.MaterialMenuView_mm_rtlEnabled, false);
            int state = attr.getInt(R.styleable.MaterialMenuView_mm_iconState, 0);

            switch (state) {
                case 0:
                    currentState = IconState.BURGER;
                    break;
                case 1:
                    currentState = IconState.ARROW;
                    break;
                case 2:
                    currentState = IconState.X;
                    break;
                case 3:
                    currentState = IconState.CHECK;
                    break;
            }

            drawable = new MaterialMenuDrawable(context, color, stroke, scale, transformDuration);

            drawable.setIconState(currentState);
            drawable.setVisible(visible);
            drawable.setRTLEnabled(rtlEnabled);
        } finally {
            attr.recycle();
        }

        drawable.setCallback(this);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (getPaddingLeft() != 0 || getPaddingTop() != 0) {
            int saveCount = canvas.getSaveCount();
            canvas.save();
            canvas.translate(getPaddingLeft(), getPaddingTop());
            drawable.draw(canvas);
            canvas.restoreToCount(saveCount);
        } else {
            drawable.draw(canvas);
        }
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        adjustDrawablePadding();
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
    public IconState getState() {
        return drawable.getIconState();
    }

    @Override
    public void animateState(IconState state) {
        currentState = state;
        drawable.animateIconState(state);
    }

    @Override
    public void animatePressedState(IconState state) {
        animateState(state);
    }

    @Override
    public void setColor(int color) {
        drawable.setColor(color);
    }

    @Override
    public void setVisible(boolean visible) {
        drawable.setVisible(visible);
    }

    @Override
    public void setTransformationDuration(int duration) {
        drawable.setTransformationDuration(duration);
    }

    @Override
    public void setInterpolator(Interpolator interpolator) {
        drawable.setInterpolator(interpolator);
    }

    @Override
    public void setAnimationListener(Animator.AnimatorListener listener) {
        drawable.setAnimationListener(listener);
    }

    @Override
    public void setRTLEnabled(boolean rtlEnabled) {
        drawable.setRTLEnabled(rtlEnabled);
    }

    @Override
    public void setTransformationOffset(MaterialMenuDrawable.AnimationState animationState, float value) {
        currentState = drawable.setTransformationOffset(animationState, value);
    }

    @Override
    public MaterialMenuDrawable getDrawable() {
        return drawable;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int paddingX = getPaddingLeft() + getPaddingRight();
        int paddingY = getPaddingTop() + getPaddingBottom();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(drawable.getIntrinsicWidth() + paddingX, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(drawable.getIntrinsicHeight() + paddingY, MeasureSpec.EXACTLY);
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        } else {
            setMeasuredDimension(drawable.getIntrinsicWidth() + paddingX, drawable.getIntrinsicHeight() + paddingY);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        adjustDrawablePadding();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.state = currentState;
        savedState.visible = drawable != null && drawable.isDrawableVisible();
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        setState(savedState.state);
        setVisible(savedState.visible);
    }

    private void adjustDrawablePadding() {
        if (drawable != null) {
            drawable.setBounds(
                0, 0,
                drawable.getIntrinsicWidth() + getPaddingLeft() + getPaddingRight(),
                drawable.getIntrinsicHeight() + getPaddingTop() + getPaddingBottom()
            );
        }
    }

    private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    private static class SavedState extends BaseSavedState {
        protected IconState state;
        protected boolean visible;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            state = IconState.valueOf(in.readString());
            visible = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(state.name());
            out.writeByte((byte) (visible ? 1 : 0));
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