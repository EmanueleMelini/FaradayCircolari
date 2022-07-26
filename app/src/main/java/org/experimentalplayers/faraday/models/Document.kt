package org.experimentalplayers.faraday.models

import java.time.LocalDateTime

data class Document(
    var IDdocument: Int,
    var title: String,
    var description: String,
    var url: String,
    var date: LocalDateTime,
    var type: DocumentType,
)

enum class DocumentType {
    CIRCOLARE,
    AVVISO
}