package org.experimentalplayers.faraday.models

import com.google.firebase.firestore.DocumentId

class Archive {

    @DocumentId
    var id: String? = null

    var endYear: Int = 0

    var schoolYear: String? = null

    var startYear: Int = 0

    var type: Type? = null

    var url: String? = null

}