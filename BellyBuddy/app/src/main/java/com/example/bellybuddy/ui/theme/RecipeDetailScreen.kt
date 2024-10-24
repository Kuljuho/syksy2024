package com.example.bellybuddy.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.bellybuddy.R
import com.example.bellybuddy.model.Recipe
import com.example.bellybuddy.viewmodel.RecipeViewModel

@Composable
fun RecipeDetailScreen(recipe: Recipe?, viewModel: RecipeViewModel, onBackClick: () -> Unit) {
    val isLoading by viewModel.isLoading.collectAsState()

    fun String.removeHtmlTags(): String {
        return this.replace(Regex("<.*?>"), "")
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.backk)
            )
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (recipe != null) {
            val rating = viewModel.ratings[recipe.id] ?: 0f
            var currentRating by remember { mutableStateOf(rating) }

            Image(
                painter = rememberAsyncImagePainter(recipe.image),
                contentDescription = recipe.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = MaterialTheme.typography.headlineLarge.fontSize * 1.2),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                stringResource(R.string.ingredients),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = MaterialTheme.typography.titleLarge.fontSize * 1.4)
            )
            recipe.extendedIngredients.forEach { ingredient ->
                Text(
                    "• ${ingredient.amount} ${ingredient.unit} ${ingredient.name}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = MaterialTheme.typography.bodyMedium.fontSize * 1.4)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                stringResource(R.string.instructions),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = MaterialTheme.typography.titleLarge.fontSize * 1.3)
            )
            Text(
                text = recipe.instructions.removeHtmlTags(),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = MaterialTheme.typography.bodyMedium.fontSize * 1.2)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.rate_recipe),
                style = MaterialTheme.typography.titleMedium.copy(fontSize = MaterialTheme.typography.titleMedium.fontSize * 1.2)
            )
            RatingBar(
                rating = currentRating,
                onRatingChanged = { newRating ->
                    currentRating = newRating
                    viewModel.addRating(recipe.id, newRating)
                },
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = stringResource(R.string.current_rating, currentRating),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = MaterialTheme.typography.bodyMedium.fontSize * 1.2)
            )
        }
    }
}