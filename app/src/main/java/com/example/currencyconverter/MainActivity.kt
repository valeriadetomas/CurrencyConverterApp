package com.example.currencyconverter


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.NumberFormat
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.currencyconverter.databinding.ActivityMainBinding
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import java.util.*


class MainActivity : AppCompatActivity() {

    var fromCurrency = "EUR"
    var toCurrency = "EUR"
    var conversionRate = 0f
    var amount = "10"
    var currentLocation: Location? = null
    var listCurrency: ArrayList<String> = arrayListOf("EUR", "GBP", "USD")
    var resultRates: WorldCurrency? = null

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    // LocationRequest - setters location updates
    private lateinit var locationRequest: LocationRequest
    // LocationCallback - it is called when fused location has a new location
    var locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            currentLocation = locationResult.lastLocation
        }
    }
    //when the second page comes back to the main one this one is called
    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationFunctionality()

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
    //it passes the list of currencies and the rates, which are the data got from the api
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_ratesList -> {
            val intent = Intent(this@MainActivity, ConversionRatesList::class.java)
            intent.putExtra("currencies", listCurrency)
            intent.putExtra("rates", resultRates!!.rates.values.toFloatArray())
            startActivity(intent)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    //set up button of change currencies
    private fun setupButton() {

        val buttonFrom: Button = binding.fromButton
        val buttonTo: Button = binding.toButton

        buttonFrom.text = fromCurrency
        buttonTo.text = toCurrency

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

        if (resultRates != null) {
            conversionRate = if (fromCurrency == "EUR") {
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

    //call to API through Retrofit, aunches coroutine to get the data
    private fun APICalls() {
        val ratesApi = RetrofitHelper.getInstance().create(RatesApi::class.java)
        // launching a new coroutine
        GlobalScope.launch(Dispatchers.IO) {

            resultRates = ratesApi.getData().body()

            val resultSymbols: SymbolsData? = ratesApi.getSymbols().body()
            if (resultSymbols != null) {
                listCurrency = resultSymbols.symbols.keys.toList() as ArrayList<String>
            }
        }
    }

    //gets the selection of the currency from intent
    private fun getSelection(data: Intent?) {
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

    //adds localization fucntionality, which lets us retrieve the current location fo the device
    private fun locationFunctionality(){
        //checks if location permissions are granted
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //initializes the location manager
            val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                //gets the last location
                val loc: Task<Location> = fusedLocationProviderClient.getLastLocation()

                loc.addOnSuccessListener { location ->
                    if (location != null) {
                        location?.let {
                            Geocoder(this)
                                .getAddress(
                                    it.latitude,
                                    it.longitude
                                ) { address: android.location.Address? ->
                                    if (address != null) {
                                            var locale = address.locale

                                            fromCurrency = if (VERSION.SDK_INT >= 24) android.icu.text.NumberFormat.getCurrencyInstance(locale).currency.toString()
                                                            else java.text.NumberFormat.getCurrencyInstance(address.locale).currency.toString()

                                            binding.fromButton.text = fromCurrency
                                    }
                                }
                        }
                    } else {
                        locationRequest =
                            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                                .setWaitForAccurateLocation(false)
                                .setMinUpdateIntervalMillis(500)
                                .setMaxUpdateDelayMillis(1000)
                                .build()

                        fusedLocationProviderClient.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.getMainLooper()
                        )
                    }
                }
            }
        }

    }

    @Suppress("DEPRECATION")
    fun Geocoder.getAddress(
        latitude: Double,
        longitude: Double,
        address: (android.location.Address?) -> Unit
    ) {

        if (Build.VERSION.SDK_INT >= 33) {
            getFromLocation(latitude, longitude, 1) { address(it.firstOrNull()) }
            return
        }

        try {
            address(getFromLocation(latitude, longitude, 1)?.firstOrNull())
        } catch(e: Exception) {
            //will catch if there is an internet problem
            address(null)
        }
    }
}

