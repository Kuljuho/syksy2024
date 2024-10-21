package com.example.bellybuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bellybuddy.network.RetrofitInstance
import com.example.bellybuddy.model.Recipe
import com.example.bellybuddy.model.RecipeSearchResponse
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.mutableStateMapOf
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class RecipeViewModel : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _searchResults = MutableStateFlow<List<Recipe>>(emptyList())
    val searchResults: StateFlow<List<Recipe>> = _searchResults

    private val _ratings = mutableStateMapOf<Int, Float>()
    val ratings: Map<Int, Float> = _ratings

    private val _selectedRecipe = MutableStateFlow<Recipe?>(null)
    val selectedRecipe: StateFlow<Recipe?> = _selectedRecipe

    fun addRating(recipeId: Int, rating: Float) {
        _ratings[recipeId] = rating
    }

    fun refreshRecipes(apiKey: String) {
        getRecipes(apiKey)
    }

    private fun parseErrorMessage(errorBody: String?): String {
        return try {
            val gson = Gson()
            val jsonObject = gson.fromJson(errorBody, JsonObject::class.java)
            jsonObject.get("message").asString
        } catch (e: Exception) {
            "Virhe dataa haettaessa"
        }
    }

    fun getRecipes(apiKey: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = RetrofitInstance.api.getRandomRecipes(apiKey)
                if (response.recipes.isEmpty()) {
                    _errorMessage.value = "Ei reseptejä saatavilla"
                } else {
                    _recipes.value = response.recipes
                }
            } catch (e: Exception) {
                _errorMessage.value = when (e) {
                    is IOException -> "Verkkovirhe: ${e.message}"
                    is HttpException -> {
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorMessageFromServer = parseErrorMessage(errorBody)
                        "HTTP-virhe ${e.code()}: $errorMessageFromServer"
                    }
                    else -> "Tuntematon virhe: ${e.message}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getRecipeById(apiKey: String, recipeId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val recipeDetail = RetrofitInstance.api.getRecipeDetails(recipeId, apiKey)
                _selectedRecipe.value = recipeDetail
            } catch (e: Exception) {
                _errorMessage.value = when (e) {
                    is IOException -> "Verkkovirhe: ${e.message}"
                    is HttpException -> {
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorMessageFromServer = parseErrorMessage(errorBody)
                        "HTTP-virhe ${e.code()}: $errorMessageFromServer"
                    }
                    else -> "Tuntematon virhe: ${e.message}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchRecipes(apiKey: String, query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = RetrofitInstance.api.searchRecipes(apiKey, query)
                if (response.results.isEmpty()) {
                    _errorMessage.value = "Ei reseptejä hakusanalla \"$query\""
                    _searchResults.value = emptyList()
                } else {
                    _searchResults.value = response.results
                }
            } catch (e: Exception) {
                _errorMessage.value = when (e) {
                    is IOException -> "Verkkovirhe: ${e.message}"
                    is HttpException -> {
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorMessageFromServer = parseErrorMessage(errorBody)
                        "HTTP-virhe ${e.code()}: $errorMessageFromServer"
                    }
                    else -> "Tuntematon virhe: ${e.message}"
                }
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}