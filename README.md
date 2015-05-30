Material Menu
===============

Morphing Android menu, back, dismiss and check buttons

![Demo Image](https://raw.github.com/balysv/material-menu/master/art/demo.gif)

Have full control of the animation:

![Demo Drawer](https://raw.github.com/balysv/material-menu/master/art/demo_drawer.gif)

Including in your project
-------------------------


```groovy
// stock actionBar
compile 'com.balysv.materialmenu:material-menu:1.x.x'

// Toolbar and ActionBarCompat-v22 (includes support-v7:22.0.x)
compile 'com.balysv.materialmenu:material-menu-toolbar:1.x.x'

// actionBarCompat-v20 (up to support-v7:20.0.0 - does not support Toolbar)
compile 'com.balysv.materialmenu:material-menu-abc:1.x.x'

// actionBarSherlock
compile 'com.balysv.materialmenu:material-menu-abs:1.x.x'
```

Check for latest version number on the widget below or visit [Releases](https://github.com/balysv/material-menu/releases)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.balysv.materialmenu/material-menu/badge.svg?style=flat)](http://mvnrepository.com/artifact/com.balysv.materialmenu/material-menu)

Usage
-----

The library provides two wrappers of `MaterialMenuDrawable` that eases implementation into the ActionBar, NavigationDrawer slide interaction or into any other custom layout.

### MaterialMenuView

A plain old `View` that draws the icon and provides an API to manipulate its state.

Customisation is also available through attributes:

```xml
app:mm_color="color"               // Color of drawable
app:mm_visible="boolean"        	  // Visible
app:mm_transformDuration="integer" // Transformation animation duration
app:mm_scale="integer"             // Scale factor of drawable
app:mm_strokeWidth="integer"       // Stroke width of icons (can only be 1, 2 or 3)
app:mm_rtlEnabled="boolean"        // Enabled RTL layout support (flips all drawables)
```

### MaterialMenuIcon

A POJO that initializes the drawable and replaces the ActionBar icon.

Jump to instructions for :
- [ActionBar (stock, Sherlock, Compat)]( https://github.com/balysv/material-menu#use-as-action-bar-icon-stock-compat-or-sherlock)
- [Toolbar](https://github.com/balysv/material-menu#use-in-toolbar)
- [NavigationDrawer interaction ](https://github.com/balysv/material-menu#navigationdrawer-slide-interaction)

## API

There are four icon states:

```java
BURGER, ARROW, X, CHECK
```

To morph the drawable state

```java
MaterialMenu.animateState(IconState state)
```
    
To change the drawable state without animation

```java
MaterialMenu.setState(IconState state)
```

To animate the drawable manually (i.e. on navigation drawer slide):

```java
MaterialMenu.setTransformationOffset(AnimationState state, float value)
```
To hide or show the drawable:

```java
MaterialMenu.setVisible(boolean visible)
```

where `AnimationState` is one of `BURGER_ARROW, BURGER_X, ARROW_X, ARROW_CHECK, BURGER_CHECK, X_CHECK`
and `value` is between `0` and `2`
    
**Note:** The current implementation resolves its state by current offset value. Make sure you use `offset` between `0` and `1` for forward animation and `1` and `2` for backwards to correctly save icon state on activity recreation.
    
### Customisation

```java
// change color
MaterialMenu.setColor(int color)

// change icon visibility
MaterialMenu.setVisible(boolean visible)

// change transformation animation duration
MaterialMenu.setTransformationDuration(int duration)

// change transformation interpolator
MaterialMenu.setInterpolator(Interpolator interpolator)

// set RTL layout support
MaterialMenu.setRTLEnabled(boolean enabled)
```
    
### Action Bar

#### Use as Action Bar icon (stock, Compat or Sherlock)

Depending on the `ActionBar` you use, use one of the following classes:
`MaterialMenuIcon`, `MaterialMenuIconCompat` or `MaterialMenuIconSherlock`.

In your `Activity` add the following:

```java
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    materialMenu = new MaterialMenuIcon(this, Color.WHITE, Stroke.THIN);
}

protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    materialMenu.syncState(savedInstanceState);
}

protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    materialMenu.onSaveInstanceState(outState);
}

public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getId() == android.R.id.home) {
        // Handle your drawable state here
        materialMenu.animateState(newState);
    }
}
```

#### Use in Toolbar

Use it as a standalone drawable. Note: you have to handle icon state yourself:

```java
private MaterialMenuDrawable materialMenu;

protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.toolbar);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
        // Handle your drawable state here
        materialMenu.animateState(newState);
        }
    });
    materialMenu = new MaterialMenuDrawable(this, Color.WHITE, Stroke.THIN);
    toolbar.setNavigationIcon(materialMenu);
}
```

OR

Use `MaterialMenuIconToolbar` which handles saved state:

```java
private MaterialMenuIconToolbar materialMenu;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.toolbar);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
        // Handle your drawable state here
        materialMenu.animateState(newState);
        }
    });
    materialMenu = new MaterialMenuIconToolbar(this, Color.WHITE, Stroke.THIN) {
        @Override public int getToolbarViewId() {
            return R.id.toolbar;
        }
    };
}

@Override
protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    materialMenu.syncState(savedInstanceState);
}

@Override protected void onSaveInstanceState(Bundle outState) {
    materialMenu.onSaveInstanceState(outState);
    super.onSaveInstanceState(outState);
}
```

#### Use in custom Action Bar view

Simply add `MaterialMenuView` in your custom layout and register an `OnClickListener` to do the
transformations. 

See [source of Demo][2] for details

#### NavigationDrawer slide interaction

Implement `MaterialMenu` into your ActionBar as described above and add a custom `DrawerListener`:

```java
private DrawerLayout drawerLayout;
private boolean      isDrawerOpened;
private MaterialMenuIcon materialMenu;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    materialMenu = new MaterialMenuIcon(this, Color.WHITE, Stroke.THIN); // or retrieve from your custom view, etc
    drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            materialMenu.setTransformationOffset(
                MaterialMenuDrawable.AnimationState.BURGER_ARROW,
                isDrawerOpened ? 2 - slideOffset : slideOffset
            );
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            isDrawerOpened = true;
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            isDrawerOpened = false;
        }
        
        @Override
        public void onDrawerStateChanged(int newState) {
            if(newState == DrawerLayout.STATE_IDLE) {
                if(isDrawerOpened) menu.setState(MaterialMenuDrawable.IconState.ARROW)
                else menu.setState(MaterialMenuDrawable.IconState.BURGER)
            }
        }
    });
}


@Override
protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    isDrawerOpened = drawerLayout.isDrawerOpen(Gravity.START); // or END, LEFT, RIGHT
    materialMenu.syncState(savedInstanceState);
}

@Override
protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    materialMenu.onSaveInstanceState(outState);
}
```

Developed By
--------------------
Balys Valentukevicius @ [Lemon Labs][1]

License
-----------

```
Copyright 2014 Balys Valentukevicius

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[1]: http://www.lemonlabs.co
[2]: https://github.com/balysv/material-menu/blob/master/demo/src/stock/java/com/balysv/materialmenu/demo/stock/CustomViewActivity.java
[3]: http://gradleplease.appspot.com/
