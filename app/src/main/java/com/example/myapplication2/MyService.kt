package com.example.myapplication2

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Handler
import android.os.Looper
import android.util.Log

class MyService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null  // В данном случае не используется связывание
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val n = intent?.getIntExtra("n", 1) ?: 1
        Log.d("MyService", "Started Fibonacci calculation for n=$n")

        // Запускаем вычисления в отдельном потоке, чтобы не блокировать основной поток
        Thread {
            val result = calculateFibonacci(n)
            Log.d("MyService", "Fibonacci result for n=$n: $result")

            // Отправляем результат в Activity через Broadcast
            val resultIntent = Intent(ACTION)
            resultIntent.putExtra("result", result.toString())
            resultIntent.setPackage("com.example.myapplication2")
            sendBroadcast(resultIntent)
        }.start()

        return START_NOT_STICKY
    }

    //TODO Тут надо испольщовать Bigint
    // (иначе переполнение будет слишком рано)
    private fun calculateFibonacci(n: Int): Long {
        var a = 0L
        var b = 1L
        for (i in 2..n) {
            val temp = a + b
            a = b
            b = temp
        }
        return b
    }

    companion object {
        //Чтоб не сравнивать строки - поместим в константу
        const val ACTION = "com.example.myapplication2.RESULT"
    }
}

// на github, подключить rscprof
