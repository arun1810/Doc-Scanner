package com.arunscs.docscanner.domain.entity

import java.time.LocalDateTime

data class Document(
    val id:String,
    val title:String,
    val createdOn:LocalDateTime,
)
