package com.arunscs.docscanner.ui.screens.viewdocument

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.core.graphics.createBitmap

class PdfToBitmapConverter(
    private val context:Context
) {

    private var renderer: PdfRenderer? = null

    suspend fun pdfToBitmaps(uri: Uri): List<Bitmap> {
        return withContext(Dispatchers.IO) {
            renderer?.close() //Close any already opened renderer

            context.contentResolver
                .openFileDescriptor(
                    uri,
                    "r"
                )?.use { fileDescriptor ->
                    with(PdfRenderer(fileDescriptor)) {
                        renderer = this

                        return@withContext (0 until pageCount).map { index ->
                            openPage(index).use{
                                    page->
                                val bitmap = createBitmap(page.width, page.height)

                                val canvas = Canvas(bitmap).apply {
                                    drawColor(Color.WHITE)
                                    drawBitmap(bitmap, 0f, 0f, null)
                                }

                                page.render(
                                    bitmap,
                                    null,
                                    null,
                                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                                )
                                bitmap
                            }
                        }
                    }
                }
            return@withContext emptyList<Bitmap>()
        }
    }
}