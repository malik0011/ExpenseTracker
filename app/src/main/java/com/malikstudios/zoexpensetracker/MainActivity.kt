package com.malikstudios.zoexpensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.navigation.findNavController
import com.malikstudios.zoexpensetracker.presentation.navigation.ExpenseNavGraph
import com.malikstudios.zoexpensetracker.ui.theme.ZoExpenseTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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