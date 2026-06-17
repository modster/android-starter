# Graph Report - C:\Users\User\AndroidStudioProjects\me\photon  (2026-06-17)

## Corpus Check
- Corpus is ~4,550 words - fits in a single context window. You may not need a graph.

## Summary
- 84 nodes · 110 edges · 14 communities (13 shown, 1 thin omitted)
- Extraction: 90% EXTRACTED · 10% INFERRED · 0% AMBIGUOUS · INFERRED: 11 edges (avg confidence: 0.82)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- [[_COMMUNITY_Compose UI Shell|Compose UI Shell]]
- [[_COMMUNITY_Flashlight Feature Plan|Flashlight Feature Plan]]
- [[_COMMUNITY_Repository Contract|Repository Contract]]
- [[_COMMUNITY_App Theme Navigation|App Theme Navigation]]
- [[_COMMUNITY_ViewModel Test Suite|ViewModel Test Suite]]
- [[_COMMUNITY_Main Screen Test|Main Screen Test]]
- [[_COMMUNITY_ViewModel State Flow|ViewModel State Flow]]
- [[_COMMUNITY_Launcher Icon Assets|Launcher Icon Assets]]
- [[_COMMUNITY_Navigation Keys|Navigation Keys]]

## God Nodes (most connected - your core abstractions)
1. `DefaultDataRepository` - 11 edges
2. `MainScreen()` - 10 edges
3. `FakeMyModelRepository` - 10 edges
4. `DataRepository` - 9 edges
5. `Flashlight Feature` - 9 edges
6. `MainScreenViewModel` - 8 edges
7. `MainScreen` - 8 edges
8. `MainActivity` - 5 edges
9. `EmptyActivityTheme()` - 5 edges
10. `MainScreenViewModelTest` - 5 edges

## Surprising Connections (you probably didn't know these)
- `MainScreenViewModel` --rationale_for--> `FlashlightViewModel (planned)`  [INFERRED]
  app/src/main/java/com/example/empty_activity/ui/main/MainScreen.kt → HANDOFF.md
- `MainScreenViewModelTest Compilation Issue` --affects--> `FakeMyModelRepository`  [EXTRACTED]
  AGENTS.md → app/src/test/java/com/example/empty_activity/ui/main/MainScreenViewModelTest.kt
- `MainScreenViewModelTest Compilation Issue` --caused_by--> `DataRepository`  [EXTRACTED]
  AGENTS.md → app/src/main/java/com/example/empty_activity/data/DataRepository.kt
- `Inverted ColorScheme Bug` --affects--> `EmptyActivityTheme`  [EXTRACTED]
  AGENTS.md → app/src/main/java/com/example/empty_activity/theme/Theme.kt
- `FlashlightScreen (planned)` --will_replace--> `MainScreen`  [EXTRACTED]
  HANDOFF.md → app/src/main/java/com/example/empty_activity/ui/main/MainScreen.kt

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **MVVM Architecture Flow** — mainscreen_composable, mainscreenviewmodel, datarepository_interface, defaultdatarepository [EXTRACTED 0.95]
- **Data Result Flow Pattern** — dataresult, datastatus, datarepository_interface, mainscreenuistate [EXTRACTED 0.90]
- **Flashlight Implementation Components** — torch_controller_planned, chop_detector_planned, flashlightscreen_planned, flashlightviewmodel_planned, screentorchscreen_planned [EXTRACTED 0.95]
- **Multi-Density Launcher Icon Set** — ic_launcher_android_icon, android_adaptive_icons, density_independent_resources [EXTRACTED 1.00]

## Communities (14 total, 1 thin omitted)

### Community 0 - "Compose UI Shell"
Cohesion: 0.20
Nodes (11): List, String, Boolean, Bundle, MainNavigation(), Greeting(), MainScreen(), MainScreenPortraitPreview() (+3 more)

### Community 1 - "Flashlight Feature Plan"
Cohesion: 0.20
Nodes (12): CAMERA Permission Requirement, ChopDetector (planned), Double-Chop Gesture, Flashlight Feature, FlashlightScreen (planned), FlashlightViewModel (planned), Linear Acceleration Sensor, Screen Torch Mode (+4 more)

### Community 2 - "Repository Contract"
Cohesion: 0.33
Nodes (10): Flow, List, String, DataRepository, DataResult, DataStatus, DefaultDataRepository, DataResult<T> (+2 more)

### Community 3 - "App Theme Navigation"
Cohesion: 0.20
Nodes (9): Color Palette, ComponentActivity, MainActivity, EmptyActivityTheme, Inverted ColorScheme Bug, MainNavigation, Navigation3 Pattern, Main NavKey (+1 more)

### Community 4 - "ViewModel Test Suite"
Cohesion: 0.36
Nodes (5): Flow, List, String, FakeMyModelRepository, MainScreenViewModelTest

### Community 5 - "Main Screen Test"
Cohesion: 0.32
Nodes (5): MainScreenTest, MainScreen, MainScreenUiState, MainScreenViewModel, MVVM Pattern

### Community 6 - "ViewModel State Flow"
Cohesion: 0.36
Nodes (7): Error, Loading, MainScreenUiState, MainScreenViewModel, Success, StateFlow, ViewModel

### Community 7 - "Launcher Icon Assets"
Cohesion: 0.50
Nodes (4): Android Adaptive Icon System, Density-Independent Resource Strategy, Android Robot Launcher Icon, Green Grid Design Pattern

## Knowledge Gaps
- **22 isolated node(s):** `Bundle`, `Boolean`, `List`, `StateFlow`, `Flow` (+17 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **1 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `MainScreenViewModel` connect `Main Screen Test` to `Compose UI Shell`, `Flashlight Feature Plan`, `Repository Contract`, `ViewModel Test Suite`?**
  _High betweenness centrality (0.322) - this node is a cross-community bridge._
- **Why does `MainScreen` connect `Main Screen Test` to `Flashlight Feature Plan`, `Repository Contract`, `App Theme Navigation`?**
  _High betweenness centrality (0.266) - this node is a cross-community bridge._
- **Why does `MainScreen()` connect `Compose UI Shell` to `Main Screen Test`?**
  _High betweenness centrality (0.216) - this node is a cross-community bridge._
- **Are the 2 inferred relationships involving `MainScreen()` (e.g. with `MainNavigation()` and `.setup()`) actually correct?**
  _`MainScreen()` has 2 INFERRED edges - model-reasoned connections that need verification._
- **What connects `Bundle`, `Boolean`, `List` to the rest of the system?**
  _22 weakly-connected nodes found - possible documentation gaps or missing edges._