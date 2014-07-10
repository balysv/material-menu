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


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.Property;
import android.util.TypedValue;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import static android.graphics.Paint.Style;


public class MaterialMenuDrawable extends Drawable implements Animatable {

    public enum IconState {
        BURGER, ARROW, X
    }

    public static final int DEFAULT_COLOR              = Color.WHITE;
    public static final int DEFAULT_SCALE              = 1;
    public static final int DEFAULT_TRANSFORM_DURATION = 800;
    public static final int DEFAULT_PRESSED_DURATION   = 400;

    private enum AnimationState {
        BURGER_ARROW, BURGER_X, ARROW_X
    }

    private static final int BASE_DRAWABLE_WIDTH  = 40;
    private static final int BASE_DRAWABLE_HEIGHT = 40;
    private static final int BASE_ICON_WIDTH      = 20;
    private static final int BASE_CIRCLE_RADIUS   = 18;
    private static final int BASE_GRID_OFFSET     = 6;

    private static final float ARROW_MID_LINE_ANGLE = 180;
    private static final float ARROW_TOP_LINE_ANGLE = 135;
    private static final float ARROW_BOT_LINE_ANGLE = 225;
    private static final float X_TOP_LINE_ANGLE     = 44;
    private static final float X_BOT_LINE_ANGLE     = -44;
    private static final float X_ROTATION_ANGLE     = 90;

    private static final float TRANSFORMATION_START = 0;
    private static final float TRANSFORMATION_MID   = 1.0f;
    private static final float TRANSFORMATION_END   = 2.0f;

    private static final int DEFAULT_CIRCLE_ALPHA = 200;

    private final float dip1;
    private final float dip2;
    private final float dip3;
    private final float dip4;
    private final float dip6;

    private final int   width;
    private final int   height;
    private final float strokeWidth;
    private final float iconWidth;
    private final float topPadding;
    private final float sidePadding;
    private final float circleRadius;
    private final float gridOffset;

    private final Paint gridPaint;
    private final Paint iconPaint;
    private final Paint circlePaint;

    private float   transformationValue   = 0f;
    private float   pressedProgressValue  = 0f;
    private boolean transformationRunning = false;
    private boolean drawGrid              = false;

    private IconState      currentIconState = IconState.BURGER;
    private AnimationState animationState   = AnimationState.BURGER_ARROW;

    private IconState animatingIconState;
    private boolean   drawTouchCircle;
    private boolean   neverDrawTouch;

    private ObjectAnimator transformation;
    private ObjectAnimator pressedCircle;

    public MaterialMenuDrawable(Context context, int color, int scale, int transformDuration, int pressedDuration) {
        Resources resources = context.getResources();
        // convert each separately due to various densities
        this.dip1 = dpToPx(resources, 1) * scale;
        this.dip2 = dpToPx(resources, 2) * scale;
        this.dip3 = dpToPx(resources, 3) * scale;
        this.dip4 = dpToPx(resources, 4) * scale;
        this.dip6 = dpToPx(resources, 6) * scale;

        // do not change this value or else things will break
        this.strokeWidth = dip3;
        this.width = (int) (dpToPx(resources, BASE_DRAWABLE_WIDTH) * scale);
        this.height = (int) (dpToPx(resources, BASE_DRAWABLE_HEIGHT) * scale);
        this.iconWidth = dpToPx(resources, BASE_ICON_WIDTH) * scale;
        this.circleRadius = dpToPx(resources, BASE_CIRCLE_RADIUS) * scale;

        this.sidePadding = (width - iconWidth) / 2;
        this.topPadding = (height - 5 * strokeWidth) / 2;

        this.gridOffset = dpToPx(resources, BASE_GRID_OFFSET) * scale;
        gridPaint = new Paint();
        gridPaint.setAntiAlias(false);
        gridPaint.setColor(Color.GREEN);
        gridPaint.setStrokeWidth(1);

        iconPaint = new Paint();
        iconPaint.setAntiAlias(true);
        iconPaint.setStyle(Style.STROKE);
        iconPaint.setStrokeWidth(strokeWidth);
        iconPaint.setColor(color);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Style.FILL);
        circlePaint.setColor(color);
        circlePaint.setAlpha(DEFAULT_CIRCLE_ALPHA);

