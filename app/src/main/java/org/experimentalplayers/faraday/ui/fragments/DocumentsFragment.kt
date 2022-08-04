package org.experimentalplayers.faraday.ui.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.experimentalplayers.faraday.R
import org.experimentalplayers.faraday.models.ArchiveEntry
import org.experimentalplayers.faraday.models.SiteDocument
import org.experimentalplayers.faraday.models.Type
import org.experimentalplayers.faraday.ui.HomeActivity
import org.experimentalplayers.faraday.ui.adapters.ArchiveAdapter
import org.experimentalplayers.faraday.ui.adapters.DocumentsAdapter
import org.experimentalplayers.faraday.utils.FirestoreHelper
import setGone
import setVisible
import simpleToast
import timber.log.Timber
import kotlin.streams.toList


class DocumentsFragment : Fragment() {

    companion object {
        fun newInstance() = DocumentsFragment()

        val PATH_CIRC = PathInterpolatorCompat.create(0.785f, 0.135f, 0.150f, 0.860f)
        val PATH_EXPO = PathInterpolatorCompat.create(1.000f, 0.000f, 0.000f, 1.000f)
        val PATH_BACK = PathInterpolatorCompat.create(0.680f, -0.550f, 0.265f, 1.550f)
    }

    private lateinit var topText: TextView
    private lateinit var yearsRecycler: RecyclerView
    private lateinit var listView: ConstraintLayout
    private lateinit var listRecycler: RecyclerView
    private lateinit var listText: TextView
    private lateinit var listLinear: LinearLayout
    private lateinit var top: View
    private lateinit var arrowBack: ImageView

    private lateinit var mContext: Context
    private lateinit var homeActivity: HomeActivity

    private lateinit var yearsAdapter: DocumentsAdapter
    private lateinit var listAdapter: ArchiveAdapter
    private var archiveEntries: List<ArchiveEntry> = emptyList()
    private var siteDocuments: List<SiteDocument> = emptyList()
    private var isArchive: Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        Timber.d("DocumentFragment.onCreateView")

        val view = inflater.inflate(R.layout.fragment_documents, container, false)

        listText = view.findViewById(R.id.list_text)
        listView = view.findViewById(R.id.document_list)
        listLinear = view.findViewById(R.id.list_linear)
        topText = view.findViewById(R.id.top_text)
        top = view.findViewById(R.id.documents_top)
        arrowBack = view.findViewById(R.id.top_back)
        yearsRecycler = view.findViewById(R.id.documents_recycler_archive)
        listRecycler = view.findViewById(R.id.list_recycler)

        archiveEntries = emptyList()
        siteDocuments = emptyList()

