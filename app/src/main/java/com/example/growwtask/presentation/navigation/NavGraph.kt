package com.example.growwtask.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.growwtask.presentation.ui.recentlySearchedViewAllScreen.RecentlySearchedViewAllScreen
import com.example.growwtask.presentation.ui.exploreScreen.ExploreScreen
import com.example.growwtask.presentation.ui.productScreen.ProductScreen
import com.example.growwtask.presentation.ui.viewAllScreen.ViewAllScreen


@Composable
fun NavigationGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController, startDestination = Destinations.ExploreScreen.route
    ) {
        composable(route = Destinations.RecentlySearchedViewAllScreen.route) {
            RecentlySearchedViewAllScreen(navController = navController)
        }
        composable(Destinations.ExploreScreen.route) {
            ExploreScreen(
                navigateToViewAll = { type ->
                    navController.navigate(Destinations.ViewAllScreen.createRoute(type))
                },
                navController = navController
            )
        }
        composable(
            route = Destinations.ProductScreen.route,
            arguments = listOf(
                navArgument("symbol") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val symbol = backStackEntry.arguments?.getString("symbol")
            if (symbol != null) {
                ProductScreen(
                    symbol = symbol,
                )
            }
        }
        composable(
            route = Destinations.ViewAllScreen.route,
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type")
            if (type != null) {
                ViewAllScreen(
                    type = type,
                    navigateToProductScreen = { symbol ->
                        navController.navigate(Destinations.ProductScreen.createRoute(symbol))
                    }
                )
            }
        }
    }

}
