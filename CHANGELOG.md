Change Log
==========

Version 1.3.1 (2014-10-14)
----------------------------

- UI fix: Remove up arrow indicator margins when using `MaterialMenuIcon` since
you could see some extra space on the left side of the icon.
If a different margin is required, find the 'Home' view of the ActionBar and apply them
(example for stock ActionBar) :

```java
View view = activity.getWindow().getDecorView().findViewById(
    resources.getIdentifier("android:id/home", null, null)
);
ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
params.leftMargin = someMargin;
```

- `MaterialMenuBase` abstraction improvements

Version 1.3.0 (2014-10-12)
----------------------------

- Added new API to allow manual animation to any IconState. For example when sliding a navigation drawer.

Usage:
```java
MaterialMenu.setTransformationOffset(AnimationState state, float value)
```

where `AnimationState` is one of `BURGER_ARROW, BURGER_X, ARROW_X, ARROW_CHECK, BURGER_CHECK, X_CHECK`
and `value` is between `0` and `2`

- Added RTL layout support. When enabled, it flips all icons horizontally.

Usage: Use API `MaterialMenu.setRTLEnabled(boolean enabled)` or set an `xml` attribute `mm_rtlEnabled="boolean"`

- Added new API to get current IconState.

Usage: `MaterialMenu.getIconState()`


Version 1.2.4 (2014-10-07)
----------------------------

- BUG FIX: A crash with `MaterialMenuView` inflation


Version 1.2.3 (2014-10-05)
----------------------------

- `MaterialMenuView` now takes padding into account and position `MaterialMenuDrawable` accordingly

Version 1.2.2 (2014-09-22)
----------------------------

- BUG FIX: Added a prefix to ambiguous xml attributes https://code.google.com/p/android/issues/detail?id=22576

Now:
```xml
app:mm_color="color"             
app:mm_transformDuration="integer" 
app:mm_pressedDuration="integer"   
app:mm_scale="integer"            
app:mm_strokeWidth="integer" 
```

Version 1.2.1 (2014-09-21)
----------------------------

- BUG FIX: Added `ConstantState` to `MaterialMenuDrawable`, so it behaves correctly with Expandable ActionBar items.
The implementation is not *correct* per se cause it doesn't provide a static `ConstantState` but it does the job.

Version 1.2.0 (2014-09-09)
----------------------------

- added new attribute `Stroke`. It adjusts icon paint stroke width for further customisation. Possible values are
`Stroke.REGULAR`, `Stroke.THIN` and `Stroke.EXTRA_THIN`

Usage: Provide `Stroke` value to a constructor or use `xml` attribute `strokeWidth` for `MaterialMenuView`

Version 1.1.3 (2014-09-03)
----------------------------

- improved CHECK to ARROW animation by adding more motion to the transformation

Version 1.1.2 (2014-08-29)
----------------------------

- animatePressedState(), animateState() and setState() now cancels running transformation so you
can spam the button and result in a correct state.

Version 1.1.1 (2014-08-27)
----------------------------

- Changed Maven ArtifactId to `com.balysv.materialmenu` for extras support
- Added support to API 9 and higher using NineOldAndroids
- Added EXTRAS: ActionBarCompat and ActionBarSherlock support

Usage: 

- import `com.balysv.materialmenu:material-menu-abs:1.1.1` for ActionBarSherlock and use `MaterialMenuIconSherlock` 
- import `com.balysv.materialmenu:material-menu-abc:1.1.1` for ActionBarCompat and use `MaterialMenuIconCompat`. 

Version 1.1.0 (2014-08-26)
----------------------------

- Added new morphing icon - CHECK

Usage: Use standard API with `IconState.CHECK`

Version 1.0.0 (2014-07-11)
----------------------------

Initial release.