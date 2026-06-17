HANDOFF CONTEXT
===============

USER REQUESTS (AS-IS)
---------------------
- "A flashlight app. Control system torch brightness with volume buttons (if available). Two quick "chop" gestures to turn it on. Tap the icon to launch a screen torch (a white screen on full brightness)."

GOAL
----
Implement a flashlight app in this existing Android scaffold: system torch with brightness slider controlled by volume buttons, double-chop gesture to toggle torch, and a screen-torch mode (white screen at max brightness).

WORK COMPLETED
--------------
- I read every source file in the project to understand the scaffold structure
- I researched Android APIs on GitHub for: CameraManager.turnOnTorchWithStrengthLevel, FLASH_INFO_STRENGTH_MAXIMUM_LEVEL, LINEAR_ACCELERATION sensor for chop detection, screen brightness control via window.attributes.screenBrightness
- I produced and presented a full architecture plan (below) which the user approved ("Does this plan look right?" -- user did not object, only asked to save it to file before restarting)
- No code has been written yet

CURRENT STATE
-------------
- Codebase is the empty-activity scaffold on branch "flashlight"
- No flashlight code exists yet
- Some minor uncommitted changes exist in .idea/kotlinc.xml, DataRepository.kt, Color.kt, Theme.kt, MainScreenViewModel.kt (from previous work unrelated to flashlight)
- Build has not been attempted this session
- minSdk is 24, compileSdk is 37, Kotlin 2.3.20, AGP 9.2.1

PENDING TASKS
-------------
All tasks are pending -- no implementation has started:
1. Add CAMERA permission and camera.flash uses-feature to AndroidManifest.xml
2. Create torch/TorchController.kt (CameraManager wrapper)
3. Create gesture/ChopDetector.kt (sensor-based double-chop detection)
4. Create ui/flashlight/FlashlightScreen.kt (main UI with toggle + brightness slider)
5. Create ui/flashlight/FlashlightViewModel.kt (owns TorchController, handles volume key events)
6. Create ui/screentorch/ScreenTorchScreen.kt (white fullscreen, max brightness)
7. Add ScreenTorch nav key to NavigationKeys.kt
8. Wire both screens into Navigation.kt
9. Update MainActivity.kt (volume key interception, chop detector lifecycle)
10. Build and verify

KEY FILES
---------
- app/src/main/AndroidManifest.xml - needs CAMERA permission added
- app/src/main/java/com/example/empty_activity/MainActivity.kt - needs volume key + chop detector wiring
- app/src/main/java/com/example/empty_activity/Navigation.kt - Nav3 NavDisplay, needs new screen entries
- app/src/main/java/com/example/empty_activity/NavigationKeys.kt - needs ScreenTorch key added
- app/src/main/java/com/example/empty_activity/ui/main/MainScreen.kt - will be replaced by FlashlightScreen
- app/src/main/java/com/example/empty_activity/ui/main/MainScreenViewModel.kt - will be replaced by FlashlightViewModel
- app/build.gradle.kts - reference for deps/config (no changes expected)
- gradle/libs.versions.toml - reference for versions (no changes expected)

IMPORTANT DECISIONS
-------------------
- Two screens via Nav3: Main (flashlight controls) and ScreenTorch (white fullscreen)
- TorchController wraps CameraManager, exposes StateFlow<Boolean> for on/off and StateFlow<Int> for brightness level
- Brightness control uses turnOnTorchWithStrengthLevel (API 33+), falls back to simple setTorchMode on older devices
- Brightness slider only shown when device reports FLASH_INFO_STRENGTH_MAXIMUM_LEVEL > 1
- Volume keys only consumed when torch is ON and device supports brightness levels; otherwise passed through to system
- ChopDetector uses TYPE_LINEAR_ACCELERATION sensor (gravity-free), detects two Z-axis spikes > 15 m/s^2 within 500ms, with 1-second cooldown
- No foreground service for v1 -- chop detection is Activity-scoped only
- Old MainScreen/MainScreenViewModel become dead code; will delete and create fresh files under ui/flashlight/
- DataRepository left untouched (scaffold artifact)
- Screen torch sets window.attributes.screenBrightness = 1.0f + FLAG_KEEP_SCREEN_ON, restores BRIGHTNESS_OVERRIDE_NONE on exit via DisposableEffect
- No DI -- ViewModel constructed via viewModel { FlashlightViewModel(...) } lambda factory, matching existing pattern

EXPLICIT CONSTRAINTS
--------------------
- From AGENTS.md: Do not use NavHost/composable(route="...") -- use Nav3 NavDisplay + entryProvider
- From AGENTS.md: Do not add Hilt/Koin without explicit ask -- no DI configured
- From AGENTS.md: Do not hardcode versions in app/build.gradle.kts -- use libs.versions.toml catalog
- From AGENTS.md: Do not enable buildConfig just to inject a constant
- From AGENTS.md: Match indent style of the file being edited (2-space in MainScreen/ViewModel/DataRepository/build.gradle; 4-space+Allman in MainActivity/Navigation/Theme)
- From AGENTS.md: RepositoriesMode.FAIL_ON_PROJECT_REPOS -- never add repositories {} in app/build.gradle.kts
- From AGENTS.md: android-starter/ is a nested git repo, not a module -- ignore it

CONTEXT FOR CONTINUATION
------------------------
- The full architecture plan is in this file above under PENDING TASKS and IMPORTANT DECISIONS
- Key API patterns found from GitHub research:
  - CameraManager.getCameraCharacteristics(cameraId).get(CameraCharacteristics.FLASH_INFO_STRENGTH_MAXIMUM_LEVEL) returns Int? -- null or 1 means no brightness control
  - cameraManager.turnOnTorchWithStrengthLevel(cameraId, level) requires API 33+ (Build.VERSION_CODES.TIRAMISU)
  - cameraManager.setTorchMode(cameraId, true/false) works from API 23+
  - Screen brightness: window.attributes = window.attributes.apply { screenBrightness = 1.0f }; window.addFlags(FLAG_KEEP_SCREEN_ON)
  - Chop detection: register SensorEventListener for TYPE_LINEAR_ACCELERATION, check abs(values[2]) for Z-axis spikes
  - Volume keys: override onKeyDown(keyCode, event) in Activity, check KEYCODE_VOLUME_UP/DOWN, return true to consume
- The existing Theme.kt has an inverted color scheme bug (documented in AGENTS.md) -- do not fix it as part of this work
- MainScreenViewModelTest.kt does not compile against current DataRepository interface -- known issue, not blocking
- The project has no CI, no lint config, no detekt/ktlint
