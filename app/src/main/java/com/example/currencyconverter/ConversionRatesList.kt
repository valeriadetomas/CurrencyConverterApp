package com.example.currencyconverter

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import com.example.currencyconverter.databinding.ActivityConversionRatesListBinding
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ConversionRatesList : AppCompatActivity() {
    lateinit var binding: ActivityConversionRatesListBinding
    var selectedCurrency = "EUR"
    var conversions: ArrayList<String> = arrayListOf("1 EUR")
    var listOfCurrencies: ArrayList<String> = arrayListOf("EUR", "GBP", "USD")
    var rates: FloatArray = floatArrayOf(2.2f, 2.4f)

    var resultLauncherFromRatesList = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            getSelectionRates(data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversionRatesListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        //enable home button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rates = intent.getFloatArrayExtra("rates")!!
        listOfCurrencies = intent.getStringArrayListExtra("currencies")!!

        setupButton()
    }

    private fun getSelectionRates(data: Intent?){
        val ratesButton: Button = binding.ratesButton

        if (data != null) {
            if (data.getStringExtra("selected") != null) {
                selectedCurrency = data.getStringExtra("selected").toString()
                ratesButton.text = selectedCurrency
            }
            setRatesList()
        }
    }

    //set up the list
    private fun setRatesList(){
        val position = listOfCurrencies.indexOf(selectedCurrency)
        var i = 0
        var conversionRate: Float
        conversions.removeAll(conversions.toSet())

        while (i < listOfCurrencies.size){
            conversionRate = rates.get(i)/rates.get(position)
            conversions.add(conversionRate.toString() + listOfCurrencies.get(i).toString())
            i++
        }

        // access the listView
        val listView = binding.ratesList
        listView.adapter  = ArrayAdapter(this,
            R.layout.aligned_right, conversions)
        listView.isScrollbarFadingEnabled = false
    }

    private fun setupButton() {
        var locale: Locale = Locale.getDefault()
        selectedCurrency = NumberFormat.getCurrencyInstance(locale).currency.toString()

        val buttonRates: Button = binding.ratesButton

        buttonRates.text = selectedCurrency

        //add listener to button to select currency
        binding.ratesButton.setOnClickListener {
            val intent = Intent(this@ConversionRatesList, ListOfCurrencies::class.java)
            intent.putExtra("where", "rates")
            intent.putExtra("currencies", listOfCurrencies)
            resultLauncherFromRatesList.launch(intent)
        }
    }

}