package com.arunscs.docscanner.ui.screens.savedocument.navigation

import androidx.compose.runtime.getValue
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.dialog
import com.arunscs.docscanner.ui.navigation.Route
import com.arunscs.docscanner.ui.screens.savedocument.SaveDocumentDialogViewModel
import com.arunscs.docscanner.ui.screens.savedocument.SavePDFDialog
import kotlinx.serialization.Serializable
import androidx.core.net.toUri

@Serializable
data class SaveDocumentRoute(val documentPath:String,val documentImagePath:String) : Route

fun NavController.navigateToSaveDocumentDialog(documentPath:String,documentImagePath:String,navOptions: NavOptions) = this.navigate(SaveDocumentRoute(documentPath, documentImagePath), navOptions)

fun NavGraphBuilder.saveDocumentDialog(navController: NavController) {
    dialog<SaveDocumentRoute>(
        dialogProperties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {backStackEntry ->
        val documentUri = backStackEntry.arguments?.getString("documentPath")?.toUri()
        val documentImgUri = backStackEntry.arguments?.getString("documentImagePath")?.toUri()
        val saveDocumentVM = hiltViewModel<SaveDocumentDialogViewModel>()
        val uiState by saveDocumentVM.uiState.collectAsStateWithLifecycle()
        val title by saveDocumentVM.title.collectAsStateWithLifecycle()
        SavePDFDialog(
            onSave = { saveDir, contentResolver ->
                saveDocumentVM.saveDocument(

                    contentResolver = contentResolver,
                    documentUri = documentUri,
                    documentImgUri = documentImgUri,
                    saveDir = saveDir,
                )
            },
            uiState = uiState,
            title = title,
            onDismiss = {
                navController.popBackStack()
            },
            onTitleChange = saveDocumentVM::updateDocumentTitle
        )
    }
}