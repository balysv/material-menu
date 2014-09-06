Change Log
==========

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