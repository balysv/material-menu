Material Menu
===============

Morphing Android menu, back, dismiss and check buttons

![Demo Image][1]

Including in your project
-------------------------

Check for latest versions at [Gradle Please][4] and import depending on ActionBar you use

```groovy
// stock actionBar
compile 'com.balysv.materialmenu:material-menu:1.2.0'

// actionBarCompat
compile 'com.balysv.materialmenu:material-menu-abc:1.2.0'

// actionBarSherlock
compile 'com.balysv.materialmenu:material-menu-abs:1.2.0'
```


Usage
-----

Generally, you should use `MaterialMenuDrawable` as a standalone drawable in your custom views, etc.

The library provides two wrappers that might ease implementation:

### MaterialMenuView

A plain old `View` that draws the icon and provides an API to manipulate its state.

Customisation is also available through attributes:

```xml
app:color="color"               // Color of drawable
app:transformDuration="integer" // Transformation animation duration
app:pressedDuration="integer"   // Pressed circle animation duration
app:scale="integer"             // Scale factor of drawable
app:strokeWidth="integer"       // Stroke width of icons (can only be 1, 2 or 3)
```

### MaterialMenuIcon

A POJO that initializes the drawable and replaces the ActionBar icon. See usage in **ActionBar** below

## API

There are four icon states:

```java
BURGER, ARROW, X, CHECK
```

To morph the drawable state with a pressed circle animation

```java
MaterialMenu.animatePressedState(IconState state)
```
    
To morph the drawable state without a pressed circle animation

```java
MaterialMenu.animateState(IconState state)
```
    
To change the drawable state without animation

```java
MaterialMenu.setState(IconState state)
```
    
Customisation

```java
// change color
MaterialMenu.setColor(int color)

// change transformation animation duration
MaterialMenu.setTransformationDuration(int duration)

// change pressed animation duration
MaterialMenu.setPressedDuration(int duration)

// change transformation interpolator
MaterialMenu.setInterpolator(Interpolator interpolator)
```
    
### Action Bar

#### Use as Action Bar icon (stock, Compat or Sherlock)

In your `Activity` add the following:

```java
protected void onCreate(Bundle savedInstanceState) {
    materialMenu = new MaterialMenuIcon(this, Color.WHITE);
}

protected void onPostCreate(Bundle savedInstanceState) {
    materialMenu.syncState(savedInstanceState);
}

protected void onSaveInstanceState(Bundle outState) {
    materialMenu.onSaveInstanceState(outState);
}

public boolean onOptionsItemSelected(MenuItem item) {
    if (id == android.R.id.home) {
        // Handle your drawable state here
        materialMenu.animatePressedState(newState);
    }
}
```
    
In order to use the new *Material* circle pressed state, you have to disable ActionBar item backgrounds in your theme and 
re-enable it for other menu icons

```xml
<style name="AppTheme" parent="android:Theme.Holo.Light.DarkActionBar">
    <item name="android:actionBarItemBackground">@null</item>
    <item name="android:actionButtonStyle">@style/ActionButtonStyle</item>
    <item name="android:actionOverflowButtonStyle">@style/OverflowButtonStyle</item>
</style>

<style name="ActionButtonStyle" parent="android:Widget.Holo.ActionButton">
    <item name="android:background">@drawable/action_bar_item_background</item>
</style>

<style name="OverflowButtonStyle" parent="android:Widget.Holo.ActionButton.Overflow">
    <item name="android:background">@drawable/action_bar_item_background</item>
</style>
```

Otherwise, to disable circle pressed state use

```java
MaterialMenu.setNeverDrawTouch(true)
```

#### Use in custom Action Bar view

Simply add `MaterialMenuView` in your custom layout and register an `OnClickListener` to do the
transformations. 

See [source of Demo][3] for details

Developed By
--------------------
Balys Valentukevicius - <balys.v@gmail.com> @ [Lemon Labs][2]

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
    
[1]: https://raw.github.com/balysv/material-menu/master/art/demo.gif
[2]: http://www.lemonlabs.co
[3]: https://github.com/balysv/material-menu/blob/master/demo/src/stock/java/com/balysv/materialmenu/demo/stock/CustomViewActivity.java
[4]: http://gradleplease.appspot.com/
