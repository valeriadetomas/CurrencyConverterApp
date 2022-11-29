package com.example.currencyconverter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.currencyconverter.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    var fromCurrency = "EUR"
    var toCurrency = "USD"
    var conversionRate = 0f
    var amount = "10"
    var listCurrency: ArrayList<String> = arrayListOf("EUR", "GBP", "USD")
    var resultRates: WorldCurrency? = null

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            getSelection(data)
        }
    }
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        APICalls()
        setupButton()

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
            val buttonFrom: Button = binding.fromButton
            val buttonTo: Button = binding.toButton
            buttonTo.text = fromCurrency
            buttonFrom.text = toCurrency
            fromCurrency = buttonFrom.text.toString()
            toCurrency = buttonTo.text.toString()
            convertCurrency()
        }
    }

    //adds items to the action bar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    //control the toolbar, used to change from one page to the other one
    override fun onOptionsItemSelected(item: MenuItem) =  when (item.itemId) {
            R.id.action_ratesList -> {
                val intent = Intent(this@MainActivity, ConversionRatesList::class.java)
                intent.putExtra("currencies", listCurrency)
                intent.putExtra("rates", resultRates!!.rates.values.toFloatArray())
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
    }

    //set up spinner of currencies
    private fun setupButton() {
        var locale: Locale = Locale.getDefault()
        fromCurrency = NumberFormat.getCurrencyInstance(locale).currency.toString()

        val buttonFrom: Button = binding.fromButton
        val buttonTo: Button = binding.toButton

        buttonFrom.text = fromCurrency
        buttonTo.text = toCurrency

        //add listener to button to swap currencies
        binding.fromButton.setOnClickListener {
            val intent = Intent(this@MainActivity, ListOfCurrencies::class.java)
            intent.putExtra("currencies", listCurrency)
            intent.putExtra("where", "from")
            resultLauncher.launch(intent)
        }

        binding.toButton.setOnClickListener {
            val intent = Intent(this@MainActivity, ListOfCurrencies::class.java)
            intent.putExtra("currencies", listCurrency)
            intent.putExtra("where", "to")
            resultLauncher.launch(intent)
        }
    }

    //converts and shows converted value
    private fun convertCurrency() {

        if (resultRates != null){
            conversionRate = if (fromCurrency.equals("EUR")){
                resultRates!!.rates.get(toCurrency)!!
            } else {
                resultRates!!.rates.get(toCurrency)!! / resultRates!!.rates.get(fromCurrency)!!
            }
        }

        amount = binding.editFromConversion.text.toString()
        if (binding.editFromConversion.text.isNotEmpty() && binding.editFromConversion.text.isNotBlank()) {
            if (fromCurrency == toCurrency) {
                binding.editToConversion.setText(amount)
            } else {
                val text = (amount.toFloat() * conversionRate).toString()
                binding.editToConversion.setText(text)
            }
        }
    }

    private fun APICalls() {
        val ratesApi = RetrofitHelper.getInstance().create(RatesApi::class.java)
        // launching a new coroutine
        GlobalScope.launch(Dispatchers.IO){

            resultRates = ratesApi.getData().body()

            val resultSymbols: SymbolsData? = ratesApi.getSymbols().body()
            if (resultSymbols != null) {
                listCurrency = resultSymbols.symbols.keys.toList() as ArrayList<String>
                Log.d("Main: ", listCurrency.toString())
            }

        }
    }

    private fun getSelection(data: Intent?){
        val fromButton: Button = binding.fromButton
        val toButton: Button = binding.toButton

        if (data != null) {
            if (data.getStringExtra("from") != null) {
                fromCurrency = data.getStringExtra("from").toString()
                fromButton.text = fromCurrency
            }

            if (data.getStringExtra("to") != null) {
                toCurrency = data.getStringExtra("to").toString()
                toButton.text = toCurrency
            }
            convertCurrency()
        }

    }
}

