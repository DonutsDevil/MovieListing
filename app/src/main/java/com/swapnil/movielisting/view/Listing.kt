package com.swapnil.movielisting.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.swapnil.movielisting.domain.model.MovieListItem
import com.swapnil.movielisting.extensions.Spacer
import com.swapnil.movielisting.view.viewmodel.SearchBar

@Composable
fun Listing(
    modifier: Modifier = Modifier,
    movies: List<MovieListItem>?,
    searchBar: @Composable () -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxWidth(),
        columns = GridCells.Fixed(2),
    ) {
        items(count = 1, span = { GridItemSpan(2) }) {
            searchBar()
        }
        movies?.let {
            items(it.size, key = { index -> it[index].id }) { index ->
                MovieItem(it[index].title, it[index].poster_path)
            }
        }
    }
}

@Composable
fun MovieItem(title: String, imageUrl: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .height(220.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = imageUrl,
            modifier = Modifier
                .size(172.dp)
                .clip(RoundedCornerShape(6.dp)),
            contentScale = ContentScale.FillBounds,
            contentDescription = title + "poster"
        )

        Spacer(dp = 12.dp)

        Text(
            modifier = Modifier
                .width(172.dp)
                .wrapContentHeight(),
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Spacer(dp = 8.dp)
    }
}