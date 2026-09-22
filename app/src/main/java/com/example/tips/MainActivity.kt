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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipCalculator(innerPadding: Modifier) {
    var orderAmountText by remember { mutableStateOf("") }
    var dishCountText by remember { mutableStateOf("") }
    var tipPercent by remember { mutableStateOf(0f) }
    var showTotalInsteadOfDiscount by remember { mutableStateOf(false) }

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
            onValueChange = { newValue -> tipPercent = newValue },
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
                    RadioButton(selected = false, onClick = { })
                    Text(text = "3%")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    RadioButton(selected = false, onClick = { })
                    Text(text = "5%")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    RadioButton(selected = false, onClick = { })
                    Text(text = "7%")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    RadioButton(selected = false, onClick = { })
                    Text(text = "10%")
                }
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