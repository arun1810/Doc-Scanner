package com.arunscs.docscanner.data.source

// Write a Document to LocalDocument mapper extension function
import com.arunscs.docscanner.data.local.LocalDocument
import com.arunscs.docscanner.domain.entity.Document

fun Document.toLocalModel() = LocalDocument(
    id = id,
    title = title,
    createdOn = createdOn,
)

fun LocalDocument.toDomainModel() = Document(
    id = id,
    title = title,
    createdOn = createdOn,
)

@JvmName("LocalDocumentListToDomainModel")
fun List<LocalDocument>.toDomainModel() = map(LocalDocument::toDomainModel)
