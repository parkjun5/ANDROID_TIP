package com.example.tiptime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {
    private val mlToOz: Double = 0.033814
    private val ozToMl: Double = 29.5735
    private val tipResultData: Int = R.string.tip_amount
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.liquidChagerMl.addTextChangedListener(textWatcherML)
        binding.liquidChagerOz.addTextChangedListener(textWatcherOZ)

        binding.calculateButton.setOnClickListener { calculateTip() }
    }

    private fun calculateTip() {

        val stringInTextField = binding.costOfService.text.toString()
        val cost = stringInTextField.toDoubleOrNull()
        if (cost == null) {
            displayTip(0.0)
            return
        }

        val tipPercentage = when (binding.tipsOption.checkedRadioButtonId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }

        var tip = cost * tipPercentage

        if (binding.optionSwitch.isChecked) {
            tip = ceil(tip)
        }
        displayTip(tip)

    }

    private fun displayTip(tipValue: Double) {
        val formattedTip = NumberFormat.getCurrencyInstance().format(tipValue)
        binding.resultTextview.text = getString(tipResultData, formattedTip)
    }

    private fun calculateLiquid(calculateManual: Double) {

        var changedData: Double? = null

        when (calculateManual) {
            mlToOz -> {
                binding.liquidChagerOz.removeTextChangedListener(textWatcherOZ)
                changedData = binding.liquidChagerMl.text.toString().toDoubleOrNull()
            }
            else -> {
                binding.liquidChagerMl.removeTextChangedListener(textWatcherML)
                changedData =binding.liquidChagerOz.text.toString().toDoubleOrNull()
            }
        }

        if (changedData == null) {
            binding.liquidChagerMl.setText("0")
            binding.liquidChagerOz.setText("0")
            return
        }
        val resultFormatted = DecimalFormat("#####.###")

        when (calculateManual) {
            mlToOz -> {
                binding.liquidChagerOz.setText(resultFormatted.format(changedData * calculateManual))
                binding.liquidChagerOz.addTextChangedListener(textWatcherOZ)
            }
            else -> {
                binding.liquidChagerMl.setText(resultFormatted.format(changedData * calculateManual))
                binding.liquidChagerMl.addTextChangedListener(textWatcherML)
            }
        }
    }

    private val textWatcherML = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {

            if (p0 != null && p0.toString() != "") {
                calculateLiquid(mlToOz)
            }
        }
    }

    private val textWatcherOZ = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {

            if (p0 != null && p0.toString() != "") {
                calculateLiquid(ozToMl)
            }
        }
    }

}


