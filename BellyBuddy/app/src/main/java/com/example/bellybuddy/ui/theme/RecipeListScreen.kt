package com.example.bellybuddy.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberAsyncImagePainter
import com.example.bellybuddy.R
import com.example.bellybuddy.model.Recipe
import com.example.bellybuddy.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(
    viewModel: RecipeViewModel,
    onRecipeClick: (Recipe) -> Unit,
    onInfoClick: () -> Unit,
    apiKey: String
) {
    val recipes by viewModel.recipes.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var isSearchBarVisible by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.bellybuddy),
                        contentDescription = stringResource(R.string.app_name),
                        modifier = Modifier
                            .height(80.dp)
                            .align(Alignment.Center)
                    )
                }

                Spacer(
                    modifier = Modifier
                        .height(15.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                )

                Scaffold(
                    containerColor = Color.Transparent,
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = stringResource(R.string.recipes),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Transparent,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            actions = {
                                IconButton(onClick = {
                                    viewModel.clearSearchResults()
                                    viewModel.refreshRecipes(apiKey)
                                }) {
                                    Icon(
                                        Icons.Default.Refresh,
                                        contentDescription = stringResource(R.string.refresh_recipes)
                                    )
                                }
                                IconButton(onClick = onInfoClick) {
                                    Icon(
                                        Icons.Default.Info,
                                        contentDescription = stringResource(R.string.info)
                                    )
                                }
                                IconButton(onClick = { isSearchBarVisible = !isSearchBarVisible }) {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = stringResource(R.string.search)
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    },
                    content = { innerPadding ->
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background)
                        ) {
                            if (isSearchBarVisible) {
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    label = { Text(stringResource(R.string.search_recipes)) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                        .background(MaterialTheme.colorScheme.surface),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        imeAction = ImeAction.Search
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onSearch = {
                                            if (searchQuery.isNotBlank()) {
                                                viewModel.searchRecipes(apiKey, searchQuery)
                                            }
                                        }
                                    ),
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        cursorColor = MaterialTheme.colorScheme.primary,
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        containerColor = MaterialTheme.colorScheme.surface
                                    ),
                                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }

                            Box(modifier = Modifier.fillMaxSize()) {
                                when {
                                    errorMessage != null -> {
                                        Text(
                                            text = errorMessage ?: "",
                                            color = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.align(Alignment.Center)
                                        )
                                    }
                                    else -> {
                                        val displayRecipes = if (searchResults.isNotEmpty()) searchResults else recipes
                                        LazyColumn {
                                            items(displayRecipes) { recipe ->
                                                RecipeItem(recipe, onClick = { onRecipeClick(recipe) })
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
            }

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun RecipeItem(recipe: Recipe, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Image(
                painter = rememberAsyncImagePainter(recipe.image),
                contentDescription = recipe.title,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterVertically),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}