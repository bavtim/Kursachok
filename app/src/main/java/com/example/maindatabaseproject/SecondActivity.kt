package com.example.maindatabaseproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.maindatabaseproject.databinding.ActivitySecondBinding
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject
const val API_KEY = "23f203ae0b2f42c3a86121922222411"
class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = FirebaseAuth.getInstance()

        if (user.currentUser != null) {
            user.currentUser?.let {

                binding.tvUserEmail.text = it.email
            }
        }

        binding.getWeather.setOnClickListener {
            var city = binding.idCity.text.toString()
            getResult(city)
        }
        //Кнопочка на настройки
        binding.toSetting.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
            finish()
        }
        //Кнопочка на мониторинг
        binding.toMonitoring.setOnClickListener {
            startActivity(Intent(this, Monitoring::class.java))
            finish()
        }
        binding.btnSignOut.setOnClickListener {
            user.signOut()
            startActivity(
                Intent(
                    this, MainActivity::class.java
                )
            )
            finish()
        }
    }
    private fun getResult(city: String){
        val url = "https://api.weatherapi.com/v1/current.json" +
                "?key=$API_KEY&q=$city&aqi=no"
        val queue = Volley.newRequestQueue(this)
        val request = StringRequest(
            Request.Method.GET,
            url,
            {
                    result-> parseWeatherData(result)
            },
            {
                    error -> Log.d("MyLog","Error: $error")
            }
        )
        queue.add(request)
    }
    private fun parseWeatherData(result: String) {
        val mainObject = JSONObject(result)
        val item = WeatherModel(
            mainObject.getJSONObject("location").getString("name"),
            mainObject.getJSONObject("current").getString("last_updated"),
            mainObject.getJSONObject("current")
                .getJSONObject("condition").getString("text"),
            mainObject.getJSONObject("current").getString("temp_c"),
            "",
            "",
            mainObject.getJSONObject("current")
                .getJSONObject("condition").getString("icon"),
            ""
        )
        binding.tvTemp.text = item.currentTemp
        binding.tvCity.text = item.city
        binding.tvCondition.text = item.condition
        binding.tvDate.text = item.time
        Log.d("MyLog", "City: ${item.city}")
        Log.d("MyLog", "Time: ${item.time}")
        Log.d("MyLog", "Condition: ${item.condition}")
        Log.d("MyLog", "Temp: ${item.currentTemp}")
        Log.d("MyLog", "Url: ${item.imageUrl}")
        var test = binding.tvTemp.text.toString()
        test = item.currentTemp
    }
}