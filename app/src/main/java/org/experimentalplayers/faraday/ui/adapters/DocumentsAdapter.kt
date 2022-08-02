package org.experimentalplayers.faraday.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import goWithExtras
import org.experimentalplayers.faraday.R
import org.experimentalplayers.faraday.models.ArchiveEntry
import org.experimentalplayers.faraday.ui.ArchiveActivity
import org.experimentalplayers.faraday.ui.HomeActivity
import org.experimentalplayers.faraday.ui.fragments.DocumentsFragment
import org.experimentalplayers.faraday.ui.fragments.InfoFragment
import org.experimentalplayers.faraday.utils.ARCHIVE_EXTRA_TITLE
import org.experimentalplayers.faraday.utils.ARCHIVE_EXTRA_TYPE

class DocumentsAdapter(private val mContext: Context, private val documentsFragment: DocumentsFragment) : ListAdapter<ArchiveEntry, DocumentsAdapter.DocumentsViewHolder>(DocumentsDiffCallback()) {

    class DocumentsDiffCallback : DiffUtil.ItemCallback<ArchiveEntry>() {
        override fun areItemsTheSame(oldItem: ArchiveEntry, newItem: ArchiveEntry): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ArchiveEntry, newItem: ArchiveEntry): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentsViewHolder {
        return DocumentsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.documents_item, parent, false))
    }

    override fun onBindViewHolder(holder: DocumentsViewHolder, position: Int) {

        val item = getItem(position)

        holder.title.text = item.id

        holder.itemView.setOnClickListener {
            val extras: MutableMap<String, Any> = mutableMapOf()
            item.schoolYear?.let {
                extras.put(ARCHIVE_EXTRA_TITLE, it)
            }
            item.type?.let {
                extras.put(ARCHIVE_EXTRA_TYPE, it)
            }

            if(item.schoolYear != null && item.type != null)
                documentsFragment.seeDocuments(item.schoolYear!!, item.type!!)

            //mContext.goWithExtras(ArchiveActivity::class.java, extras)
        }

    }

    override fun getItemCount(): Int = currentList.size


    inner class DocumentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView

        init {
            title = itemView.findViewById(R.id.documents_item_text)
        }

    }
}