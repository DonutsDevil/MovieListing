# Movie Listing App

Welcome to the Movie Listing App! This application allows users to search for and view details of movies using a clean architecture approach. It leverages the MVI (Model-View-Intent) pattern combined with Clean Architecture principles to ensure a maintainable and scalable codebase.

## Features

- **Search Movies:** Allows users to search for movies by name.
- **View Details:** Provides detailed information about selected movies.
- **Responsive UI:** Provides a smooth and responsive user experience with Jetpack Compose.

## Architecture

### Clean Architecture + MVI

Our app is designed following Clean Architecture principles to separate concerns and improve maintainability. The architecture is divided into the following layers:

- **Presentation Layer:** Responsible for UI and user interaction. Uses MVI to manage state and handle user actions.
- **Domain Layer:** Contains business logic and domain models. It is independent of any framework or UI-related code.
- **Data Layer:** Handles data operations, including network requests. Provides data to the domain layer through repositories.


## App States

Here are different states of the app and how they are handled:

### Searching + State

While searching for movies, the app displays a loading indicator, scroll state and shows search results as they are retrieved:

<img src="gif/searc%2Bscroll_state.gif" width="150">

### Preview

The user can select a movie and check the preview of it, when navigating back the user is back to the old state when he left.

<img src="gif/previous_scroll_state.gif" width="150">


### No Movies Found

If no movies match the search criteria, the app informs the user with a message:

<img src="gif/no_movies_found.gif" width="150">

### No Internet Connection

When the app detects that there is no internet connection, it shows an appropriate message to inform the user:

<img src="gif/no_internet.gif" width="150">




## API Integration

The Movie Listing App uses the TMDB (The Movie Database) API to fetch movie data.

### API Token

To access the TMDB API, you need an API token. You can store your API token securely in the `gradle.properties` file:

1. **Obtain an API Token:**
   - Sign up and generate your API token at [TMDB API](https://www.themoviedb.org/documentation/api).

2. **Store the Token:**
   - Add the following line to your `gradle.properties` file located in the root of your project:

     ```properties
     TMDB_API_TOKEN=your_api_token_here
     ```
