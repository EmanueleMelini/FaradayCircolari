package org.experimentalplayers.faraday.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

class SiteDocument : java.io.Serializable {

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

    override fun equals(other: Any?): Boolean {
        return if(other is SiteDocument) super.equals(other) else false
    }

    override fun hashCode(): Int {
        var result = articleId
        result = 31 * result + (attachments?.hashCode() ?: 0)
        result = 31 * result + (category?.hashCode() ?: 0)
        result = 31 * result + (id?.hashCode() ?: 0)
        result = 31 * result + (pageUrl?.hashCode() ?: 0)
        result = 31 * result + (publishDate?.hashCode() ?: 0)
        result = 31 * result + (schoolYear?.hashCode() ?: 0)
        result = 31 * result + (snippet?.hashCode() ?: 0)
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "id-$id"
    }

}