        setBounds(0, 0, width, height);
        initAnimations(transformDuration, pressedDuration);
    }

    /*
     * Drawing
     */

    @Override public void draw(Canvas canvas) {
        if (drawGrid) drawGrid(canvas);

        final float ratio = transformationValue <= 1 ? transformationValue : 2 - transformationValue;
        drawTopLine(canvas, ratio);
        drawMiddleLine(canvas, ratio);
        drawBottomLine(canvas, ratio);

        if (drawTouchCircle) drawTouchCircle(canvas);
    }

    private void drawTouchCircle(Canvas canvas) {
        canvas.restore();
        canvas.drawCircle(width / 2, height / 2, pressedProgressValue, circlePaint);
    }

    private void drawMiddleLine(Canvas canvas, float ratio) {
        canvas.restore();
        canvas.save();

        float rotation = 0;
        float startX = sidePadding;
        float startY = topPadding + strokeWidth / 2 * 5;
        float stopX = width - sidePadding;
        float stopY = topPadding + strokeWidth / 2 * 5;
        int alpha = 255;

        switch (animationState) {
            case BURGER_ARROW:
                // rotate by 180 and shorten one end
                rotation = transformationValue * ARROW_MID_LINE_ANGLE;
                stopX -= ratio * strokeWidth / 2;
                break;
            case BURGER_X:
                // fade out
                alpha = Math.max(0, Math.min(255, (int) ((1 - ratio) * 255)));
                break;
            case ARROW_X:
                // fade out and shorten one end
                alpha = Math.max(0, Math.min(255, (int) ((1 - ratio) * 255)));
                startX += (1 - ratio) * strokeWidth / 2;
                break;
        }

        iconPaint.setAlpha(alpha);
        canvas.rotate(rotation, width / 2, height / 2);
        canvas.drawLine(startX, startY, stopX, stopY, iconPaint);
        iconPaint.setAlpha(255);
    }

    private void drawTopLine(Canvas canvas, float ratio) {
        canvas.save();

        float rotation = 0, pivotX = 0, pivotY = 0;
        float rotation2 = 0;
        // pivot at center of line
        float pivotX2 = width / 2 + strokeWidth / 2;
        float pivotY2 = topPadding + dip2;

        float startX = sidePadding;
        float startY = topPadding + dip2;
        float stopX = width - sidePadding;
        float stopY = topPadding + dip2;

        switch (animationState) {
            case BURGER_ARROW:
                if (transformationValue <= TRANSFORMATION_MID) {
                    // rotate until required angle
                    rotation = ratio * ARROW_BOT_LINE_ANGLE;
                } else {
                    // rotate back to start doing a 360
                    rotation = ARROW_BOT_LINE_ANGLE + (1 - ratio) * ARROW_TOP_LINE_ANGLE;
                }
                // rotate by middle
                pivotX = width / 2;
                pivotY = height / 2;

                // shorten both ends
                startX += dip3 * ratio;
                stopX -= dip3 * ratio;

                break;
            case BURGER_X:
                // rotate until required angles
                rotation = X_TOP_LINE_ANGLE * ratio;
                rotation2 = X_ROTATION_ANGLE * ratio;

                // pivot at left corner of line
                pivotX = sidePadding + dip4;
                pivotY = topPadding + dip3;

                // lengthen one end
                stopX += dip3 * ratio;
                break;
            case ARROW_X:
                // rotate from ARROW angle to X angle
                rotation = ARROW_BOT_LINE_ANGLE + (X_TOP_LINE_ANGLE - ARROW_BOT_LINE_ANGLE) * ratio;
                rotation2 = X_ROTATION_ANGLE * ratio;

                // move pivot from ARROW pivot to X pivot
                pivotX = width / 2 + (sidePadding + dip4 - width / 2) * ratio;
                pivotY = height / 2 + (topPadding + dip3 - height / 2) * ratio;

                // lengthen both ends
                startX += dip3 * (1 - ratio);
                stopX -= dip3 - (dip6 * ratio);
                break;
        }

        canvas.rotate(rotation, pivotX, pivotY);
        canvas.rotate(rotation2, pivotX2, pivotY2);
        canvas.drawLine(startX, startY, stopX, stopY, iconPaint);
    }

    private void drawBottomLine(Canvas canvas, float ratio) {
        canvas.restore();
        canvas.save();

        float rotation = 0, pivotX = 0, pivotY = 0;
        float rotation2 = 0;
        // pivot at center of line
        float pivotX2 = width / 2 + strokeWidth / 2;
        float pivotY2 = height - topPadding - dip2;

        float startX = sidePadding;
        float startY = height - topPadding - dip2;
        float stopX = width - sidePadding;
        float stopY = height - topPadding - dip2;

        switch (animationState) {
            case BURGER_ARROW:
                if (transformationValue <= TRANSFORMATION_MID) {
                    // rotate to required angle
                    rotation = ARROW_TOP_LINE_ANGLE * ratio;
                } else {
                    // rotate back to start doing a 360
                    rotation = ARROW_TOP_LINE_ANGLE + (1 - ratio) * ARROW_BOT_LINE_ANGLE;
                }
                // pivot center of canvas
                pivotX = width / 2;
                pivotY = height / 2;

                // shorten both ends
                startX = sidePadding + dip3 * ratio;
                stopX = width - sidePadding - dip3 * ratio;
                break;
            case BURGER_X:
                if (transformationValue <= TRANSFORMATION_MID) {
                    // rotate around
                    rotation2 = -X_ROTATION_ANGLE * ratio;
                } else {
                    // rotate directly
                    rotation2 = X_ROTATION_ANGLE * ratio;
                }
                // rotate to required angle
                rotation = X_BOT_LINE_ANGLE * ratio;

                // pivot left corner of line
                pivotX = sidePadding + dip4;
                pivotY = height - topPadding - dip3;

                // lengthen one end
                stopX += dip3 * ratio;
                break;
            case ARROW_X:
                // rotate from ARROW angle to X angle
                rotation = ARROW_TOP_LINE_ANGLE + (360 + X_BOT_LINE_ANGLE - ARROW_TOP_LINE_ANGLE) * ratio;
                rotation2 = -X_ROTATION_ANGLE * ratio;

                // move pivot from ARROW pivot to X pivot
                pivotX = width / 2 + (sidePadding + dip4 - width / 2) * ratio;
                pivotY = height / 2 + (height / 2 - topPadding - dip3) * ratio;

                // lengthen both ends
                startX += dip3 * (1 - ratio);
                stopX -= dip3 - dip6 * ratio;
                break;
        }

        canvas.rotate(rotation, pivotX, pivotY);
        canvas.rotate(rotation2, pivotX2, pivotY2);
        canvas.drawLine(startX, startY, stopX, stopY, iconPaint);
    }


    private void drawGrid(Canvas canvas) {
        for (int i = 0; i < width + 1; i += dip1) {
            if (i % sidePadding == 0) gridPaint.setColor(Color.BLUE);
            canvas.drawLine(i, 0, i, height, gridPaint);
            if (i % sidePadding == 0) gridPaint.setColor(Color.GREEN);
        }

        for (int i = 0; i < height + 1; i += dip1) {
            if (i % gridOffset == 0) gridPaint.setColor(Color.BLUE);
            canvas.drawLine(0, i, width, i, gridPaint);
            if (i % gridOffset == 0) gridPaint.setColor(Color.GREEN);
        }
    }

    @Override public void setAlpha(int alpha) {
        iconPaint.setAlpha(alpha);
    }

    @Override public void setColorFilter(ColorFilter cf) {
        iconPaint.setColorFilter(cf);
    }

    @Override public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    /*
     * Accessor methods
     */

    public void setColor(int color) {
        iconPaint.setColor(color);
        circlePaint.setColor(color);
        invalidateSelf();
    }

    protected void setDrawGrid(boolean drawGrid) {
        this.drawGrid = drawGrid;
    }

    public void setTransformationDuration(int duration) {
        transformation.setDuration(duration);
    }

    public void setPressedDuration(int duration) {
        pressedCircle.setDuration(duration);
    }

    public void setInterpolator(Interpolator interpolator) {
        transformation.setInterpolator(interpolator);
    }

    public void setNeverDrawTouch(boolean neverDrawTouch) {
        this.neverDrawTouch = neverDrawTouch;
    }

    public synchronized void setIconState(IconState iconState) {
        if (currentIconState == iconState) return;

        switch (iconState) {
            case BURGER:
                animationState = AnimationState.BURGER_ARROW;
                transformationValue = TRANSFORMATION_START;
                break;
            case ARROW:
                animationState = AnimationState.BURGER_ARROW;
                transformationValue = TRANSFORMATION_MID;
                break;
            case X:
                animationState = AnimationState.BURGER_X;
                transformationValue = TRANSFORMATION_MID;
                break;
        }
        currentIconState = iconState;
        invalidateSelf();
    }

    public synchronized void animateIconState(IconState state, boolean drawTouch) {
        if (!transformationRunning) {
            drawTouchCircle = drawTouch;
            animatingIconState = state;
            start();
        }
    }

    /*
     * Animations
     */
    private Property<MaterialMenuDrawable, Float> transformationProperty
        = new Property<MaterialMenuDrawable, Float>(Float.class, "transformation") {
        @Override
        public Float get(MaterialMenuDrawable object) {
            return object.getTransformationValue();
        }

        @Override
        public void set(MaterialMenuDrawable object, Float value) {
            object.setTransformationValue(value);
        }
    };

    private Property<MaterialMenuDrawable, Float> pressedProgressProperty
        = new Property<MaterialMenuDrawable, Float>(Float.class, "pressedProgress") {
        @Override
        public Float get(MaterialMenuDrawable object) {
            return object.getPressedProgress();
        }

        @Override
        public void set(MaterialMenuDrawable object, Float value) {
            object.setPressedProgress(value);
        }
    };

    public Float getTransformationValue() {
        return transformationValue;
    }

    public void setTransformationValue(Float value) {
        this.transformationValue = value;
        invalidateSelf();
    }

    public Float getPressedProgress() {
        return pressedProgressValue;
    }

    public void setPressedProgress(Float value) {
        this.pressedProgressValue = value;
        circlePaint.setAlpha((int) (DEFAULT_CIRCLE_ALPHA * (1 - value / (circleRadius * 1.22f))));
        invalidateSelf();
    }

    private void initAnimations(int transformDuration, int pressedDuration) {
        transformation = ObjectAnimator.ofFloat(this, transformationProperty, 0);
        transformation.setInterpolator(new DecelerateInterpolator(3));
        transformation.setDuration(transformDuration);
        transformation.addListener(new SimpleAnimatorListener() {
            @Override public void onAnimationEnd(Animator animation) {
                transformationRunning = false;
                setIconState(animatingIconState);
            }
        });


        pressedCircle = ObjectAnimator.ofFloat(this, pressedProgressProperty, 0, 0);
        pressedCircle.setDuration(pressedDuration);
        pressedCircle.setInterpolator(new DecelerateInterpolator());
        pressedCircle.addListener(new SimpleAnimatorListener() {
            @Override public void onAnimationEnd(Animator animation) {
                pressedProgressValue = 0;
            }

            @Override public void onAnimationCancel(Animator animation) {
                pressedProgressValue = 0;
            }
        });
    }

    private boolean resolveTransformation() {
        boolean isCurrentBurger = currentIconState == IconState.BURGER;
        boolean isCurrentArrow = currentIconState == IconState.ARROW;
        boolean isCurrentX = currentIconState == IconState.X;
        boolean isAnimatingBurger = animatingIconState == IconState.BURGER;
        boolean isAnimatingArrow = animatingIconState == IconState.ARROW;
        boolean isAnimatingX = animatingIconState == IconState.X;

        if ((isCurrentBurger && isAnimatingArrow) || (isCurrentArrow && isAnimatingBurger)) {
            animationState = AnimationState.BURGER_ARROW;
            return isCurrentBurger;
        }

        if ((isCurrentArrow && isAnimatingX) || (isCurrentX && isAnimatingArrow)) {
            animationState = AnimationState.ARROW_X;
            return isCurrentArrow;
        }

        if ((isCurrentBurger && isAnimatingX) || (isCurrentX && isAnimatingBurger)) {
            animationState = AnimationState.BURGER_X;
            return isCurrentBurger;
        }

        throw new IllegalStateException("Current icon state or animating icon state are not set");
    }

    @Override public void start() {
        if (transformationRunning || animatingIconState == null || animatingIconState == currentIconState) return;
        transformationRunning = true;

        final boolean direction = resolveTransformation();
        transformation.setFloatValues(
            direction ? TRANSFORMATION_START : TRANSFORMATION_MID,
            direction ? TRANSFORMATION_MID : TRANSFORMATION_END
        );
        transformation.start();

        if (pressedCircle.isRunning()) {
            pressedCircle.cancel();
        }
        if (drawTouchCircle && !neverDrawTouch) {
            pressedCircle.setFloatValues(pressedProgressValue, circleRadius * 1.22f);
            pressedCircle.start();
        }

        invalidateSelf();
    }

    @Override public void stop() {
        if (!isRunning()) return;
        if (transformation.isRunning()) {
            transformation.end();
        } else {
            transformationRunning = false;
            invalidateSelf();
        }
    }

    @Override public boolean isRunning() {
        return transformationRunning;
    }

    @Override
    public int getIntrinsicWidth() {
        return width;
    }

    @Override
    public int getIntrinsicHeight() {
        return height;
    }

    private class SimpleAnimatorListener implements Animator.AnimatorListener {
        @Override public void onAnimationStart(Animator animation) {
        }

        @Override public void onAnimationEnd(Animator animation) {
        }

        @Override public void onAnimationCancel(Animator animation) {
        }

        @Override public void onAnimationRepeat(Animator animation) {
        }
    }

    static float dpToPx(Resources resources, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }
}
