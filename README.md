# Rick and Morty Android App

Android application built with 100% Kotlin and Jetpack Compose that explores the universe of "Rick and Morty". This app consumes the [Rick and Morty API](https://rickandmortyapi.com/) to display characters and episodes from the show. It demonstrates modern Android development practices, including a clean architecture, dependency injection, and a reactive UI.

## Features

*   **Character Browser**: View a grid of all characters from the show.
*   **Character Details**: Tap on a character to see detailed information, including their status, species, origin, current location, and a list of episodes they've appeared in.
*   **Episode Details**: Navigate from a character's profile to see details about a specific episode, including its air date and all the characters featured.
*   **Search**: Easily search for characters by name.
*   **Modern UI**: User interface built entirely with Jetpack Compose, with support for both light and dark themes.
*   **Loading & Error States**: Handles loading and network error states with Lottie animations and informative messages.

## Technology Stack & Architecture

This project follows a clean, MVVM-like architecture and leverages a modern Android technology stack.

*   **Tech Stack**
    *   **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) for building the entire UI declaratively.
    *   **Networking**: [Ktor](https://ktor.io/) for making asynchronous HTTP requests to the Rick and Morty API.
    *   **Dependency Injection**: [Koin](https://insert-koin.io/) for managing dependencies throughout the app.
    *   **Asynchronous Programming**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) for managing background threads and API calls.
    *   **Image Loading**: [Coil](https://coil-kt.github.io/coil/) for loading and displaying images.
    *   **Navigation**: A custom navigation solution built with Jetpack Compose and integrated with Koin.
    *   **Serialization**: [Kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) for parsing JSON data from the API.
    *   **Animations**: [Lottie](https://airbnb.io/lottie/) for displaying engaging loading animations.

*   **Architecture**
    *   **UI Layer**: Contains Jetpack Compose screens (`character_details`, `characters_screen`), reusable components, and ViewModels which manage UI state and business logic.
    *   **Data Layer**: Includes repositories that abstract the data sources. The `network` package contains the Ktor client (`RickAndMortyApi`) responsible for fetching data from the remote API.
    *   **Model**: DTOs (`data/model/dto`) for parsing API responses and domain models (`ui/models`) used within the UI layer.

## Project Structure

The codebase is organized into logical packages to maintain a clean and scalable structure.

```
.
└── app/src/main/java/com/mjalocha/rickandmortyapp/
    ├── data/                # Data layer
    │   ├── model/           # DTOs, enums, and response models
    │   ├── network/         # Ktor API interface and implementation
    │   └── repository/      # Repositories for data abstraction
    ├── di/                  # Koin dependency injection modules
    └── ui/                  # UI (presentation) layer
        ├── character_details/ # Character details screen and ViewModel
        ├── characters_screen/ # Character list screen and ViewModel
        ├── components/      # Reusable UI components (e.g., CharacterCard)
        ├── episode_details_screen/ # Episode details screen and ViewModel
        ├── models/          # UI-specific data models
        ├── navigation/      # Navigation routes and Navigator logic
        └── theme/           # Jetpack Compose theme, colors, and typography
```
## App Screenshots

<table>
  <!-- Light Mode Section -->
  <tr>
    <th colspan="4" align="center">Light Mode</th>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/c54ef1f1-7317-4e11-adb4-bfa21f044b6c" width="220" alt="Home Screen Light Mode">
      <br>Home
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/b2357f51-a368-4b54-bcc4-099b94001b6b" width="220" alt="Character Details Light Mode">
      <br>Character Details
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/48115d75-d28f-49d7-985e-676b23da67d3" width="220" alt="Character Details 2 Light Mode">
      <br>More Details
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/cfda4cf0-7bb7-4ca3-9b2d-4661268a9290" width="220" alt="Episode Details Light Mode">
      <br>Episode
    </td>
  </tr>
  
  <!-- Dark Mode Section -->
  <tr>
    <th colspan="4" align="center">Dark Mode</th>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/9a67cc43-a53f-4cae-8610-9447dae6bf96" width="220" alt="Home Screen Dark Mode">
      <br>Home
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/f8a2fc0e-4295-4488-80c9-151fd3aa58e1" width="220" alt="Character Details Dark Mode">
      <br>Character Details
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/d2400b70-36c4-4090-8dc5-6f37c96b12e9" width="220" alt="Character Details 2 Dark Mode">
      <br>More Details
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/cf5b7043-2784-4440-b918-97ff800b147d" width="220" alt="Episode Details Dark Mode">
      <br>Episode
    </td>
  </tr>
</table>
