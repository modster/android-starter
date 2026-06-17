# PROJECT KNOWLEDGE BASE

**Generated:** 2026-06-17
**Commit:** 191deee — "update data emission in DefaultDataRepository and increment library versions"
**Branch:** `flashlight`

## OVERVIEW
Single-module Android app scaffold (`:app`). Jetpack Compose + Material3 + **Navigation 3** (`androidx.navigation3`, *preview*) on Kotlin 2.3.20 / AGP 9.2.1 / JDK 17. Repo folder is `photon` but `rootProject.name = "empty-activity"` and package is `com.example.empty_activity` — both are placeholder values from the starter template, not yet renamed.

## STRUCTURE
```
photon/
├── app/                                 # ONLY Gradle module (settings.gradle.kts includes :app only)
│   └── src/main/java/com/example/empty_activity/
│       ├── MainActivity.kt              # Single Activity — sets Compose content, calls MainNavigation()
│       ├── Navigation.kt                # Nav3 NavDisplay + entryProvider (NOT NavHost from navigation-compose)
│       ├── NavigationKeys.kt            # @Serializable data object Main : NavKey
│       ├── data/DataRepository.kt       # Repo interface + DefaultDataRepository + DataResult<T>(status,data,error)
│       ├── theme/                       # EmptyActivityTheme, Color, Type — Material3 dynamic color
│       └── ui/main/                     # MainScreen (Composable) + MainScreenViewModel + sealed UiState
├── android-starter/                     # NESTED git repo (template ref). NOT a Gradle module. Ignore.
├── gradle/libs.versions.toml            # Single source of truth for versions
├── build.gradle.kts                     # Root — declares plugins `apply false` only
├── app/build.gradle.kts                 # All Android config + deps (no Hilt/Koin/Room/Retrofit)
└── settings.gradle.kts                  # rootProject.name = "empty-activity"
```

## WHERE TO LOOK
| Task | Location |
|------|----------|
| Add a screen | `ui/<feature>/` + register entry in `Navigation.kt` + add `@Serializable data object/class` to `NavigationKeys.kt` |
| Add a dependency | `gradle/libs.versions.toml` first, then reference via `libs.*` in `app/build.gradle.kts` |
| Change theme colors | `theme/Color.kt` (palette) + `theme/Theme.kt` (color schemes) |
| Add data source | `data/` next to `DataRepository.kt` — instantiate manually in `viewModel { ... }` factory (no DI configured) |
| Bump SDK / Kotlin / AGP | `app/build.gradle.kts` for SDK; `libs.versions.toml` for everything else |
| Add unit test | `app/src/test/java/...` — JUnit4 + `kotlinx-coroutines-test` (`runTest`) |
| Add UI test | `app/src/androidTest/java/...` — `createAndroidComposeRule<ComponentActivity>()` |

## STACK SPECIFICS
- **Navigation**: `androidx.navigation3` (`NavDisplay`, `rememberNavBackStack`, `entryProvider { entry<T> {...} }`). Nav keys are `@Serializable` Kotlin objects implementing `NavKey`. The legacy `androidx.navigation:navigation-compose` is *also* declared as a dep but **not used** — do not introduce `NavHost`/`composable(route="...")` patterns.
- **DI**: None. ViewModels constructed via `viewModel { MainScreenViewModel(DefaultDataRepository()) }` lambda factory. Do not add Hilt/Koin without discussion.
- **State**: ViewModel exposes `StateFlow<UiState>` via `stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)`. Sealed `MainScreenUiState { Loading; Success(data); Error(throwable) }`. Collected with `collectAsStateWithLifecycle()`.
- **Repository contract**: `Flow<DataResult<T>>` where `DataResult(status: DataStatus, data: T?, error: Throwable?)`, `DataStatus { LOADING, SUCCESS, ERROR }`. Repository emits all three states itself; ViewModel maps to UiState.
- **Build features**: `compose = true`; `aidl = false`, `buildConfig = false`, `shaders = false` — keep them off unless required.
- **JVM**: `jvmToolchain(17)`, `sourceCompatibility = VERSION_17`. Do not lower.
- **Repos**: `RepositoriesMode.FAIL_ON_PROJECT_REPOS` is set — never add `repositories {}` in `app/build.gradle.kts`; add to `settings.gradle.kts` only.
- **Gradle**: configuration-cache **enabled** (`org.gradle.configuration-cache=true`) — any `Task` you add must be configuration-cache compatible.

