package com.balysv.materialmenu.extras.toolbar;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.balysv.materialmenu.MaterialMenuBase;
import com.balysv.materialmenu.MaterialMenuDrawable;

public abstract class MaterialMenuIconToolbar extends MaterialMenuBase {

    public MaterialMenuIconToolbar(Activity activity, int color, MaterialMenuDrawable.Stroke stroke) {
        super(activity, color, stroke);
    }

    public MaterialMenuIconToolbar(Activity activity, int color, MaterialMenuDrawable.Stroke stroke, int transformDuration) {
        super(activity, color, stroke, transformDuration);
    }

    @Override
    protected final void setActionBarSettings(Activity activity) {
        Toolbar toolbar = (Toolbar) activity.findViewById(getToolbarViewId());
        toolbar.setNavigationIcon(getDrawable());
    }

    @Override
    protected final View getActionBarHomeView(Activity activity) {
        return null;
    }

    @Override
    protected final View getActionBarUpView(Activity activity) {
        return null;
    }

    @Override
    protected final boolean providesActionBar() {
        return false;
    }

    public abstract int getToolbarViewId();
}
