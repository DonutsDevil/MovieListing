package com.swapnil.movielisting.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.swapnil.movielisting.R
import com.swapnil.movielisting.extensions.Spacer
import com.swapnil.movielisting.routing.Router
import com.swapnil.movielisting.view.viewmodel.MoviePreviewViewModel
import com.swapnil.movielisting.view.viewmodel.PreviewAction
import com.swapnil.movielisting.view.viewmodel.PreviewState


@Composable
fun MoviePreviewNavigation(
    navBackStack: NavBackStackEntry,
    navController: NavHostController
) {
    val selectedMovieId = navBackStack.arguments?.getString(Router.MovieDetails.ID)

    if (selectedMovieId == null) navController.navigateUp()

    val moviePreviewViewModel: MoviePreviewViewModel = hiltViewModel()
    LaunchedEffect(key1 = selectedMovieId) {
        moviePreviewViewModel.processAction(PreviewAction.PreviewSelected(selectedMovieId!!.toInt()))
    }

    val moviePreviewState by moviePreviewViewModel.state.collectAsState()

    when {
        moviePreviewState.isLoading -> {
            LoadingAnimation()
        }

        moviePreviewState.error != null -> {
            ErrorView(text = moviePreviewState.error!!)
        }

        else -> {
            MoviePreview(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp),
                previewState = moviePreviewState,
                goBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}

@Composable
fun MoviePreview(modifier: Modifier = Modifier, previewState: PreviewState, goBack: () -> Unit) {
    Column(modifier = modifier) {
        Image(
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    goBack()
                },
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_back),
            contentDescription = "back arrow"
        )

        Spacer(dp = 24.dp)

        AsyncImage(
            model = previewState.movie!!.posterPath,
            modifier = Modifier
                .fillMaxWidth()
                .height(361.dp)
                .clip(RoundedCornerShape(6.dp)),
            contentDescription = "Movie Poster"
        )

        Spacer(dp = 24.dp)

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            text = previewState.movie.title,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight(500),
                fontSize = 24.sp,
                lineHeight = 32.sp,
                color = Color.Black
            )
        )
        
        Spacer(dp = 24.dp)

        Text(text = previewState.movie.overview,
            style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight(400),
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = Color.Black
        ))
    }
}