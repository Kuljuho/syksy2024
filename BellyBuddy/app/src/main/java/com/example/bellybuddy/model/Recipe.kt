package com.example.bellybuddy.model

data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val servings: Int,
    val readyInMinutes: Int,
    val sourceUrl: String,
    val summary: String,
    val instructions: String,
    val extendedIngredients: List<Ingredient>
)

data class Ingredient(
    val id: Int,
    val name: String,
    val amount: Double,
    val unit: String
)
