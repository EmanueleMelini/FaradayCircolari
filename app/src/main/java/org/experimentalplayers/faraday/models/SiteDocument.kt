package org.experimentalplayers.faraday.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

class SiteDocument() {
    lateinit var attachments: ArrayList<DocumentReference>
    lateinit var category: String
    lateinit var id: String
    lateinit var pageUrl: String
    lateinit var publishDate: Timestamp
    lateinit var snippet: String
    lateinit var title: String
    lateinit var description: String
    lateinit var type: DocumentType

    enum class DocumentType {
        CIRCOLARE,
        AVVISO,
        UNKNOWN
    }

}