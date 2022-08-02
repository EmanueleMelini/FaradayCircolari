package org.experimentalplayers.faraday.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import org.experimentalplayers.faraday.models.Archive
import org.experimentalplayers.faraday.models.Attachment
import org.experimentalplayers.faraday.models.SiteDocument
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors
import kotlin.streams.toList

class FirestoreHelper {

    companion object {

        val firestoreInstance: FirestoreHelper = FirestoreHelper()

    }

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private fun checkCollectionUpdated(collection: CollectionReference): Source {
        val last = MyApplication.sharedPreferences!!.getString(SHARED_LAST, "") ?: ""
        return if(last != "") {
            val sdf = MyApplication.instance!!.simpleDateFormat
            val lastModified = sdf.parse(last)?.let { Timestamp(it) }
            if(lastModified != null) {
                val query = collection.orderBy("lastModified", Query.Direction.DESCENDING)
                    .whereGreaterThan("lastModified", lastModified)
                Source.CACHE
            } else
                Source.SERVER
        } else
            Source.SERVER

        /*
            TODO: update shared when new found
            with(sharedPreferences.edit()) {
                putString(SHARED_LAST, lastModified ?: "")
                apply()
            }
         */
    }

    fun getDocuments(doWithDocs: (List<SiteDocument>?) -> Unit) {

        var docs: List<SiteDocument> = emptyList()

        val documentsCollection = db.collection(FIREBASE_COLLECTION_DOCUMENTS)

        val src = checkCollectionUpdated(documentsCollection)

        documentsCollection.get(src)
            .addOnSuccessListener {
                if(!it.isEmpty) {
                    docs = it.documents.stream()
                        .map { doc ->
                            doc.toObject(SiteDocument::class.java)
                        }
                        .filter { doc ->
                            doc != null
                        }
                        .map { doc ->
                            doc as SiteDocument
                        }
                        .toList()
                }
                doWithDocs(docs)
            }
            .addOnFailureListener {
                it.printStackTrace()
                doWithDocs(null)
            }

    }

    fun getAttachments(doWithAttachs: (List<Attachment>?) -> Unit) {

        val attachmentsCollection = db.collection(FIREBASE_COLLECTION_ATTACHMENTS)

        val src = checkCollectionUpdated(attachmentsCollection)

        var attachs = emptyList<Attachment>()

        attachmentsCollection.get(src)
            .addOnSuccessListener {
                if(!it.isEmpty) {
                    attachs = it.documents.stream()
                        .map { attach ->
                            attach.toObject(Attachment::class.java)
                        }
                        .filter { attach ->
                            attach != null
                        }
                        .map { attach ->
                            attach as Attachment
                        }
                        .toList()
                }
                doWithAttachs(attachs)
            }
            .addOnFailureListener {
                it.printStackTrace()
                doWithAttachs(null)
            }

    }

    fun getArchive(doWithArchive: (List<Archive>?) -> Unit) {

        val archiveCollection = db.collection(FIREBASE_COLLECTION_ARCHIVE)

        val src = checkCollectionUpdated(archiveCollection)

        var archives = emptyList<Archive>()

        archiveCollection
            .get(src)
            .addOnSuccessListener {
                if(!it.isEmpty) {
                    archives = it.documents.stream()
                        .map { archive ->
                            archive.toObject(Archive::class.java)
                        }
                        .filter { archive ->
                            archive != null
                        }
                        .map { archive ->
                            archive as Archive
                        }
                        .toList()
                }
                doWithArchive(archives)
            }
            .addOnFailureListener {
                it.printStackTrace()
                doWithArchive(null)
            }

    }

}