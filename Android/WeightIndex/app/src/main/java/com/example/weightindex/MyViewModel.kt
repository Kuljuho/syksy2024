package com.example.weightindex

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MyViewModel: ViewModel() {
    var weight by mutableStateOf("")
    var height by mutableStateOf("")
    var bmi by mutableStateOf(0.0)
    var resultText by mutableStateOf("Anna paino ja pituus")

    fun calculateBMI() {
        val weightValue = weight.toDoubleOrNull()
        val heightValue = height.toDoubleOrNull()

        if (weightValue != null && heightValue != null && heightValue > 0) {
            bmi = weightValue / (heightValue * heightValue)
            resultText = "Painoindeksi on %.2f".format(bmi)
        } else {
            resultText = "Tarkista syötetyt arvot"
        }
    }
}