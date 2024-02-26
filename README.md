## Description
**NutriFinder** is a basic app that displays the result of an REST API call. It is a simple food browser where the user neeeds to input a text in the text field and the matching items will be displayed in a list below the text field.

## Tech stack and Open-source libraries
- Minimum SDK level 27
- Built in [Kotlin](https://kotlinlang.org/) and utilizes [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) and [Flows](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for handling asynchronous operations.
- Jetpack components:
    - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel): Manages UI-related data and is lifecycle-aware, ensuring data survives configuration changes like screen rotations.
    - [Hilt](https://dagger.dev/hilt/): Used for dependency injection.
    - [Jetpack Compose](https://developer.android.com/jetpack/compose): Android's recommended modern toolkit for building native UI
    - [Navigation](https://developer.android.com/guide/navigation) for Compose: framework for navigating between destinations.
- Architecture
    - MVVM Architecture (Model - View - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel))
    - Repository Pattern
- [Ktor](https://ktor.io/): Used for REST API calls.

## Architecture
- **NutriFinder** utilizes the MVVM architecture and the Repository pattern, which follows the [Google's official architecture guidance](https://developer.android.com/topic/architecture).

![architecture](previews/architecture_layers.png)

**NutriFinder** follows a three-layer architecture comprising the UI layer, the data layer and the domain layer, where each layer has dedicated components with different responsibilities.<br>
Also, each layer follows [unidirectional event/data flow](https://developer.android.com/topic/architecture/ui-layer#udf), meaning:<br>
<p align="center"><i>the UI emits events → the mediator (ViewModel) notifies the state change to the data layer → the data layer updates the application data → the ViewModel gets the updated data and updates the UI</i></p>