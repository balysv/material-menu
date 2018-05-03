Material Menu
===============

Morphing Android menu, back, dismiss and check buttons

![Demo Image](https://raw.github.com/balysv/material-menu/master/art/demo.gif)

Have full control of the animation:

![Demo Drawer](https://raw.github.com/balysv/material-menu/master/art/demo_drawer.gif)

Including in your project
-------------------------

```groovy
implementation 'com.balysv.materialmenu:material-menu:2.0.0'
```

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.balysv.materialmenu/material-menu/badge.svg?style=flat)](http://mvnrepository.com/artifact/com.balysv.materialmenu/material-menu)

<a href="http://www.methodscount.com/?lib=com.balysv.materialmenu%3Amaterial-menu%3A%2B"><img src="https://img.shields.io/badge/Methods and size-206 | 23 KB-e91e63.svg"/></a>


#### Versions up to 2.0 (deprecated)

See [README][1] for setting up older versions of the library.

Usage
-----

### MaterialMenuDrawable

Use it as a standalone drawable in your `Toolbar`:

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

### MaterialMenuView

A plain old `View` that draws the icon and provides an API to manipulate its state. You can embed it
in any layout including a `Toolbar`.

Customisation is also available through xml attributes:

```xml
app:mm_color="color"               // Color of drawable
app:mm_visible="boolean"           // Visible
app:mm_transformDuration="integer" // Transformation animation duration
app:mm_scale="integer"             // Scale factor of drawable
app:mm_strokeWidth="integer"       // Stroke width of icons (can only be 1, 2 or 3)
app:mm_rtlEnabled="boolean"        // Enabled RTL layout support (flips all drawables)
app:mm_iconState="enum"            // Set the intial state of the drawable (burger, arrow, x or check)
```

## API

There are four icon states:

```java
BURGER, ARROW, X, CHECK
```

To morph the drawable state

```java
MaterialMenu.animateIconState(IconState state)
```
    
To change the drawable state without animation

```java
MaterialMenu.setIconState(IconState state)
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
    
**Note:** The icon state is resolved by current offset value. Make sure you use `offset` between `0` and `1` for forward animation and `1` and `2` for backwards to correctly save icon state on activity recreation.

## NavigationDrawer slide interaction

Implement `MaterialMenu` into your ActionBar as described above and add a custom `DrawerListener`:

```java
private DrawerLayout drawerLayout;
private boolean isDrawerOpened;
private MaterialMenuDrawable materialMenu;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    materialMenu = new MaterialMenuDrawable(this, Color.WHITE, Stroke.THIN);
    toolbar.setNavigationIcon(materialMenu);

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
                if(isDrawerOpened) {
                   menu.setIconState(MaterialMenuDrawable.IconState.ARROW);
                } else {
                   menu.setIconState(MaterialMenuDrawable.IconState.BURGER);
                }
            }
        }
    });
}
```

Developed By
--------------------
Balys Valentukevicius

License
-----------

```
Copyright 2016 Balys Valentukevicius

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

[1]: https://github.com/balysv/material-menu/blob/master/README-1.0.md
