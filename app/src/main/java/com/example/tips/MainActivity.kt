package com.example.tips

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tips.ui.theme.TipsTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Button
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

val InputFieldColor = Color(0xFFF6C6D9)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TipCalculator(innerPadding = Modifier.padding(innerPadding))

                }
            }
        }

    }

}

fun calculateTip(orderAmount: Double, tipPercent: Int): Double {
    val tip = orderAmount * tipPercent / 100.0
    return tip
}

fun calculateDiscountPercent(dishCount: Int): Int {
    var discountPercent = 0
    if (dishCount in 1..2) {
        discountPercent = 3
    } else if (dishCount in 3..5) {
        discountPercent = 5
    } else if (dishCount in 6..10) {
        discountPercent = 7
    } else if (dishCount > 10) {
        discountPercent = 10
    }
    return discountPercent
}

fun calculateDiscountAmount(orderAmount: Double, discountPercent: Int): Double {
    val discountAmount = orderAmount * discountPercent / 100.0
    return discountAmount
}

fun calculateTotal(orderAmount: Double, tip: Double, discountAmount: Double): Double {
    val total = orderAmount + tip - discountAmount
    return total
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipCalculator(innerPadding: Modifier) {
    var orderAmountText by remember { mutableStateOf("") }
    var dishCountText by remember { mutableStateOf("") }
    var tipPercent by remember { mutableStateOf(0f) }
    var showTotalInsteadOfDiscount by remember { mutableStateOf(false) }

    // ===== Snackbar + Coroutine =====
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // ===== Вычисляемые переменные =====
    var orderAmount = orderAmountText.toDoubleOrNull()
    if (orderAmount == null) {
        orderAmount = 0.0
    }

    var dishCount = dishCountText.toIntOrNull()
    if (dishCount == null) {
        dishCount = 0
    }

    val discountPercent = calculateDiscountPercent(dishCount)
    val discountAmount = calculateDiscountAmount(orderAmount, discountPercent)
    val tipAmount = calculateTip(orderAmount, tipPercent.toInt())
    val totalAmount = calculateTotal(orderAmount, tipAmount, discountAmount)

    val pinkFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = InputFieldColor,
        unfocusedContainerColor = InputFieldColor,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent
    )

    Column(modifier = innerPadding.fillMaxSize().padding(16.dp)) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Сумма заказа:")
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = orderAmountText,
                onValueChange = { orderAmountText = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = pinkFieldColors,
                shape = RoundedCornerShape(6.dp),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                modifier = Modifier
                    .width(140.dp)
                    .height(52.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Количество блюд:")
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = dishCountText,
                onValueChange = { dishCountText = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = pinkFieldColors,
                shape = RoundedCornerShape(6.dp),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                modifier = Modifier
                    .width(80.dp)
                    .height(52.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Чаевые:")

        Slider(
            value = tipPercent,
            onValueChange = { newValue ->
                tipPercent = newValue
                val currentTip = calculateTip(orderAmount, newValue.toInt())
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(message = "Чаевые: ${currentTip}")
                }
            },
            valueRange = 0f..25f,
            steps = 4
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "0")
            Text(text = "25")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Скидка:")
            Spacer(modifier = Modifier.width(8.dp))

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    RadioButton(selected = discountPercent == 3, onClick = { })
                    Text(text = "3%")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    RadioButton(selected = discountPercent == 5, onClick = { })
                    Text(text = "5%")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    RadioButton(selected = discountPercent == 7, onClick = { })
                    Text(text = "7%")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    RadioButton(selected = discountPercent == 10, onClick = { })
                    Text(text = "10%")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (showTotalInsteadOfDiscount) {
            Text(text = "Итого:")
            TextField(
                value = totalAmount.toString(),
                onValueChange = { },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text(text = "Сумма скидки:")
            TextField(
                value = discountAmount.toString(),
                onValueChange = { },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(
            onClick = { showTotalInsteadOfDiscount = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Итого")
        }

        SnackbarHost(hostState = snackbarHostState) { data ->
            Snackbar {
                Text(data.visuals.message)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TipCalculatorScreenPreview() {
    TipsTheme {
        TipCalculator(innerPadding = Modifier)
    }
}