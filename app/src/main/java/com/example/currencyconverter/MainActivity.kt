package com.example.currencyconverter

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.currencyconverter.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    var fromCurrency = "EUR"
    var positionFrom = 1
    var toCurrency = "USD"
    var positionTo = 1
    var conversionRate = 0f


    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupSpinner()

        //add listener to input edit text
        binding.editFromConversion.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.d("Main", "After Text Changed")
                convertCurrency()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("Main", "Before Text Changed")
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("Main", "On Text Changed")
                convertCurrency()
            }
        })

        //add listener to button to swap currencies
        binding.swap.setOnClickListener {
            swapFunction()
        }
    }

    data class Currency(val image: Int,
                    val description: String)

    //adds items to the action bar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    //control the toolbar, used to change from one page to the other one
    override fun onOptionsItemSelected(item: MenuItem) =  when (item.itemId) {
            R.id.action_ratesList -> {
                val intent = Intent(this@MainActivity, ConversionRatesList::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
    }

    //set up spinner of currencies
    private fun setupSpinner(){
        val spinnerFrom: Spinner = binding.spinnerFromConversion
        val spinnerTo: Spinner = binding.spinnerToConversion
        ArrayAdapter.createFromResource(
            this,
            R.array.currencies,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerFrom.adapter = adapter
            spinnerTo.adapter = adapter
        }
        spinnerFrom.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                positionFrom = position
                fromCurrency = parent?.getItemAtPosition(position).toString()
                convertCurrency()
            }
        })

        spinnerTo.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                positionTo = position
                toCurrency = parent?.getItemAtPosition(position).toString()
                convertCurrency()
            }
        })
    }

    //manuel currency convertor
    private fun conversionRates(){
        val conversionRate_eurusd =  1.0354f
        val conversionRate_sekusd = 0.09636f
        val conversionRate_gbpusd = 1.18426f
        val conversionRate_cnyusd = 0.14068f
        val conversionRate_jpyusd = 0.0072f
        val conversionRate_krwusd = 0.00076f
        when (fromCurrency){
            "EUR" -> conversionRate = when (toCurrency){
                "SEK" -> conversionRate_eurusd/conversionRate_sekusd
                "GBP" -> conversionRate_eurusd/conversionRate_gbpusd
                "USD" -> conversionRate_eurusd
                "CNY" -> conversionRate_eurusd/conversionRate_cnyusd
                "JPY" -> conversionRate_eurusd/conversionRate_jpyusd
                "KRW" -> conversionRate_eurusd/conversionRate_krwusd
                else -> 0f
            }
            "SEK" -> conversionRate = when (toCurrency){
                "EUR" -> conversionRate_sekusd/conversionRate_eurusd
                "GBP" -> conversionRate_sekusd/conversionRate_gbpusd
                "USD" -> conversionRate_sekusd
                "CNY" -> conversionRate_sekusd/conversionRate_cnyusd
                "JPY" -> conversionRate_sekusd/conversionRate_jpyusd
                "KRW" -> conversionRate_sekusd/conversionRate_krwusd
                else -> 0f
            }

            "GBP" -> conversionRate = when (toCurrency){
                "EUR" -> conversionRate_gbpusd/conversionRate_eurusd
                "SEK" -> conversionRate_gbpusd/conversionRate_sekusd
                "USD" -> conversionRate_gbpusd
                "CNY" -> conversionRate_gbpusd/conversionRate_cnyusd
                "JPY" -> conversionRate_gbpusd/conversionRate_jpyusd
                "KRW" -> conversionRate_gbpusd/conversionRate_krwusd
                else -> 0f
            }

            "USD" -> conversionRate = when (toCurrency){
                "EUR" -> 1/conversionRate_eurusd
                "SEK" -> 1/conversionRate_sekusd
                "GBP" -> 1/conversionRate_gbpusd
                "CNY" -> 1/conversionRate_cnyusd
                "JPY" -> 1/conversionRate_jpyusd
                "KRW" -> 1/conversionRate_krwusd
                else -> 0f
            }

            "CNY" -> conversionRate = when (toCurrency){
                "EUR" -> conversionRate_cnyusd/conversionRate_eurusd
                "SEK" -> conversionRate_cnyusd/conversionRate_sekusd
                "USD" -> conversionRate_cnyusd
                "GBP" -> conversionRate_cnyusd/conversionRate_gbpusd
                "JPY" -> conversionRate_cnyusd/conversionRate_jpyusd
                "KRW" -> conversionRate_cnyusd/conversionRate_krwusd
                else -> 0f
            }

            "JPY" -> conversionRate = when (toCurrency){
                "SEK" -> conversionRate_jpyusd/conversionRate_sekusd
                "GBP" -> conversionRate_jpyusd/conversionRate_gbpusd
                "USD" -> conversionRate_jpyusd
                "CNY" -> conversionRate_jpyusd/conversionRate_cnyusd
                "EUR" -> conversionRate_jpyusd/conversionRate_eurusd
                "KRW" -> conversionRate_jpyusd/conversionRate_krwusd
                else -> 0f
            }

            "KRW" -> conversionRate = when (toCurrency){
                "SEK" -> conversionRate_krwusd/conversionRate_sekusd
                "GBP" -> conversionRate_krwusd/conversionRate_gbpusd
                "USD" -> conversionRate_krwusd
                "CNY" -> conversionRate_krwusd/conversionRate_cnyusd
                "JPY" -> conversionRate_krwusd/conversionRate_jpyusd
                "EUR" -> conversionRate_krwusd/conversionRate_eurusd
                else -> 0f
            }
            else -> conversionRate = 0f
        }
    }

    //swaps from and to
    private fun swapFunction(){
        val spinnerFrom: Spinner = binding.spinnerFromConversion
        val spinnerTo: Spinner = binding.spinnerToConversion
        ArrayAdapter.createFromResource(
            this,
            R.array.currencies,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerFrom.adapter = adapter
            spinnerTo.adapter = adapter
        }
        spinnerFrom.setSelection(positionTo)
        spinnerTo.setSelection(positionFrom)
    }

    //converts and shows converted value
    private fun convertCurrency() {
        conversionRates()

        if (binding.editFromConversion.text.isNotEmpty() && binding.editFromConversion.text.isNotBlank()) {
            if (fromCurrency == toCurrency) {
                binding.editToConversion.setText(binding.editFromConversion.text.toString())
            } else {
                val text = (binding.editFromConversion.text.toString().toFloat() * conversionRate).toString()
                binding.editToConversion.setText(text)
            }
        }
    }
}