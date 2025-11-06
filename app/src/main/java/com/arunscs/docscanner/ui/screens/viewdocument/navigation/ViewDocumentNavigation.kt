package com.arunscs.docscanner.ui.screens.viewdocument.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.arunscs.docscanner.ui.screens.dashboard.DashboardViewModel
import com.arunscs.docscanner.ui.navigation.Route
import com.arunscs.docscanner.ui.screens.viewdocument.ViewDocumentScreen
import com.arunscs.docscanner.ui.screens.viewdocument.ViewDocumentViewModel
import kotlinx.serialization.Serializable

@Serializable
data class ViewDocumentRoute(val documentId:String): Route

fun NavController.navigateToViewDocumentScreen(documentId:String,navOptions: NavOptions){
    this.navigate(ViewDocumentRoute(documentId),navOptions)
}

fun NavGraphBuilder.viewDocumentScreen(navController: NavController){
    composable<ViewDocumentRoute>{ backStackEntry->
        val docId = backStackEntry.arguments?.getString("documentId") ?: ""

        val viewDocumentVM: ViewDocumentViewModel = hiltViewModel<ViewDocumentViewModel>()
        val uiState by viewDocumentVM.uiState.collectAsStateWithLifecycle()

        ViewDocumentScreen(
            getDocument = {applicationCtx->
                viewDocumentVM.loadDocument(docId,applicationCtx)
            },
            uiState = uiState
        )
    }
}