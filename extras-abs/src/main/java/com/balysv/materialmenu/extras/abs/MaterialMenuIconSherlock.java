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

package com.balysv.materialmenu.extras.abs;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.balysv.materialmenu.MaterialMenuBase;

import static com.balysv.materialmenu.MaterialMenuDrawable.Stroke;

/**
 * A helper class for implementing {@link com.balysv.materialmenu.MaterialMenuDrawable}
 * as an {@link com.actionbarsherlock.app.ActionBar} Sherlock icon.
 * <p/>
 * In order to preserve default ActionBar icon click state call {@link com.balysv.materialmenu.MaterialMenuBase#setNeverDrawTouch(boolean)}.
 * Otherwise, adjust your theme to disable pressed background color by setting <code>android:actionBarItemBackground</code>
 * to null and use <code>android:actionButtonStyle</code>, <code>android:actionOverflowButtonStyle</code> to enable other
 * menu icon backgrounds.
 * <p/>
 * Disables ActionBar Up arrow and replaces default drawable using {@link ActionBar#setIcon(android.graphics.drawable.Drawable)}
 */
public class MaterialMenuIconSherlock extends MaterialMenuBase {

    public MaterialMenuIconSherlock(SherlockActivity activity, int color, Stroke stroke) {
        super(activity, color, stroke);
    }

    public MaterialMenuIconSherlock(SherlockActivity activity, int color, Stroke stroke, int transformDuration) {
        super(activity, color, stroke, transformDuration);
    }

    public MaterialMenuIconSherlock(SherlockActivity activity, int color, Stroke stroke, int transformDuration, int pressedDuration) {
        super(activity, color, stroke, transformDuration, pressedDuration);
    }

    @Override
    protected View getActionBarHomeView(Activity activity) {
        Resources resources = activity.getResources();
        return activity.getWindow().getDecorView().findViewById(
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
                ? resources.getIdentifier("android:id/home", null, null)
                : R.id.abs__home
        );
    }

    @Override
    protected View getActionBarUpView(Activity activity) {
        Resources resources = activity.getResources();
        View decorView = activity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ViewGroup actionBarView = (ViewGroup) activity.getWindow().getDecorView().findViewById(
                resources.getIdentifier("android:id/action_bar", null, null)
            );
            View homeView = actionBarView.getChildAt(
                actionBarView.getChildCount() > 1 ? 1 : 0
            );
            return homeView.findViewById(
                resources.getIdentifier("android:id/up", null, null)
            );
        } else {
            // there are duplicate up ids; extract it from non expanded action bar view
            ViewGroup actionBarView = (ViewGroup) decorView.findViewById(R.id.abs__action_bar);
            View homeView = actionBarView.getChildAt(1);
            return homeView.findViewById(R.id.abs__up);
        }
    }

    @Override
    protected boolean providesActionBar() {
        return false;
    }

    @Override
    protected void setActionBarSettings(Activity activity) {
        ActionBar actionBar = ((SherlockActivity) activity).getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setIcon(getDrawable());
    }
}
