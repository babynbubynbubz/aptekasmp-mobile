package com.example.aptekaspm_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.aptekaspm_mobile.ui.navigation.AppNavigation
import com.example.aptekaspm_mobile.ui.theme.AptekaspmmobileTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AptekaspmmobileTheme {
                AppNavigation()
            }
        }
    }
}
