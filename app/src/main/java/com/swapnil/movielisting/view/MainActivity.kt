package com.swapnil.movielisting.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swapnil.movielisting.ui.theme.MovieListingTheme
import com.swapnil.movielisting.view.viewmodel.MovieListAction
import com.swapnil.movielisting.view.viewmodel.MovieListViewModel
import com.swapnil.movielisting.view.viewmodel.SearchAction
import com.swapnil.movielisting.view.viewmodel.SearchBar
import com.swapnil.movielisting.view.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieListingTheme {
                Scaffold(modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .systemBarsPadding()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val moviesViewModel: MovieListViewModel = hiltViewModel()
    val moviesFeedState by moviesViewModel.state.collectAsState()

    val searchViewModel: SearchViewModel = hiltViewModel()
    val searchState by searchViewModel.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        moviesViewModel.processAction(MovieListAction.OnLaunch)
    }
    val movieList = when {
        searchState.isSearchActive() -> searchState.searchedMovies?.results
        moviesFeedState.isMoviesAvailable() -> moviesFeedState.movies?.results
        else -> null
    }

    Box {
        Listing(
            modifier = Modifier,
            movies = movieList,
            searchBar = {
                SearchBar(
                    Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 8.dp),
                    searchState.query.movieName
                ) {
                    searchViewModel.processAction(SearchAction.OnSearch(it))
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MovieListingTheme {
        Greeting("Android")
    }
}