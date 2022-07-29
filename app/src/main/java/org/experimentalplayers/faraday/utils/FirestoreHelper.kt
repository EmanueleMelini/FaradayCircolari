package org.experimentalplayers.faraday.utils

import com.google.firebase.firestore.FirebaseFirestore
import org.experimentalplayers.faraday.models.Attachment
import org.experimentalplayers.faraday.models.SiteDocument
import timber.log.Timber

class FirestoreHelper {

    companion object {

        val firestoreInstance: FirestoreHelper = FirestoreHelper()

    }

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getDocuments(doWithDocs: (MutableList<SiteDocument>?) -> Unit) {

        val docs = mutableListOf<SiteDocument>()

        db.collection("documents")
            .get()
            .addOnSuccessListener {
                if(!it.isEmpty) {
                    for(doc in it.documents) {
                        val d = doc.toObject(SiteDocument::class.java)
                        if(d != null)
                            docs.add(d)
                    }
                    doWithDocs(docs)
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
                doWithDocs(null)
            }

    }

    fun getAttachments(doWithAttachs: (MutableList<Attachment>?) -> Unit) {

        val attachs = mutableListOf<Attachment>()

        db.collection("attachments")
            .get()
            .addOnSuccessListener {
                Timber.d("ATTACHS1-$it")
                Timber.d("ATTACHS2-${it.documents}")
                if(!it.isEmpty) {
                    for(attach in it.documents) {
                        val a = attach.toObject(Attachment::class.java)
                        if(a != null)
                            attachs.add(a)
                    }
                    doWithAttachs(attachs)
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
                doWithAttachs(null)
            }

    }

}