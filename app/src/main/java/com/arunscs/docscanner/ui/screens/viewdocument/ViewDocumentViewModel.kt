package com.arunscs.docscanner.ui.screens.viewdocument

import android.content.Context
import android.graphics.Bitmap
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arunscs.docscanner.domain.repository.DocumentRepository
import com.arunscs.docscanner.ui.util.FileUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


//Write a sealed class ui states for loading, error, success with list of bitmaps
sealed class ViewDocumentUiState {
    data object Loading : ViewDocumentUiState()
    data class Success(val bitmaps: List<Bitmap>,val title:String) : ViewDocumentUiState()
    data object Error : ViewDocumentUiState()
}

@HiltViewModel
class ViewDocumentViewModel @Inject constructor(
    private val documentRepository: DocumentRepository
):ViewModel() {

    private val _uiState: MutableStateFlow<ViewDocumentUiState> = MutableStateFlow(ViewDocumentUiState.Loading)
    val uiState: StateFlow<ViewDocumentUiState> = _uiState.asStateFlow()

    //Write a meth that gets the document id as param and get the doc from repo
    fun loadDocument(documentId: String,applicationContext:Context) {
        viewModelScope.launch{
            _uiState.update{ ViewDocumentUiState.Loading}
            val pdfToBitmapConverter = PdfToBitmapConverter(applicationContext)
            val document = documentRepository.getDocumentById(documentId)
            if(document==null){
                _uiState.update{ ViewDocumentUiState.Error}
                return@launch
            }
            val bitmaps =  pdfToBitmapConverter.pdfToBitmaps(File(FileUtil.getDocumentPath(document.title,applicationContext)).toUri())
            _uiState.value = ViewDocumentUiState.Success(bitmaps,document.title)
        }

    }

}