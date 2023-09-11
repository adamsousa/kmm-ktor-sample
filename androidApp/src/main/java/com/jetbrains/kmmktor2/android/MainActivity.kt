package com.jetbrains.kmmktor2.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jetbrains.kmmktor2.MainActivityViewModel
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    private val viewModel = MainActivityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv: TextView = findViewById(R.id.text_view)
        tv.text = "Loading..."

        Toast.makeText(this, "hello", Toast.LENGTH_LONG).show()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.greetingFlow.collect {
                    tv.text = it
                    Toast.makeText(this@MainActivity, "hello2", Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.fetchGreeting()
    }
}
