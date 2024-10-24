package com.example.bellybuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bellybuddy.ui.InfoScreen
import com.example.bellybuddy.ui.RecipeListScreen
import com.example.bellybuddy.ui.theme.BellyBuddyTheme
import com.example.bellybuddy.ui.theme.RecipeDetailScreen
import com.example.bellybuddy.viewmodel.RecipeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BellyBuddyTheme {
                val navController = rememberNavController()
                val viewModel: RecipeViewModel = viewModel()
                val apiKey = 

                NavHost(navController = navController, startDestination = "recipe_list") {
                    composable("recipe_list") {
                        LaunchedEffect(Unit) {
                            viewModel.getRecipes(apiKey)
                        }
                        RecipeListScreen(
                            viewModel,
                            onRecipeClick = { recipe ->
                                navController.navigate("recipe_detail/${recipe.id}")
                            },
                            onInfoClick = {
                                navController.navigate("info")
                            },
                            apiKey = apiKey
                        )
                    }
                    composable("recipe_detail/{recipeId}") { backStackEntry ->
                        val recipeId = backStackEntry.arguments?.getString("recipeId")?.toIntOrNull()
                        if (recipeId != null) {
                            LaunchedEffect(recipeId) {
                                viewModel.getRecipeById(apiKey, recipeId)
                            }

                            val selectedRecipe by viewModel.selectedRecipe.collectAsState()
                            RecipeDetailScreen(
                                recipe = selectedRecipe,
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() }
                            )
                        } else {
                            navController.popBackStack()
                        }
                    }
                    composable("info") {
                        InfoScreen(onBackClick = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}