## CONVENTIONS
- `kotlin.code.style=official` declared, but actual files mix **2-space indent** (`MainScreen.kt`, `MainScreenViewModel.kt`, `DataRepository.kt`, `app/build.gradle.kts`) with **4-space indent + Allman braces** (`MainActivity.kt`, `Navigation.kt`, `Theme.kt`). When editing, **match the style of the file you are in** — do not reformat existing files.
- Compose previews live next to the Composable they preview (e.g. `MainScreenPreview` in `MainScreen.kt`).
- Test fakes are `private class` declared in the test file (see `FakeMyModelRepository` in `MainScreenViewModelTest.kt`).
- `android.nonTransitiveRClass=true` — reference `R` from the owning module only.

## KNOWN BUGS / GOTCHAS (don't "fix" without context)
- **`theme/Theme.kt` colorScheme branch is inverted** (lines 40–47): in dynamic-color mode `darkTheme` returns `dynamicLightColorScheme`, and the static branches assign `LightColorScheme` to `darkTheme=true` and `DarkColorScheme` to `darkTheme=false`. Likely a real bug, but ship-as-is until confirmed.
- **`MainScreenViewModelTest.kt` does not compile against the current `DataRepository` interface**: `FakeMyModelRepository.data` is typed `Flow<List<String>>`, but the interface now requires `Flow<DataResult<List<String>>>` (changed in commit 191deee). Update the fake before relying on `./gradlew test`.
- `rootProject.name = "empty-activity"`, package `com.example.empty_activity`, theme `EmptyActivityTheme`, manifest `@string/app_name = "EmptyActivity"` (likely) — these are **template placeholders**. The repo/branch (`photon`/`flashlight`) suggests a rename is pending. Do not rename piecemeal; do it as one atomic change.
- `app/build.gradle.kts` line 40: `compileSdkMinor = 0` — preview AGP 9.x property. Keep it.
- `android-starter/` is a separate nested git repository (its own `.git/`) used as a reference template. It is **not** in `settings.gradle.kts` and must not be added as a module.

## ANTI-PATTERNS (this project)
- Don't add `NavHost { composable(route="...") }` — this codebase uses Nav3 `NavDisplay` instead.
- Don't hardcode versions in `app/build.gradle.kts` — use the `libs.versions.toml` catalog.
- Don't introduce `runBlocking` in production code; tests use `runTest` from `kotlinx-coroutines-test`.
- Don't add `@HiltAndroidApp` / Hilt / Koin without an explicit ask — there is currently no Application subclass and no DI graph.
- Don't enable `buildConfig` just to inject a constant; use Kotlin `const val` in source.

## COMMANDS

This project uses `uv` for python v3.14 tasks.

### Gradle

```bash
./gradlew assembleDebug          # build APK
./gradlew test                   # JVM unit tests (app/src/test) — currently broken, see gotchas
./gradlew connectedDebugAndroidTest  # instrumented UI tests (app/src/androidTest), needs device/emulator
./gradlew lint                   # Android Lint
./gradlew :app:dependencies      # inspect resolved deps
```

### Graphify: `uv graphify` generates a static call graph of the codebase. Output is in `graphify-out/` and can be viewed with `index.html` in that folder. The graph includes calls from Android framework entry points (e.g. `Activity.onCreate`) to app code, but does not include external library code (e.g. Compose runtime) or calls between library code.
To update the graph, run:
```bash
uv graphify update graphify-out
```

No CI workflows, no Makefile, no pre-commit hooks, no detekt/ktlint configured.

## NOTES
- No `proguard-rules.pro` file exists; release `isMinifyEnabled = false`. R8 is effectively off.
- Permissions in `AndroidManifest.xml`: **none**. Branch name `flashlight` suggests CAMERA / FLASHLIGHT permissions and a `CameraManager`-based torch implementation are likely incoming work.
- Only `:app` is included in `settings.gradle.kts`. There is no `buildSrc/` and no convention plugins.
