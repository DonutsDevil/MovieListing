package com.swapnil.movielisting.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.swapnil.movielisting.domain.model.MovieListItem
import com.swapnil.movielisting.routing.Router
import com.swapnil.movielisting.view.viewmodel.MovieListAction
import com.swapnil.movielisting.view.viewmodel.MovieListViewModel
import com.swapnil.movielisting.view.viewmodel.SearchAction
import com.swapnil.movielisting.view.viewmodel.SearchViewModel


@Composable
fun FeedNavigation(modifier: Modifier = Modifier, navController: NavController) {
    val moviesViewModel: MovieListViewModel = hiltViewModel()
    val moviesFeedState by moviesViewModel.state.collectAsState()

    val searchViewModel: SearchViewModel = hiltViewModel()
    val searchState by searchViewModel.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        moviesViewModel.processAction(MovieListAction.OnLaunch)
    }

    val getMovieList: () -> List<MovieListItem>? = {
        when {
            searchState.isSearchActive() -> searchState.searchedMovies?.results
            moviesFeedState.areMoviesAvailable() -> moviesFeedState.movies?.results
            else -> null
        }
    }

    val showLoading = searchState.isLoading || moviesFeedState.isLoading
    val showError = searchState.error != null || moviesFeedState.error != null

    MovieFeed(modifier,
        searchState.query.movieName,
        getMovieList = getMovieList,
        showLoading = showLoading,
        showError = showError,
        noMoviesFound = { searchState.noMoviesFound() },
        search = { searchViewModel.processAction(SearchAction.Search(it)) },
        getErrorMessage = {
            if(searchState.isSearchActive()) {
                searchState.error
            } else {
                moviesFeedState.error
            }
        },
        onMovieTapped = { selectedMovieId ->
            val route  = Router.MovieDetails(id = selectedMovieId).key
            navController.navigate(route)
        }
    )
}

@Composable
private fun MovieFeed(
    modifier: Modifier = Modifier,
    searchText: String,
    showLoading: Boolean,
    showError: Boolean,
    noMoviesFound: () -> Boolean,
    search: (String) -> Unit,
    getMovieList: () -> List<MovieListItem>?,
    getErrorMessage: () -> String?,
    onMovieTapped: (Int) -> Unit
) {
    val movieList = getMovieList()

    Column(modifier = modifier.fillMaxSize()) {
        SearchBar(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 8.dp),
            text = searchText,
            onValueChange = search
        )

        when {
            showLoading -> {
                LoadingAnimation()
            }

            noMoviesFound() -> {
                ErrorView(
                    modifier = Modifier.fillMaxSize(),
                    text = "No Movies Found with Name $searchText"
                )
            }

            showError -> {
                ErrorView(
                    modifier = Modifier.fillMaxSize(),
                    text = getErrorMessage()!!
                )
            }

            movieList != null -> {
                MovieListingScreen(
                    modifier = Modifier
                        .fillMaxSize(),
                    movies = movieList,
                    onMovieTapped = onMovieTapped
                )
            }
        }
    }
}