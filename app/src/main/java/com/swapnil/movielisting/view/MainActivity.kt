package com.swapnil.movielisting.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swapnil.movielisting.ui.theme.MovieListingTheme
import com.swapnil.movielisting.view.viewmodel.MovieListAction
import com.swapnil.movielisting.view.viewmodel.MovieListViewModel
import com.swapnil.movielisting.view.viewmodel.SearchAction
import com.swapnil.movielisting.view.viewmodel.SearchBar
import com.swapnil.movielisting.view.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs
import kotlin.math.roundToInt

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

    val searchBarHeight = 56.dp
    val searchBarHeightPx = with(LocalDensity.current) { searchBarHeight.toPx() }
    var currentSearchBarOffset by remember { mutableStateOf(0f) }

    // Nested scroll connection to detect scroll events
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y.toInt()
                val newOffset = currentSearchBarOffset + delta
                val previous = currentSearchBarOffset
                currentSearchBarOffset = newOffset.coerceIn(-searchBarHeightPx, 0f)
                val consumed = currentSearchBarOffset - previous
                Log.d("SWAPNIL", "onPreScroll: no:$newOffset, p:$previous, c:$consumed")
                return Offset(0f, consumed)
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        moviesViewModel.processAction(MovieListAction.OnLaunch)
    }

    val movieList = when {
        searchState.isSearchActive() -> searchState.searchedMovies?.results
        moviesFeedState.isMoviesAvailable() -> moviesFeedState.movies?.results
        else -> null
    }

    val isLoading = searchState.isLoading || moviesFeedState.isLoading

    Column(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(nestedScrollConnection)
    ) {
        SearchBar(
            Modifier
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 8.dp)
                .offset {
                    IntOffset(x = 0, y = currentSearchBarOffset.roundToInt())
                },
            searchState.query.movieName
        ) {
            searchViewModel.processAction(SearchAction.OnSearch(it))
        }

        if(movieList != null) {
            Listing(
                modifier = Modifier
                    .offset {
                        IntOffset(x = 0, y = currentSearchBarOffset.roundToInt())
                    }
                    .fillMaxSize(),
                movies = movieList,
            )
        }

        if (isLoading) {
            LoadingAnimation()
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MovieListingTheme {
        Greeting("Android")
    }
}