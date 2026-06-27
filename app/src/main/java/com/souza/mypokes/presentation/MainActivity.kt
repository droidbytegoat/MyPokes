package com.souza.mypokes.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.souza.mypokes.presentation.navigation.AppNavGraph
import com.souza.mypokes.presentation.navigation.BottomNavBar
import com.souza.mypokes.presentation.navigation.Screen
import com.souza.mypokes.presentation.theme.MyPokesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { viewModel.isLoading.value }

        enableEdgeToEdge()

        setContent {
            val theme by viewModel.theme.collectAsStateWithLifecycle()
            val navController = rememberNavController()

            MyPokesTheme(appTheme = theme) {
                MainScaffold(navController = navController)
            }
        }
    }
}

@Composable
private fun MainScaffold(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomNavRoutes) {
                BottomNavBar(navController = navController)
            }
        },
    ) { paddingValues ->
        AppNavGraph(
            navController = navController,
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues),
        )
    }
}

private val bottomNavRoutes = setOf(
    Screen.PokemonList.route,
    Screen.Favorites.route,
    Screen.Settings.route,
)
