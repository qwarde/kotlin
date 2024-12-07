package com.example.myapplication2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.myapplication2.ui.theme.MyApplication2Theme

class MainActivity : ComponentActivity() {

    private val receiver = ResultReceiver()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplication2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = { startFibonacciComputation() }) {
                            Text(text = "Start Fibonacci Calculation")
                        }
                    }
                }
            }
        }

        val action = MyService.ACTION
        val filter = IntentFilter(action)
        ContextCompat.registerReceiver(this,receiver, filter,
                ContextCompat.RECEIVER_NOT_EXPORTED)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun startFibonacciComputation() {
        val intent = Intent(this, MyService::class.java)
        intent.putExtra("n", 100)  // Параметр для вычисления
        startService(intent)
    }

    private inner class ResultReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val result = intent.getStringExtra("result")
            Log.d("MainActivity", "Fibonacci result $result")
            Toast.makeText(context, "Calculation Complete: $result", Toast.LENGTH_LONG).show()

            // Добавление кода для отправки результата в другое приложение
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Fibonacci result: $result")
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Share Fibonacci result"))
        }
    }
}
