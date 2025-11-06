package com.arunscs.docscanner.ui.screens.savedocument

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arunscs.docscanner.core.util.DateUtils
import com.arunscs.docscanner.domain.entity.Document
import com.arunscs.docscanner.domain.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject


//Write a sealed class for savePdf ui state containing Saving in progress, save success, Error, initialized

sealed class SaveDocumentUiState {
    data object Initialized : SaveDocumentUiState()
    data object SavingInProgress : SaveDocumentUiState()
    data object SaveSuccess : SaveDocumentUiState()
    data object Error : SaveDocumentUiState()
}

@HiltViewModel
class SaveDocumentDialogViewModel @Inject constructor(private val documentRepository: DocumentRepository):ViewModel(){

    private val _title:MutableStateFlow<String> = MutableStateFlow("")
    val title:StateFlow<String> = _title.asStateFlow()

    private val _uiState:MutableStateFlow<SaveDocumentUiState> = MutableStateFlow(SaveDocumentUiState.Initialized)
    val uiState:StateFlow<SaveDocumentUiState> = _uiState.asStateFlow()

    //Write a method to update the document
    fun updateDocumentTitle(name:String){
        _title.update {
            name
        }
    }

    //Write a method to save the document
    fun saveDocument(documentUri: Uri?, documentImgUri:Uri?, saveDir:File, contentResolver: ContentResolver){
        if(documentUri==null || documentImgUri==null){
            _uiState.update {
                SaveDocumentUiState.Error
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                SaveDocumentUiState.SavingInProgress
            }
            val documentName = if(_title.value.isEmpty() || _title.value.isBlank()){ "Document_${DateUtils.convertDateTimeToString(LocalDateTime.now())}" } else _title.value
            val currSaveDir = File(saveDir, documentName)
            if(!currSaveDir.exists()){
                currSaveDir.mkdirs()
            }
            savePdf(currSaveDir,documentName,contentResolver,documentUri)
            saveImg(currSaveDir,documentName,contentResolver,documentImgUri)

            val document = Document(
                id = UUID.randomUUID().toString(),
                title = documentName,
                createdOn = LocalDateTime.now(),
            )
            documentRepository.addDocument(document)
            _uiState.update {
                SaveDocumentUiState.SaveSuccess
            }
        }
    }

    private fun savePdf(saveDir:File,documentName:String,contentResolver: ContentResolver,documentPath:Uri){
        val fos = FileOutputStream(
            File(saveDir, "$documentName.pdf")
        )
        contentResolver.openInputStream(documentPath)?.use { inputStream ->
            inputStream.copyTo(fos)
        }
    }

    private fun saveImg(saveDir:File,documentName:String,contentResolver: ContentResolver,documentPath:Uri){

        val fos = FileOutputStream(
            File(saveDir, "$documentName.png")
        )
        contentResolver.openInputStream(documentPath)?.use { inputStream ->
            inputStream.copyTo(fos)
        }
    }
}