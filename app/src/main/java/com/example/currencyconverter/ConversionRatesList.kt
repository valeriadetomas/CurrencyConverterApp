package com.example.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import com.example.currencyconverter.databinding.ActivityConversionRatesListBinding

class ConversionRatesList : AppCompatActivity() {
    lateinit var binding: ActivityConversionRatesListBinding
    var selectedCurrency = "EUR"
    var conversionOne = "1 SEK"
    var conversionTwo = "1 USD"
    var conversionThree = "1 GBP"
    var conversionFour = "1 CNY"
    var conversionFive = "1 JPY"
    var conversionSix = "1 KRW"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversionRatesListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        //enable home button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupSpinner()

    }

    //set up the list
    private fun setRatesList(){
        conversionRatesToString()
        //all the currencies with the correspondent rate except for the one selected are part of this array
        val conversionRates = arrayOf(
            conversionOne, conversionTwo, conversionThree,
            conversionFour, conversionFive, conversionSix
        )
        // access the listView
        var listView = binding.ratesList

        listView.adapter  = ArrayAdapter(this,
            R.layout.aligned_right, conversionRates)
    }

    private fun setupSpinner(){
        val spinnerList: Spinner = binding.spinnerListRates
        ArrayAdapter.createFromResource(
            this,
            R.array.currencies,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            spinnerList.adapter = adapter
        }
        spinnerList.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedCurrency = parent?.getItemAtPosition(position).toString()
                setRatesList()
            }
        })
    }

    private fun conversionRatesToString(){
        val conversionRate_eurusd =  1.0354f
        val conversionRate_sekusd = 0.09636f
        val conversionRate_gbpusd = 1.18426f
        val conversionRate_cnyusd = 0.14068f
        val conversionRate_jpyusd = 0.0072f
        val conversionRate_krwusd = 0.00076f
        when (selectedCurrency){
            "EUR" -> {
                conversionOne = (conversionRate_eurusd/conversionRate_sekusd).toString() + "  SEK"
                conversionTwo = (conversionRate_eurusd/conversionRate_gbpusd).toString() + "  GBP"
                conversionThree = "$conversionRate_eurusd  USD"
                conversionFour = (conversionRate_eurusd/conversionRate_cnyusd).toString() + "  CNY"
                conversionFive = (conversionRate_eurusd/conversionRate_jpyusd).toString() + "  JPY"
                conversionSix = (conversionRate_eurusd/conversionRate_krwusd).toString() + "  KRW"
            }
            "SEK" -> {
                conversionOne = (conversionRate_sekusd/conversionRate_eurusd).toString() + "  EUR"
                conversionTwo = (conversionRate_sekusd/conversionRate_gbpusd).toString() + "  GBP"
                conversionThree = "$conversionRate_sekusd  USD"
                conversionFour = (conversionRate_sekusd/conversionRate_cnyusd).toString() + "  CNY"
                conversionFive = (conversionRate_sekusd/conversionRate_jpyusd).toString() + "  JPY"
                conversionSix = (conversionRate_sekusd/conversionRate_krwusd).toString() + "  KRW"
            }

            "GBP" -> {
                conversionOne = (conversionRate_gbpusd/conversionRate_eurusd).toString() + "  EUR"
                conversionTwo = (conversionRate_gbpusd/conversionRate_sekusd).toString() + "  SEK"
                conversionThree = "$conversionRate_gbpusd  USD"
                conversionFour = (conversionRate_gbpusd/conversionRate_cnyusd).toString() + "  CNY"
                conversionFive = (conversionRate_gbpusd/conversionRate_jpyusd).toString() + "  JPY"
                conversionSix = (conversionRate_gbpusd/conversionRate_krwusd).toString() + "  KRW"
            }

            "USD" -> {
                conversionOne = (1/conversionRate_eurusd).toString() + "  EUR"
                conversionTwo = (1/conversionRate_sekusd).toString() + "  SEK"
                conversionThree = (1/conversionRate_gbpusd).toString() + "  GBP"
                conversionFour = (1/conversionRate_cnyusd).toString() + "  CNY"
                conversionFive = (1/conversionRate_jpyusd).toString() + "  JPY"
                conversionSix = (1/conversionRate_krwusd).toString() + "  KRW"
            }

            "CNY" -> {
                conversionOne = (conversionRate_cnyusd/conversionRate_eurusd).toString() + "  EUR"
                conversionTwo = (conversionRate_cnyusd/conversionRate_sekusd).toString() + "  SEK"
                conversionThree = "$conversionRate_cnyusd  USD"
                conversionFour = (conversionRate_cnyusd/conversionRate_gbpusd).toString() + "  GBP"
                conversionFive = (conversionRate_cnyusd/conversionRate_jpyusd).toString() + "  JPY"
                conversionSix = (conversionRate_cnyusd/conversionRate_krwusd).toString() + "  KRW"
            }

            "JPY" -> {
                conversionOne = (conversionRate_jpyusd/conversionRate_sekusd).toString() + "  SEK"
                conversionTwo = (conversionRate_jpyusd/conversionRate_gbpusd).toString() + "  GBP"
                conversionThree= "$conversionRate_jpyusd  USD"
                conversionFour= (conversionRate_jpyusd/conversionRate_cnyusd).toString() + "  CNY"
                conversionFive= (conversionRate_jpyusd/conversionRate_eurusd).toString() + "  EUR"
                conversionSix= (conversionRate_jpyusd/conversionRate_krwusd).toString() + "  KRW"
            }

            "KRW" -> {
                conversionOne = (conversionRate_krwusd/conversionRate_sekusd).toString() + "  SEK"
                conversionTwo = (conversionRate_krwusd/conversionRate_gbpusd).toString() + "  GBP"
                conversionThree = "$conversionRate_krwusd  USD"
                conversionFour = (conversionRate_krwusd/conversionRate_cnyusd).toString() + "  CNY"
                conversionFive = (conversionRate_krwusd/conversionRate_jpyusd).toString() + "  JPY"
                conversionSix = (conversionRate_krwusd/conversionRate_eurusd).toString() + "  EUR"
            }
            else -> return
        }
    }

}