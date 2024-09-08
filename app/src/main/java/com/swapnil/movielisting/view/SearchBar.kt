package com.swapnil.movielisting.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swapnil.movielisting.R
import com.swapnil.movielisting.extensions.Spacer

@Composable
fun SearchBar(modifier: Modifier = Modifier, text: String, onValueChange: (String) -> Unit) {
    val focusManager = LocalFocusManager.current

    Box(modifier = modifier
        .fillMaxWidth()
        .height(56.dp)
        .border(width = 1.dp, color = Color(0xFFE3E8EF), shape = RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = Color.White)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
               imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                contentDescription = "Search image"
            )
            Spacer(dp = 4.dp)
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = text,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Search Movies",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight(400),
                            color = Color(0xFF9AA4B2)
                        )
                    )
                },
                colors = TextFieldDefaults.colors().copy(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                    }
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchBarPreview() {
    Box {
        var query by remember {
            mutableStateOf("")
        }
        SearchBar(Modifier.fillMaxSize(), query) {
            query = it
        }
    }
}