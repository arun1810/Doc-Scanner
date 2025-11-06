package com.arunscs.docscanner.ui.screens.dashboard.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.arunscs.docscanner.ui.screens.dashboard.DashboardScreen
import com.arunscs.docscanner.ui.screens.dashboard.DashboardViewModel
import com.arunscs.docscanner.ui.navigation.Route
import com.arunscs.docscanner.ui.screens.savedocument.navigation.navigateToSaveDocumentDialog
import com.arunscs.docscanner.ui.screens.viewdocument.navigation.navigateToViewDocumentScreen
import kotlinx.serialization.Serializable

@Serializable
data object DashboardRoute: Route

fun NavController.navigateToDashboardScreen(navOptions: NavOptions) {
    this.navigate(DashboardRoute,navOptions)
}

fun NavGraphBuilder.dashboardScreen(navController: NavController){
    composable<DashboardRoute>{
        val dashboardVm = hiltViewModel<DashboardViewModel>()
        val uiState by dashboardVm.uiState.collectAsStateWithLifecycle()
        DashboardScreen(
            uiState = uiState,
            navigateToSaveDocument = { documentPath, documentImagePath ->
                navController.navigateToSaveDocumentDialog(
                    documentPath = documentPath,
                    documentImagePath = documentImagePath,
                    navOptions = navOptions { })
            },
            onClickDocItem = {
                navController.navigateToViewDocumentScreen(
                    documentId = it,
                    navOptions = navOptions {}
                )
            }
        )
    }
}

