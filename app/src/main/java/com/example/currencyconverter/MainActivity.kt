package com.example.currencyconverter

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
    var conversionRate_eurusd =  1.0354f
    var conversionRate_sekusd = 0.09636f
    var conversionRate_gbpusd = 1.18426f
    var conversionRate_cnyusd = 0.14068f
    var conversionRate_jpyusd = 0.0072f
    var conversionRate_krwusd = 0.00076f


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
                convertCurrency()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                convertCurrency()
            }
        })
        //add listener to button to swap currencies
        binding.swap.setOnClickListener {
            swapFunction()
        }
    }

    //adds items to the action bar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    //control the toolbar, used to change from one page to the other one
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_converter) {
            //todo: it does nothing because you are already in the page
            Toast.makeText(this, "Converter Clicked", Toast.LENGTH_LONG).show()
            return true
        }
        else if (item.itemId == R.id.action_ratesList) {
            val intent = Intent(this@MainActivity,ConversionRatesList::class.java)
            startActivity(intent)
            Toast.makeText(this, "Info Clicked", Toast.LENGTH_LONG).show()
            return true
        }
        return super.onOptionsItemSelected(item)
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
        when (fromCurrency){
            "EUR" -> when (toCurrency){
                "SEK" -> conversionRate = conversionRate_eurusd/conversionRate_sekusd
                "GBP" -> conversionRate = conversionRate_eurusd/conversionRate_gbpusd
                "USD" -> conversionRate = conversionRate_eurusd
                "CNY" -> conversionRate = conversionRate_eurusd/conversionRate_cnyusd
                "JPY" -> conversionRate = conversionRate_eurusd/conversionRate_jpyusd
                "KRW" -> conversionRate = conversionRate_eurusd/conversionRate_krwusd
            }
            "SEK" -> when (toCurrency){
                "EUR" -> conversionRate = conversionRate_sekusd/conversionRate_eurusd
                "GBP" -> conversionRate = conversionRate_sekusd/conversionRate_gbpusd
                "USD" -> conversionRate = conversionRate_sekusd
                "CNY" -> conversionRate = conversionRate_sekusd/conversionRate_cnyusd
                "JPY" -> conversionRate = conversionRate_sekusd/conversionRate_jpyusd
                "KRW" -> conversionRate = conversionRate_sekusd/conversionRate_krwusd
            }

            "GBP" -> when (toCurrency){
                "EUR" -> conversionRate = conversionRate_gbpusd/conversionRate_eurusd
                "SEK" -> conversionRate = conversionRate_gbpusd/conversionRate_sekusd
                "USD" -> conversionRate = conversionRate_gbpusd
                "CNY" -> conversionRate = conversionRate_gbpusd/conversionRate_cnyusd
                "JPY" -> conversionRate = conversionRate_gbpusd/conversionRate_jpyusd
                "KRW" -> conversionRate = conversionRate_gbpusd/conversionRate_krwusd
            }

            "USD" -> when (toCurrency){
                "EUR" -> conversionRate = 1/conversionRate_eurusd
                "SEK" -> conversionRate = 1/conversionRate_sekusd
                "GBP" -> conversionRate = 1/conversionRate_gbpusd
                "CNY" -> conversionRate = 1/conversionRate_cnyusd
                "JPY" -> conversionRate = 1/conversionRate_jpyusd
                "KRW" -> conversionRate = 1/conversionRate_krwusd
            }

            "CNY" -> when (toCurrency){
                "EUR" -> conversionRate = conversionRate_cnyusd/conversionRate_eurusd
                "SEK" -> conversionRate = conversionRate_cnyusd/conversionRate_sekusd
                "USD" -> conversionRate = conversionRate_cnyusd
                "GBP" -> conversionRate = conversionRate_cnyusd/conversionRate_gbpusd
                "JPY" -> conversionRate = conversionRate_cnyusd/conversionRate_jpyusd
                "KRW" -> conversionRate = conversionRate_cnyusd/conversionRate_krwusd
            }

            "JPY" -> when (toCurrency){
                "SEK" -> conversionRate = conversionRate_jpyusd/conversionRate_sekusd
                "GBP" -> conversionRate = conversionRate_jpyusd/conversionRate_gbpusd
                "USD" -> conversionRate = conversionRate_jpyusd
                "CNY" -> conversionRate = conversionRate_jpyusd/conversionRate_cnyusd
                "EUR" -> conversionRate = conversionRate_jpyusd/conversionRate_eurusd
                "KRW" -> conversionRate = conversionRate_jpyusd/conversionRate_krwusd
            }

            "KRW" -> when (toCurrency){
                "SEK" -> conversionRate = conversionRate_krwusd/conversionRate_sekusd
                "GBP" -> conversionRate = conversionRate_krwusd/conversionRate_gbpusd
                "USD" -> conversionRate = conversionRate_krwusd
                "CNY" -> conversionRate = conversionRate_krwusd/conversionRate_cnyusd
                "JPY" -> conversionRate = conversionRate_krwusd/conversionRate_jpyusd
                "EUR" -> conversionRate = conversionRate_krwusd/conversionRate_eurusd
            }
        }
    }

    //TODO: IT DOES NOT WORK, IT NEEDS TO BE FIXED
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
        //don't know how to set the parent of the spinner
        spinnerTo.parent?.bringChildToFront(spinnerFrom.getChildAt(positionFrom))
        spinnerFrom.parent?.bringChildToFront(spinnerFrom.getChildAt(positionTo))

    }

    //converts and shows converted value
    private fun convertCurrency() {
        conversionRates()
        if (binding.editFromConversion.text.isNotEmpty() && binding.editFromConversion.text.isNotBlank()) {
            if (fromCurrency == toCurrency) {
                binding.editToConversion.setText(binding.editFromConversion.text.toString())
                Toast.makeText(
                    applicationContext,
                    "Pick two different currencies",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val text = (binding.editFromConversion.text.toString().toFloat() * conversionRate).toString()
                binding.editToConversion.setText(text)
            }
        }
    }


}