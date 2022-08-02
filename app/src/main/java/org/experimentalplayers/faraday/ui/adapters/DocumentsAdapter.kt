package org.experimentalplayers.faraday.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import go
import goWithExtras
import org.experimentalplayers.faraday.R
import org.experimentalplayers.faraday.models.Archive
import org.experimentalplayers.faraday.ui.ArchiveActivity
import org.experimentalplayers.faraday.utils.ARCHIVE_EXTRA
import timber.log.Timber

class DocumentsAdapter(
    private var mContext: Context,
    private var list: List<Archive>
) : RecyclerView.Adapter<DocumentsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentsViewHolder {
        return DocumentsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.documents_item, parent, false))
    }

    override fun onBindViewHolder(holder: DocumentsViewHolder, position: Int) {

        val item = list[position]

        holder.title.text = item.id

        holder.itemView.setOnClickListener {
            val extras: MutableMap<String, String> = mutableMapOf()
            item.schoolYear?.let {
                extras.put(ARCHIVE_EXTRA, it)
            }
            Timber.d("SY1-$item.schoolYear")
            mContext.goWithExtras(ArchiveActivity::class.java, extras)
        }

    }

    override fun getItemCount(): Int = list.size

}

class DocumentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var title: TextView

    init {
        title = itemView.findViewById(R.id.documents_item_text)
    }

}