package com.arunscs.docscanner.ui.screens.dashboard

import android.app.Activity.RESULT_OK
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.arunscs.docscanner.R
import com.arunscs.docscanner.domain.entity.Document
import com.arunscs.docscanner.ui.theme.DocScannerTheme
import com.arunscs.docscanner.ui.util.FileUtil
import com.arunscs.docscanner.ui.util.UiDateUtil
import com.arunscs.docscanner.ui.util.getActivity
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import java.io.File
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    onClickDocItem:(docId:String)->Unit,
    navigateToSaveDocument: (documentPath: String,documentImagePath:String) -> Unit,
) {

    val context = LocalContext.current
    val options = GmsDocumentScannerOptions.Builder()
        .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
        .setGalleryImportAllowed(true)
        .setResultFormats(RESULT_FORMAT_PDF, RESULT_FORMAT_JPEG)
        .build()

    val scanner = GmsDocumentScanning.getClient(options)

    val scannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                val result = GmsDocumentScanningResult.fromActivityResultIntent(activityResult.data)
                navigateToSaveDocument(
                    result?.pdf?.uri.toString(),
                    result?.pages?.get(0)?.imageUri.toString()
                )
            }
        }
    )

    Scaffold(
        topBar = {
            LargeTopAppBar(
                modifier = Modifier
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
                title = {
                    Text(text= stringResource(R.string.hey_there_lets_to_scan),color=MaterialTheme.colorScheme.onPrimary)
                        },
                colors = topAppBarColors(containerColor = MaterialTheme.colorScheme.primary
        )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    context.getActivity()?.let {
                        scanner
                            .getStartScanIntent(it)
                            .addOnSuccessListener { intent ->
                                scannerLauncher.launch(IntentSenderRequest.Builder(intent)
                                    .build()
                                )
                            }
                            .addOnFailureListener { e ->
                                Log.e("DashboardScreen", "Error starting document scan", e)
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.failed_to_scan_document),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = stringResource(R.string.add_document))
            }
        }
    ){innerPadding->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        ) {
            when(uiState){
                is DashboardUiState.Loading -> Loading()
                is DashboardUiState.NoDocuments -> NoDocumentsAvailable()
                is DashboardUiState.DocumentsAvailable -> DocumentsList(uiState.documents,onClickDocItem)
                DashboardUiState.Error -> Error()
            }
        }
    }
}

@Composable
private fun BoxScope.NoDocumentsAvailable() {
    Text(
        text = stringResource(R.string.no_documents_available),
        modifier = Modifier
            .align(Alignment.Center)
    )
}

@Composable
private fun BoxScope.Error(){
    Text(
        text= stringResource(R.string.something_went_wrong),
        modifier = Modifier
            .align(Alignment.Center)
    )
}

@Composable
fun BoxScope.Loading() {
    CircularProgressIndicator(
        modifier = Modifier
            .align(Alignment.Center)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DocumentsList(
    documents:List<Document>,
    onClickDocItem: (docId: String) -> Unit
) {
    val lazyListState = rememberLazyListState()


    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
    ){
        stickyHeader {
            StickyHeader(title = stringResource(R.string.your_documents))
        }
        items(documents){doc->
            DocumentListItem(doc){
                onClickDocItem(doc.id)
            }
        }
    }
}

@Composable
fun StickyHeader(title:String){
    //Create a sticky header with card and inside it a text showing a title, the card should have rounded edges
    Card(
        shape = RoundedCornerShape(4.dp),
        elevation= CardDefaults.cardElevation(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(vertical = 8.dp,horizontal = 4.dp)
        )
    }
}

@Composable
fun DocumentListItem(
    document:Document,
    onClickDocItem: (docId: String) -> Unit,
) {
    val context = LocalContext.current
   Card(
       shape = RoundedCornerShape(12.dp),
       elevation= CardDefaults.cardElevation(8.dp),
       modifier = Modifier
           .padding(
               horizontal = 24.dp,
               vertical = 12.dp
           )
           .fillMaxWidth()
           .clickable{
                onClickDocItem(document.id)
           }
           .wrapContentHeight()
   ) {
       Row(
           verticalAlignment = Alignment.CenterVertically,
           modifier = Modifier.height(height = 82.dp)
       ){
            AsyncImage(
                model = File(FileUtil.getDocumentImgPath(document.title, context = context)).toUri(),
                contentDescription = "Document Icon",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(4.dp)
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp))
                )
           Column(
               verticalArrangement = Arrangement.Top,
               modifier = Modifier
                   .padding(top = 16.dp, start = 12.dp)
                   .fillMaxHeight()
           ) {
               Text(
                   text = document.title,
                   style = MaterialTheme.typography.bodyMedium,
                   maxLines = 1,
                   overflow = TextOverflow.Ellipsis
               )
               Spacer(modifier =Modifier.height(8.dp))
               Text(
                   text = UiDateUtil.formatDateTimeToString(document.createdOn),
                   style = MaterialTheme.typography.bodySmall,
                   fontWeight = FontWeight.Thin,
               )
           }
       }
   }
}


@Preview(showBackground = true)
@Composable
fun DashboardScreen_DocumentsAvailable_Preview() {
    DocScannerTheme {
        DashboardScreen(
            DashboardUiState.DocumentsAvailable(
                listOf(
                    Document(
                        id = "1",
                        title = "Document 1",
                        createdOn = LocalDateTime.now(),
                    ),
                    Document(
                        id = "2",
                        title = "Document 2",
                        createdOn = LocalDateTime.now(),
                    ),
                    Document(
                        id = "3",
                        title = "Document 3",
                        createdOn = LocalDateTime.now(),
                    ),
                    Document(
                        id = "4",
                        title = "Document 4",
                        createdOn = LocalDateTime.now(),
                    ),
                    Document(
                        id = "5",
                        title = "Document 5",
                        createdOn = LocalDateTime.now(),
                    ),
                    Document(
                        id = "6",
                        title = "Document 6",
                        createdOn = LocalDateTime.now(),
                    ),
                    Document(
                        id = "7",
                        title = "Document 7",
                        createdOn = LocalDateTime.now(),
                    ),
                    Document(
                        id = "8",
                        title = "Document 8",
                        createdOn = LocalDateTime.now(),
                    )
                )
            ),
            navigateToSaveDocument = {_,_->},
            onClickDocItem = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreen_Loading_Preview() {
    DocScannerTheme {
        DashboardScreen(
            DashboardUiState.Loading,
            navigateToSaveDocument = {_,_->},
            onClickDocItem = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreen_Error_Preview() {
    DocScannerTheme {
        DashboardScreen(
            DashboardUiState.Error,
            navigateToSaveDocument = {_,_->},
            onClickDocItem = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreen_NoDocuments_Preview() {
    DocScannerTheme {
        DashboardScreen(
            DashboardUiState.NoDocuments,
            navigateToSaveDocument = { _, _ -> },
            onClickDocItem = {}
        )
    }
}