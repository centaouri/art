package com.example.art

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val barChart: BarChart = findViewById(R.id.barChart)

        val entries1 = arrayListOf<BarEntry>()
        entries1.add(BarEntry(0f, 70f))
        entries1.add(BarEntry(1f, 0f))
        entries1.add(BarEntry(2f, 0f))

        val entries2 = arrayListOf<BarEntry>()
        entries2.add(BarEntry(0f, 0f))
        entries2.add(BarEntry(1f, 100f))
        entries2.add(BarEntry(2f, 0f))

        val entries3 = arrayListOf<BarEntry>()
        entries3.add(BarEntry(0f, 0f))
        entries3.add(BarEntry(1f, 0f))
        entries3.add(BarEntry(2f, 50f))

        val barDataSet1 = BarDataSet(entries1, "")
        val barDataSet2 = BarDataSet(entries2, "")
        val barDataSet3 = BarDataSet(entries3, "")

        barDataSet1.color = Color.rgb(50, 60, 47) // metal
        barDataSet2.color = Color.rgb(50, 60, 47) // glass
        barDataSet3.color = Color.rgb(50, 60, 47) // plastic

        val barData = BarData(barDataSet1, barDataSet2, barDataSet3)

        // 값 텍스트 크기 조절
        barDataSet1.setValueTextSize(15f)
        barDataSet2.setValueTextSize(15f)
        barDataSet3.setValueTextSize(15f)

        barChart.description.isEnabled = false // Description 비활성화

        barChart.data = barData

        // x축의 라벨 숨기기
        barChart.xAxis.isEnabled = false

        // 왼쪽 축의 라벨 숨기기
        barChart.axisLeft.isEnabled = false

        // 오른쪽 축의 라벨 숨기기
        barChart.axisRight.isEnabled = false

        barChart.setDrawGridBackground(false) // 바차트의 배경 격자 무늬 없애기

        barChart.legend.isEnabled = false // 범례(legend) 비활성화

        // 선택적으로 추가적인 설정 가능
        barChart.setFitBars(true) // 막대 사이 간격 조정
        barChart.invalidate() // 그래프 업데이트

        // x 축의 값 표시 간격 설정
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(arrayOf("", "", ""))

        // 막대 아래에 값 표시 설정
        barDataSet1.setDrawValues(true)
        barDataSet2.setDrawValues(true)
        barDataSet3.setDrawValues(true)

        barChart.axisLeft.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return if (value == 0f) "" else value.toInt().toString()
            }
        }
    }
}












