package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.AppDatabase
import com.example.data.AppRepository
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.screens.CustomerLayout
import com.example.ui.screens.AdminPortalScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. Initialize local reactive Room structures
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = AppRepository(database)
        
        // 2. Initialize unified MainViewModel leveraging safe Factory method
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return MainViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        // 3. Enable edge-to-edge full screen bleed
        enableEdgeToEdge()
        
        setContent {
            MyApplicationTheme {
                val currentScreen by viewModel.currentScreen.collectAsState()

                // Register native android physical gestures/trackpad swipe back handle
                BackHandler(enabled = true) {
                    if (currentScreen is Screen.Home) {
                        finish()
                    } else {
                        val navigatedBack = viewModel.navigateBack()
                        if (!navigatedBack) {
                            finish()
                        }
                    }
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    when (currentScreen) {
                        is Screen.AdminLogin, is Screen.AdminDashboard -> {
                            AdminPortalScreen(viewModel = viewModel)
                        }
                        else -> {
                            CustomerLayout(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}
