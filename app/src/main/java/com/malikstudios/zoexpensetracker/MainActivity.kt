package com.malikstudios.zoexpensetracker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.malikstudios.zoexpensetracker.presentation.navigation.ExpenseNavGraph
import com.malikstudios.zoexpensetracker.ui.theme.ZoExpenseTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted
        } else {
            // Permission denied
        }
    }
    
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        
        setContent {
            val navController = androidx.navigation.compose.rememberNavController()
            ZoExpenseTrackerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
//                    topBar = {
//                        TopAppBar(
//                            title = { Text("Home") },
//                            actions = { /* Icons */ },
//                            modifier = Modifier.statusBarsPadding() // <-- this adds safe space
//                        )
//                    }
                ) { innerPadding ->
//                    Box(
//                        modifier = Modifier
//                            .padding(top = innerPadding.calculateTopPadding())
//                            .statusBarsPadding()
//                            .fillMaxSize()
//                    ) {
                        ExpenseNavGraph(
                            innerPadding = innerPadding,
                            navController = navController
                        )
//                    }
                }
            }
        }
    }
}