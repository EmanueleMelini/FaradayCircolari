package org.experimentalplayers.faraday.ui

import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Source
import kotlinx.android.synthetic.main.activity_archive.*
import org.experimentalplayers.faraday.R
import org.experimentalplayers.faraday.models.SiteDocument
import org.experimentalplayers.faraday.models.Type
import org.experimentalplayers.faraday.ui.adapters.ArchiveAdapter
import org.experimentalplayers.faraday.ui.adapters.DocumentsAdapter
import org.experimentalplayers.faraday.utils.ARCHIVE_EXTRA
import org.experimentalplayers.faraday.utils.FirestoreHelper
import simpleToast
import timber.log.Timber
import kotlin.streams.toList

class ArchiveActivity : BaseActivity() {

    private lateinit var schoolYear: String
    private lateinit var archiveRecycler: RecyclerView
    private lateinit var archiveAdapter: ArchiveAdapter
    private var siteDocuments: MutableList<SiteDocument> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        myContentView = R.layout.activity_archive
        super.onCreate(savedInstanceState)

        mContext = this

        archiveRecycler = findViewById(R.id.archive_recycler)
        archiveRecycler.setHasFixedSize(true)
        archiveRecycler.layoutManager = GridLayoutManager(mContext, 2)
        archiveAdapter = ArchiveAdapter(mContext, siteDocuments)
        archiveRecycler.adapter = archiveAdapter

        mContext = this

        val sy = intent?.extras?.getString(ARCHIVE_EXTRA)

        Timber.d("SY-$sy")

        if(sy == null)
            finish()
        else
            schoolYear = sy

    }

    override fun onResume() {
        super.onResume()

        mContext = this

        archive_text.text = schoolYear

        siteDocuments.clear()

        FirestoreHelper.firestoreInstance.getDocuments() {
            if(it != null) {
                siteDocuments.addAll(it)
                archiveAdapter.notifyDataSetChanged()
            } else
                mContext.simpleToast("Errore nella chiamata, riprovare pià tardi")
        }

    }

}