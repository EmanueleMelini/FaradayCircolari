package org.experimentalplayers.faraday.models

import com.google.firebase.firestore.Exclude

class Attachment() {

    var filename: String? = null

    var size: String? = null

    var type: String? = null

    var url: String? = null

    @Exclude
    var path: String? = null

}