package com.arunscs.docscanner.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arunscs.docscanner.domain.entity.Document
import com.arunscs.docscanner.domain.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data class DocumentsAvailable(val documents:List<Document>) : DashboardUiState
    data object NoDocuments : DashboardUiState
    data object Error : DashboardUiState
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val documentRepository: DocumentRepository
): ViewModel() {

    private val _uiState: MutableStateFlow<DashboardUiState> = MutableStateFlow(DashboardUiState.Loading)
    val uiState: MutableStateFlow<DashboardUiState> = _uiState

    init {
        viewModelScope.launch {
            fetchDocuments()
        }
    }

    private suspend fun fetchDocuments() {
        _uiState.update {
            DashboardUiState.Loading
        }
        try {
            documentRepository.getAllDocuments().collect{ documents->
                if(documents.isEmpty()){
                    _uiState.update {
                        DashboardUiState.NoDocuments
                    }
                }else{
                    _uiState.update {
                        DashboardUiState.DocumentsAvailable(documents)
                    }
                }
            }
        } catch (e: Exception) {
            _uiState.update {
                DashboardUiState.Error
            }
        }
    }
}
