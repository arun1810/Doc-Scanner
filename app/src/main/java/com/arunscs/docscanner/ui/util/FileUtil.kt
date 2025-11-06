package com.arunscs.docscanner.ui.util


import android.content.Context


class FileUtil {

    companion object {
        fun getDocumentPath(title:String,context: Context): String {
            return "${context.filesDir}/$title/$title.pdf"
        }

        fun getDocumentImgPath(title:String,context: Context): String {
            return "${context.filesDir}/$title/$title.png"
        }
    }

}