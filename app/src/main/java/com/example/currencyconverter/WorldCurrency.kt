package com.example.currencyconverter

data class WorldCurrency(
    val success: Boolean,
    val timestamp: Int,
    val base: String,
    val rates: Map<String, Float>
)