        val itemAnimator: DefaultItemAnimator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }
        yearsRecycler.itemAnimator = itemAnimator
        yearsRecycler.layoutManager = LinearLayoutManager(mContext)
        yearsAdapter = DocumentsAdapter(mContext, this)
        yearsRecycler.adapter = yearsAdapter
        yearsAdapter.submitList(archiveEntries)

        val itemAnimator2: DefaultItemAnimator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }
        listRecycler.itemAnimator = itemAnimator2
        listRecycler.layoutManager = LinearLayoutManager(mContext)
        listAdapter = ArchiveAdapter()
        listRecycler.adapter = listAdapter
        listAdapter.submitList(siteDocuments)

        top.bringToFront()

        arrowBack.setGone()
        arrowBack.setOnClickListener { homeActivity.onBackPressed() }

        listView.setGone()

        listView.translationY = -(listView.height.toFloat())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.d("DocumentFragment.onViewCreated")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d("DocumentFragment.onCreate")

        mContext = requireContext()
        homeActivity = (mContext as HomeActivity)

    }

    override fun onResume() {
        super.onResume()

        Timber.d("DocumentFragment.onResume")

        homeActivity.canExit = false
        homeActivity.setOnBackPressedDispatcher {
            if(!isArchive) {
                isArchive = true
                slideUp(listView, 1000L, { arrowBack.setGone() }, {
                    listView.setGone()
                    slideDown(yearsRecycler, 1000L, {}, {})
                })
            } else
                homeActivity.bottomBar.selectTabById(R.id.tab_home)
        }

        top.bringToFront()

        if(isArchive) {
            yearsRecycler.setVisible()
            listView.setGone()
        }

        topText.setText(R.string.documents_top_text)

        mContext = requireContext()

        refreshYears()

    }

    private fun slideUp(view: View, duration: Long, onAnimationStart: () -> Unit, onAnimationEnd: () -> Unit) {

        view.animate()
            .translationYBy(view.height.toFloat())
            .translationY(-view.height.toFloat())
            .alphaBy(1.0f)
            .alpha(0.0f)
            .setDuration(duration)
            .setInterpolator(PATH_CIRC)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)

                    onAnimationStart()

                }

                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)

                    onAnimationEnd()

                }
            })

    }

    private fun slideLeft(view: View, duration: Long, onAnimationStart: () -> Unit, onAnimationEnd: () -> Unit) {

        view.animate()
            .translationXBy(view.height.toFloat())
            .translationX(-view.height.toFloat())
            .alphaBy(1.0f)
            .alpha(0.0f)
            .setDuration(duration)
            .setInterpolator(PATH_CIRC)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)

                    onAnimationStart()

                }

                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)

                    onAnimationEnd()

                }
            })

    }

    private fun slideDown(view: View, duration: Long, onAnimationStart: () -> Unit, onAnimationEnd: () -> Unit) {

        view.setVisible()
        view.animate()
            .translationY(0.0f)
            .alphaBy(0.0f)
            .alpha(1.0f)
            .setDuration(duration)
            .setInterpolator(PATH_CIRC)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)

                    onAnimationStart()

                }

                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)

                    onAnimationEnd()

                }
            })

    }

    private fun slideRight(view: View, duration: Long, onAnimationStart: () -> Unit, onAnimationEnd: () -> Unit) {

        view.setVisible()
        view.animate()
            .translationX(0.0f)
            .alphaBy(0.0f)
            .alpha(1.0f)
            .setDuration(duration)
            .setInterpolator(PATH_CIRC)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)

                    onAnimationStart()

                }

                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)

                    onAnimationEnd()

                }
            })

    }

    fun seeDocuments(schoolYear: String, type: Type) {
        isArchive = false

        slideUp(yearsRecycler, 1000L, {}, {
            yearsRecycler.setGone()
            slideDown(listView, 1000L, {}, { arrowBack.setVisible() })
        })

        listText.text = schoolYear

        refreshDocuments(type, schoolYear)

    }


    private fun refreshYears() {
        FirestoreHelper.firestoreInstance.getArchive() {
            if(it != null) {

                archiveEntries = it

                yearsAdapter.submitList(archiveEntries)

                if(it.isEmpty())
                    mContext.simpleToast(R.string.empty_archive)

            } else
                mContext.simpleToast(R.string.conn_error_text)
        }
    }

    private fun refreshDocuments(type: Type, schoolYear: String) {
        FirestoreHelper.firestoreInstance.getDocuments(-1L) {
            if(it != null) {

                it.forEach { doc ->
                    Timber.d("DOC PRE FILTER-${doc.id},${doc.type},${doc.schoolYear},${doc.title}")
                }

                siteDocuments = it.stream()
                    .filter { doc -> doc.type == type /*&& doc.schoolYear == schoolYear*/ }
                    .toList()

                siteDocuments.forEach { doc ->
                    Timber.d("DOC AFTER FILTER-${doc.id},${doc.type},${doc.schoolYear},${doc.title}")
                }

                listAdapter.submitList(siteDocuments)

                if(it.isEmpty())
                    mContext.simpleToast(R.string.empty_docs)

            } else
                mContext.simpleToast(R.string.conn_error_text)
        }
    }

}