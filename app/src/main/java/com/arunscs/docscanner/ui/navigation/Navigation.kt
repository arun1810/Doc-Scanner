package com.arunscs.docscanner.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.arunscs.docscanner.ui.screens.dashboard.navigation.DashboardRoute
import com.arunscs.docscanner.ui.screens.dashboard.navigation.dashboardScreen
import com.arunscs.docscanner.ui.screens.savedocument.navigation.saveDocumentDialog
import com.arunscs.docscanner.ui.screens.viewdocument.navigation.viewDocumentScreen

interface Route

@Composable
fun DocScannerNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination =  DashboardRoute,
        modifier = modifier,
        enterTransition = {
            fadeIn(tween(700))
        },
        exitTransition = {
            fadeOut(tween(700))
        }
    ){
      dashboardScreen(navController)
      saveDocumentDialog(navController)
      viewDocumentScreen(navController)
    }
}