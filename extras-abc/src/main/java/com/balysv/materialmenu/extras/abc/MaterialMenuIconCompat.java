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

package com.balysv.materialmenu.extras.abc;

import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;

import com.balysv.materialmenu.MaterialMenuBase;
import com.balysv.materialmenu.MaterialMenuDrawable;

import static com.balysv.materialmenu.MaterialMenuDrawable.DEFAULT_PRESSED_DURATION;
import static com.balysv.materialmenu.MaterialMenuDrawable.DEFAULT_SCALE;
import static com.balysv.materialmenu.MaterialMenuDrawable.DEFAULT_TRANSFORM_DURATION;
import static com.balysv.materialmenu.MaterialMenuDrawable.Stroke;

/**
 * A helper class for implementing {@link com.balysv.materialmenu.MaterialMenuDrawable}
 * as an {@link android.support.v7.app.ActionBar} Compat icon.
 * <p/>
 * In order to preserve default ActionBar icon click state call {@link com.balysv.materialmenu.MaterialMenuBase#setNeverDrawTouch(boolean)}.
 * Otherwise, adjust your theme to disable pressed background color by setting <code>android:actionBarItemBackground</code>
 * to null and use <code>android:actionButtonStyle</code>, <code>android:actionOverflowButtonStyle</code> to enable other
 * menu icon backgrounds.
 * <p/>
 * Disables ActionBar Up arrow and replaces default drawable using {@link ActionBar#setIcon(android.graphics.drawable.Drawable)}
 */
public class MaterialMenuIconCompat extends MaterialMenuBase {

    private MaterialMenuDrawable drawable;

    public MaterialMenuIconCompat(ActionBarActivity activity, int color, Stroke stroke) {
        this(activity, color, stroke, DEFAULT_TRANSFORM_DURATION, DEFAULT_PRESSED_DURATION);
    }

    public MaterialMenuIconCompat(ActionBarActivity activity, int color, Stroke stroke, int transformDuration) {
        this(activity, color, stroke, transformDuration, DEFAULT_PRESSED_DURATION);
    }

    public MaterialMenuIconCompat(ActionBarActivity activity, int color, Stroke stroke, int transformDuration, int pressedDuration) {
        drawable = new MaterialMenuDrawable(activity, color, stroke, DEFAULT_SCALE, transformDuration, pressedDuration);
        setupActionBar(activity);
    }

    private void setupActionBar(ActionBarActivity activity) {
        View view;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Resources resources = activity.getResources();
            int id = resources.getIdentifier("android:id/home", null, null);
            view = activity.getWindow().getDecorView().findViewById(id);
        } else {
            view = activity.getWindow().getDecorView().findViewById(R.id.home);
        }
        if (view == null) throw new IllegalStateException("Could not find ActionBar icon view");

        // need no margins so that clicked state would work nicely
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.bottomMargin = 0;
        params.topMargin = 0;

        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setIcon(drawable);
    }

    @Override
    public MaterialMenuDrawable getDrawable() {
        return drawable;
    }
}
