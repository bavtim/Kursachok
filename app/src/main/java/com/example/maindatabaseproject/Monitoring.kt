package com.example.maindatabaseproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.maindatabaseproject.databinding.ActivityMainBinding
import com.example.maindatabaseproject.databinding.ActivityMonitoringBinding
import com.example.maindatabaseproject.databinding.ActivitySecondBinding
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Data(
    var date: String = "",
    var time: String = "",
    var temperature: Double = 0.0,
    var humidity: Double = 0.0
)

class DateIterator(
    val startDate: LocalDate,
    val endDateInclusive: LocalDate,
    val stepDays: Long
) : Iterator<LocalDate> {
    private var currentDate = startDate

    override fun hasNext() = currentDate <= endDateInclusive

    override fun next(): LocalDate {

        val next = currentDate

        currentDate = currentDate.plusDays(stepDays)

        return next

    }

}

class DateProgression(
    override val start: LocalDate,
    override val endInclusive: LocalDate,
    val stepDays: Long = 1
) :
    Iterable<LocalDate>, ClosedRange<LocalDate> {

    override fun iterator(): Iterator<LocalDate> =
        DateIterator(start, endInclusive, stepDays)

    infix fun step(days: Long) = DateProgression(start, endInclusive, days)

}

operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)


public object Store {
    val dataList = ArrayList<Data>()
    public var startDate = "";
    public var endDate = "";
    public var valTemp = 99;
    public var valHumid = 99;
    public var selectedOperator1 = 3;
    public var selectedOperator2 = 3;

    public fun fillData2() { //ГЕНЕРАЦИЯ ТЕСТОВЫХ ДАННЫХ!!! ЗАМЕНИТЬ
        for (i in 10..30) {
            dataList.add(
                Data(
                    temperature = i.toDouble(),
                    humidity = i.toDouble(),
                    date = "${i}.11.2022",
                    time = "time_$i"
                )
            )
        }
    }

    public fun filter() {
        filterByRange()
        filterByTemp()
        filterByHumid()
    }

    public fun filterByDay() {
        if (startDate != "") {
            val removalList = ArrayList<Data>()
            for (data in dataList) {
                if (data.date != startDate) {
                    removalList.add(data)
                }
            }
            dataList.removeAll(removalList.toSet())
        }
    }

    public fun filterByTemp() {
        if (valTemp != 99) {
            val removalList = ArrayList<Data>()
            if (selectedOperator1 == 0) {
                for (data in dataList) {
                    if (data.temperature != valTemp.toDouble()) {
                        removalList.add(data)
                    }
                }
            } else if (selectedOperator1 == 1) {
                for (data in dataList) {
                    if (data.temperature <= valTemp.toDouble()) {
                        removalList.add(data)
                    }
                }
            } else {
                for (data in dataList) {
                    if (data.temperature >= valTemp.toDouble()) {
                        removalList.add(data)
                    }
                }
            }
            dataList.removeAll(removalList.toSet())
        }
    }

    public fun filterByHumid() {
        if (valHumid != 99) {
            val removalList = ArrayList<Data>()
            if (selectedOperator2 == 0) {
                for (data in dataList) {
                    if (data.humidity != valHumid.toDouble()) {
                        removalList.add(data)
                    }
                }
            }
            if (selectedOperator2 == 1) {
                for (data in dataList) {
                    if (data.humidity <= valHumid.toDouble()) {
                        removalList.add(data)
                    }
                }
            }
            if (selectedOperator2 == 2) {
                for (data in dataList) {
                    if (data.humidity >= valHumid.toDouble()) {
                        removalList.add(data)
                    }
                }
            }
            dataList.removeAll(removalList.toSet())
        }
        println(valHumid)
    }

    public fun filterByRange() {
        if (startDate != "" && endDate != "") {
            val formatDate = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val sDate = LocalDate.parse(startDate, formatDate)
            val eDate = LocalDate.parse(endDate, formatDate)
            val removalList = ArrayList<Data>()
            val filterData = ArrayList<String>()
            for (date in sDate..eDate step 1) {
                filterData.add(date.format(formatDate))
                println(date.toString())
            }
            for (data in dataList) {
                if (!filterData.contains(data.date)) {
                    removalList.add(data)
                }
            }
            dataList.removeAll(removalList.toSet())

        } else filterByDay()
        println(endDate.toString())
    }

    public fun resetFilter() {
        startDate = "";
        endDate = "";
        valTemp = 99;
        valHumid = 99;
        selectedOperator1 = 3;
        selectedOperator2 = 3;
    }
}


class Monitoring : AppCompatActivity() {
    private fun mainEvent() {
        Store.dataList.clear()
        Store.fillData2()
        Store.filter()
        //Работаем с температурой
        val temperatureData = ArrayList<Double>()
        for (index in 0 until Store.dataList.size) {
            temperatureData.add(Store.dataList[index].temperature)
        }

        //Работаем с влажностью
        val humidityData = ArrayList<Double>()
        for (index in 0 until Store.dataList.size) {
            humidityData.add(Store.dataList[index].humidity)
        }

        //Горизонтальная ось графика
        val xAxisText = ArrayList<String>()
        for (index in 0 until Store.dataList.size) {
            xAxisText.add("${Store.dataList[index].date} ${Store.dataList[index].time}")
        }

        val aaChartView = findViewById<AAChartView>(R.id.aa_chart_view)
        val aaChartModel: AAChartModel = AAChartModel()
            .chartType(AAChartType.Area)
            .dataLabelsEnabled(true)
            .legendEnabled(false)
            .title("Влажность")
            .yAxisTitle("Влажность, %")
            .backgroundColor("#ffffff")
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Влажность")
                        .data(humidityData.toArray())
                        .color("#44BBCC"),
                ),
            ).categories(xAxisText.toArray(arrayOf(String())))

        val aaChartView2 = findViewById<AAChartView>(R.id.aa_chart_view2)
        val aaChartModel2: AAChartModel = AAChartModel()
            .chartType(AAChartType.Area)
            .dataLabelsEnabled(true)
            .legendEnabled(false)
            .yAxisTitle("Градусы °C")
            .title("Температура")
            .backgroundColor("#ffffff")
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Температура")
                        .data(temperatureData.toArray())
                        .color("#DDBB22"),
                ),
            ).categories(xAxisText.toArray(arrayOf(String())))

        aaChartView.aa_drawChartWithChartModel(aaChartModel)
        aaChartView2.aa_drawChartWithChartModel(aaChartModel2)
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitoring)
        val buttonFilter: FloatingActionButton = findViewById(R.id.floatingActionButton2)
        buttonFilter.setOnClickListener {
            val intent = Intent(this, PickFilterActivity::class.java)
            startActivity(intent)
        }
        val buttonSync: FloatingActionButton = findViewById(R.id.floatingActionButton1)
        buttonSync.setOnClickListener {
            Store.resetFilter()
            mainEvent()
        }
        val buttonback: Button = findViewById(R.id.backmainmenu)
        buttonback.setOnClickListener {

            startActivity(Intent(this, SecondActivity::class.java))
        }
        mainEvent()
    }
}