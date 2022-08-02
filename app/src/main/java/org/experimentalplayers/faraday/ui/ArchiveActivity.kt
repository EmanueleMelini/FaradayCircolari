package org.experimentalplayers.faraday.ui

import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_archive.*
import org.experimentalplayers.faraday.R
import org.experimentalplayers.faraday.models.SiteDocument
import org.experimentalplayers.faraday.models.Type
import org.experimentalplayers.faraday.ui.adapters.ArchiveAdapter
import org.experimentalplayers.faraday.utils.ARCHIVE_EXTRA_TITLE
import org.experimentalplayers.faraday.utils.ARCHIVE_EXTRA_TYPE
import org.experimentalplayers.faraday.utils.FirestoreHelper
import simpleToast
import java.util.stream.Collectors
import kotlin.streams.toList

class ArchiveActivity : BaseActivity() {

    private lateinit var schoolYear: String
    private lateinit var type: Type

    private lateinit var archiveRecycler: RecyclerView
    private lateinit var archiveAdapter: ArchiveAdapter
    private var siteDocuments: List<SiteDocument> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        myContentView = R.layout.activity_archive
        super.onCreate(savedInstanceState)

        mContext = this

        //archiveRecycler = findViewById(R.id.archive_recycler)
        //archive_recycler.setHasFixedSize(true)
        /*mContext, siteDocuments*/

        val itemAnimator: DefaultItemAnimator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }
        archive_recycler.itemAnimator = itemAnimator

        siteDocuments = emptyList()
        archive_recycler.layoutManager = LinearLayoutManager(mContext)
        //GridLayoutManager(mContext, 2)
        archiveAdapter = ArchiveAdapter()
        archive_recycler.adapter = archiveAdapter
        archiveAdapter.submitList(siteDocuments)

        mContext = this

        var sy: String? = null
        var ty: Type? = null

        intent?.extras?.let {
            sy = it.getString(ARCHIVE_EXTRA_TITLE)
            ty = it.getSerializable(ARCHIVE_EXTRA_TYPE) as Type?
        }

        if(sy == null || ty == null)
            finish()
        else {
            sy?.let {
                schoolYear = it
            }
            ty?.let {
                type = it
            }
        }

    }

    override fun onResume() {
        super.onResume()

        mContext = this

        archive_text.text = schoolYear

        FirestoreHelper.firestoreInstance.getDocuments() {
            if(it != null) {

                siteDocuments = it.stream()
                    //.sorted { anno1, anno2 -> anno2.startYear.compareTo(anno1.startYear) }
                    //.filter { doc -> doc.type == type }
                    .toList()

                archiveAdapter.submitList(siteDocuments)

                if(it.isEmpty())
                    mContext.simpleToast(R.string.empty_docs)

            } else
                mContext.simpleToast(R.string.conn_error_text)
        }

    }

}