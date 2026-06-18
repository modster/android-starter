# Graph Report - C:\Users\User\AndroidStudioProjects\me\photon  (2026-06-17)

## Corpus Check
- 29 files · ~5,981 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 106 nodes · 122 edges · 15 communities
- Extraction: 94% EXTRACTED · 6% INFERRED · 0% AMBIGUOUS · INFERRED: 7 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- [[_COMMUNITY_Flashlight State Flow|Flashlight State Flow]]
- [[_COMMUNITY_Torch Controller State|Torch Controller State]]
- [[_COMMUNITY_Repo Instructions|Repo Instructions]]
- [[_COMMUNITY_Flashlight Activity Wiring|Flashlight Activity Wiring]]
- [[_COMMUNITY_Flashlight Screen UI|Flashlight Screen UI]]
- [[_COMMUNITY_Chop Detection Logic|Chop Detection Logic]]
- [[_COMMUNITY_Navigation and Screen Torch|Navigation and Screen Torch]]
- [[_COMMUNITY_Repository Contract|Repository Contract]]
- [[_COMMUNITY_Navigation Keys|Navigation Keys]]

## God Nodes (most connected - your core abstractions)
1. `TorchController` - 12 edges
2. `FlashlightViewModel` - 11 edges
3. `PROJECT KNOWLEDGE BASE` - 10 edges
4. `MainActivity` - 8 edges
5. `FlashlightScreen()` - 7 edges
6. `DataRepository` - 6 edges
7. `DefaultDataRepository` - 6 edges
8. `ChopDetector` - 6 edges
9. `MainNavigation()` - 5 edges
10. `EmptyActivityTheme()` - 5 edges

## Surprising Connections (you probably didn't know these)
- `MainNavigation()` --calls--> `FlashlightScreen()`  [INFERRED]
  app/src/main/java/com/example/empty_activity/Navigation.kt → app/src/main/java/com/example/empty_activity/ui/flashlight/FlashlightScreen.kt
- `MainNavigation()` --calls--> `ScreenTorchScreen()`  [INFERRED]
  app/src/main/java/com/example/empty_activity/Navigation.kt → app/src/main/java/com/example/empty_activity/ui/screentorch/ScreenTorchScreen.kt
- `FlashlightScreenPreview()` --calls--> `EmptyActivityTheme()`  [INFERRED]
  app/src/main/java/com/example/empty_activity/ui/flashlight/FlashlightScreen.kt → app/src/main/java/com/example/empty_activity/theme/Theme.kt

## Import Cycles
- None detected.

## Communities (15 total, 0 thin omitted)

### Community 0 - "Flashlight State Flow"
Cohesion: 0.17
Nodes (8): Boolean, FlashlightUiState, Int, StateFlow, String, FlashlightUiState, FlashlightViewModel, ViewModel

### Community 1 - "Torch Controller State"
Cohesion: 0.25
Nodes (5): Boolean, Int, StateFlow, String, TorchController

### Community 2 - "Repo Instructions"
Cohesion: 0.15
Nodes (12): ANTI-PATTERNS (this project), COMMANDS, CONVENTIONS, Gradle, Graphify: `uv graphify` generates a static call graph of the codebase. Output is in `graphify-out/` and can be viewed with `index.html` in that folder. The graph includes calls from Android framework entry points (e.g. `Activity.onCreate`) to app code, but does not include external library code (e.g. Compose runtime) or calls between library code., KNOWN BUGS / GOTCHAS (don't "fix" without context), NOTES, OVERVIEW (+4 more)

### Community 3 - "Flashlight Activity Wiring"
Cohesion: 0.17
Nodes (8): Boolean, FlashlightViewModel, Int, Bundle, ChopDetector, ComponentActivity, MainActivity, KeyEvent

### Community 4 - "Flashlight Screen UI"
Cohesion: 0.19
Nodes (8): Boolean, FlashlightUiState, FlashlightViewModel, Modifier, FlashlightScreen(), FlashlightScreenPreview(), FlashlightScreenTest, EmptyActivityTheme()

### Community 5 - "Chop Detection Logic"
Cohesion: 0.20
Nodes (5): Int, ChopDetector, Sensor, SensorEvent, SensorEventListener

### Community 6 - "Navigation and Screen Torch"
Cohesion: 0.28
Nodes (7): Activity, FlashlightViewModel, Modifier, MainNavigation(), findActivity(), ScreenTorchScreen(), ScreenTorchScreenPreview()

### Community 7 - "Repository Contract"
Cohesion: 0.46
Nodes (7): String, DataRepository, DataResult, DataStatus, DefaultDataRepository, Flow, List

### Community 8 - "Navigation Keys"
Cohesion: 0.67
Nodes (3): Main, ScreenTorch, NavKey

## Knowledge Gaps
- **30 isolated node(s):** `FlashlightViewModel`, `Bundle`, `Int`, `KeyEvent`, `Boolean` (+25 more)
  These have ≤1 connection - possible missing edges or undocumented components.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `MainNavigation()` connect `Navigation and Screen Torch` to `Flashlight Activity Wiring`, `Flashlight Screen UI`?**
  _High betweenness centrality (0.050) - this node is a cross-community bridge._
- **Why does `FlashlightScreen()` connect `Flashlight Screen UI` to `Navigation and Screen Torch`?**
  _High betweenness centrality (0.028) - this node is a cross-community bridge._
- **Are the 2 inferred relationships involving `FlashlightScreen()` (e.g. with `MainNavigation()` and `.setup()`) actually correct?**
  _`FlashlightScreen()` has 2 INFERRED edges - model-reasoned connections that need verification._
- **What connects `FlashlightViewModel`, `Bundle`, `Int` to the rest of the system?**
  _30 weakly-connected nodes found - possible documentation gaps or missing edges._