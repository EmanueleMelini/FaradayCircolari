package org.experimentalplayers.faraday.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

class SiteDocument() {

    var articleId: Int = 0

    var attachments: List<Attachment>? = null

    var category: String? = null

    @DocumentId
    var id: String? = null

    var pageUrl: String? = null

    var publishDate: Timestamp? = null

    var schoolYear: String? = null

    var snippet: String? = null

    var title: String? = null

    var type: Type? = null

}