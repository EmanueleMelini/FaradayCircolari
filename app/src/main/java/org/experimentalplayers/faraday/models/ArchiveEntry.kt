package org.experimentalplayers.faraday.models

import com.google.firebase.firestore.DocumentId

class ArchiveEntry : java.io.Serializable {

    @DocumentId
    var id: String? = null

    var endYear: Int = 0

    var schoolYear: String? = null

    var startYear: Int = 0

    var type: Type? = null

    var url: String? = null

    override fun equals(other: Any?): Boolean {
        return if(other is ArchiveEntry) super.equals(other) else false
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + endYear
        result = 31 * result + (schoolYear?.hashCode() ?: 0)
        result = 31 * result + startYear
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (url?.hashCode() ?: 0)
        return result
    }

}