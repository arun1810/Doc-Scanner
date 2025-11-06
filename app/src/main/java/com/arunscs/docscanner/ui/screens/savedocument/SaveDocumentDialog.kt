package com.arunscs.docscanner.ui.screens.savedocument

//Write a composable dialog that asks for a name for the document and a save button, the background must be a card

import android.content.ContentResolver
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arunscs.docscanner.R
import kotlinx.coroutines.delay
import java.io.File

@Composable
fun SavePDFDialog(
    title:String,
    onTitleChange:(String)->Unit,
    uiState: SaveDocumentUiState,
    onSave: (saveDir: File, contentResolver: ContentResolver) -> Unit,
    onDismiss: () -> Unit,
) {

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .heightIn(min=200.dp)
        ,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.save_document),
                style = MaterialTheme.typography.headlineSmall
            )

            when(uiState){
                SaveDocumentUiState.Error ->{
                    Error()
                }
                SaveDocumentUiState.Initialized -> {
                    Initialize(
                        onDismiss = onDismiss,
                        onSave = onSave,
                        onTitleChange = onTitleChange,
                        title = title
                    )
                }
                SaveDocumentUiState.SaveSuccess -> {
                    SaveSuccess()
                    LaunchedEffect(Unit){
                       delay(750)
                        onDismiss()
                    }
                }
                SaveDocumentUiState.SavingInProgress -> {
                    SaveInProgress()
                }
            }

        }
    }
}

@Composable
private fun Initialize(onDismiss: () -> Unit, onSave: (saveDir: File, contentResolver: ContentResolver) -> Unit, onTitleChange: (String) -> Unit, title: String){
    val context = LocalContext.current
    OutlinedTextField(
        value = title,
        onValueChange = { onTitleChange(it) },
        label = { Text(stringResource(R.string.document_name)) },
        modifier = Modifier.fillMaxWidth()
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { onDismiss() },
            modifier = Modifier.padding(end= 8.dp)
        ) {
            Text(stringResource(R.string.cancel))
        }
        Button(
            onClick = { onSave(context.filesDir,context.contentResolver) },
        ) {
            Text(stringResource(R.string.save))
        }
    }
}

@Composable
private fun Error(){
    Text(text = "An error occurred while saving the document.")
}

@Composable
private fun SaveInProgress(){
    CircularProgressIndicator()
}

@Composable
private fun SaveSuccess(){
    Text(text = "Document saved successfully.")
}