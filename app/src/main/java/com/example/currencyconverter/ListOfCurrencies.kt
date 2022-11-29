package com.example.currencyconverter

import android.R
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.currencyconverter.databinding.ActivityListOfCurrenciesBinding
import java.util.ArrayList

class ListOfCurrencies : AppCompatActivity() {
    lateinit var binding: ActivityListOfCurrenciesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOfCurrenciesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val values: ArrayList<String>? = intent.getStringArrayListExtra("currencies")

        val currenciesListView = binding.listViewCurrencies

        val adapter = ArrayAdapter(this,
            R.layout.simple_list_item_1,
            values ?: emptyList()
        )

       currenciesListView.adapter =adapter

        currenciesListView.setOnItemClickListener { parent, view, position, id ->
            val selectedCurrency = adapter.getItem(position) as String
            if (intent.getStringExtra("where").equals("rates")){
                //val intent = Intent(this@ListOfCurrencies, ConversionRatesList::class.java)
                val intent = Intent()
                intent.putExtra("selected", selectedCurrency)
                setResult(Activity.RESULT_OK, intent);
                finish()
            // resultLauncher.launch(intent)

            }
            else if (intent.getStringExtra("where").equals("to")){
                val intent = Intent()
                //val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("from", intent.getStringExtra("from"))
                intent.putExtra("to", selectedCurrency)
                setResult(Activity.RESULT_OK, intent);
                finish()

            }
            else if (intent.getStringExtra("where").equals("from")){
                val intent = Intent()
                intent.putExtra("from", selectedCurrency)
                intent.putExtra("to", intent.getStringExtra("to"))
                setResult(Activity.RESULT_OK, intent);
                finish()
//                val intent = Intent(this, MainActivity::class.java)
//                intent.putExtra("from", selectedCurrency)
//                intent.putExtra("to", intent.getStringExtra("to"))
//                resultLauncher.launch(intent)
            }
            else {
                val intent = Intent(this@ListOfCurrencies, MainActivity::class.java)
            }
        }

    }
}