package com.example.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.currencyconverter.databinding.ActivityConversionRatesListBinding
import com.example.currencyconverter.databinding.ActivityMainBinding

class ConversionRatesList : AppCompatActivity() {
    //lateinit var binding: ActivityConversionRatesListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityConversionRatesListBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_conversion_rates_list)

        setSupportActionBar(findViewById(R.id.toolbar))

        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



    }
}