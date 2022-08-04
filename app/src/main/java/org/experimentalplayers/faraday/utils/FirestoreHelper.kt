package org.experimentalplayers.faraday.utils

import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import org.experimentalplayers.faraday.models.ArchiveEntry
import org.experimentalplayers.faraday.models.SiteDocument
import timber.log.Timber
import java.util.*
import kotlin.streams.toList

class FirestoreHelper {

    companion object {

        val firestoreInstance: FirestoreHelper = FirestoreHelper()

    }

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private fun checkCollectionUpdated(collection: CollectionReference, sharedString: String): Source {
        val sharedLast = MyApplication.sharedPreferences!!.getString(sharedString, null)
        sharedLast?.let { last ->
            val sdf = MyApplication.instance!!.simpleDateFormat
            val lastModified = sdf.parse(last)?.let { Timestamp(it) }

            if(lastModified != null) {
                collection.orderBy("lastModified", Query.Direction.DESCENDING)
                    .whereGreaterThan("lastModified", lastModified)
                    .limit(1)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if(!querySnapshot.isEmpty) {
                            var updateLast: Timestamp? = null
                            querySnapshot.documents.toList().let { list ->
                                if(list.size == 1)
                                    updateLast = list[0].get("lastModified") as Timestamp?
                            }

                            updateLast?.let { update ->
                                updateLastModified(update, sharedString)
                            }

                        }
                    }
                    .addOnFailureListener {
                        it.printStackTrace()
                    }
                return Source.CACHE
            } else {
                updateLastModified(Timestamp.now(), sharedString)
                return Source.SERVER
            }
        }
        updateLastModified(Timestamp.now(), sharedString)
        return Source.SERVER
    }

    private fun updateLastModified(lastModified: Timestamp, sharedString: String) {
        Timber.d("UPDATELAST-$lastModified")
        with(MyApplication.sharedPreferences!!.edit()) {
            putString(sharedString, MyApplication.instance!!.simpleDateFormat.format(Date(lastModified.seconds * 1000)) ?: "")
            apply()
        }
    }

    fun getDocuments(limit: Long, doWithDocs: (List<SiteDocument>?) -> Unit) {

        var docs: List<SiteDocument> = emptyList()

        val documentsCollection = db.collection(FIREBASE_COLLECTION_DOCUMENTS)

        val src = checkCollectionUpdated(documentsCollection, SHARED_LAST_DOCUMENTS)
        Timber.d("SOURCED-$src")

        documentsCollection
            .orderBy("articleId", Query.Direction.DESCENDING)
            .apply {
                if(limit != -1L)
                    this.limit(limit)
            }
            .get(src)
            .addOnSuccessListener {
                if(!it.isEmpty) {
                    docs = it.documents.stream()
                        .map { doc -> doc.toObject(SiteDocument::class.java) }
                        .filter { doc -> doc != null }
                        .map { doc -> doc as SiteDocument }
                        .toList()
                }
                doWithDocs(docs)
            }
            .addOnFailureListener {
                it.printStackTrace()
                doWithDocs(null)
            }

    }

    fun getArchive(doWithArchive: (List<ArchiveEntry>?) -> Unit) {

        val archiveCollection = db.collection(FIREBASE_COLLECTION_ARCHIVE)

        val src = checkCollectionUpdated(archiveCollection, SHARED_LAST_ARCHIVE)
        Timber.d("SOURCEA-$src")

        var archiveEntries = emptyList<ArchiveEntry>()

        archiveCollection
            .orderBy("startYear", Query.Direction.DESCENDING)
            .get(src)
            .addOnSuccessListener {
                if(!it.isEmpty) {
                    archiveEntries = it.documents.stream()
                        .map { archiveEntry -> archiveEntry.toObject(ArchiveEntry::class.java) }
                        .filter { archive -> archive != null }
                        .map { archiveEntry -> archiveEntry as ArchiveEntry }
                        .toList()
                }
                doWithArchive(archiveEntries)
            }
            .addOnFailureListener {
                it.printStackTrace()
                doWithArchive(null)
            }

    }

}