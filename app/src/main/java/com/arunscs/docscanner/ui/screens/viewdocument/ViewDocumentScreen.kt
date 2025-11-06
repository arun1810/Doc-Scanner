package com.arunscs.docscanner.ui.screens.viewdocument

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.arunscs.docscanner.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//Write a composable screen to display the bitmaps in a lazy list
fun ViewDocumentScreen(
    getDocument:(applicationContext: Context)->Unit,
    uiState: ViewDocumentUiState,
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        getDocument(context.applicationContext)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if(uiState is ViewDocumentUiState.Success) {
                        "${uiState.title}.pdf"
                    } else {
                        ""
                    })
                }
            )
        }
    ) {innerPadding->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ){

            when(uiState){
                ViewDocumentUiState.Error -> {
                    Error()
                }
                ViewDocumentUiState.Loading -> {
                    Loading()
                }
                is ViewDocumentUiState.Success ->{
                    Success(uiState.bitmaps)
                }
            }
        }
    }
}

@Composable
private fun Success(bitmaps:List<Bitmap>){
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ){
        items(bitmaps) { bitmap ->
            AsyncImage(
                model= bitmap,
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .aspectRatio(bitmap.width.toFloat() / bitmap.height.toFloat())
            )
        }
    }
}

//Create a error and loading composable screens
@Composable
private fun BoxScope.Error() {
    Text(
        stringResource(R.string.something_went_wrong),
        modifier = Modifier.align(Alignment.Center)
    )
}

@Composable
private fun BoxScope.Loading() {
    CircularProgressIndicator(
        modifier = Modifier.align(Alignment.Center)
    )
}
