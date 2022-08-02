package org.experimentalplayers.faraday.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import goWithExtras
import org.experimentalplayers.faraday.R
import org.experimentalplayers.faraday.models.SiteDocument
import org.experimentalplayers.faraday.ui.ArchiveActivity
import org.experimentalplayers.faraday.utils.ARCHIVE_EXTRA
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class ArchiveAdapter(
    private var mContext: Context,
    private var list: List<SiteDocument>
) : RecyclerView.Adapter<ArchiveViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchiveViewHolder {
        return ArchiveViewHolder(LayoutInflater.from(mContext).inflate(R.layout.archive_item, parent, false))
    }

    override fun onBindViewHolder(holder: ArchiveViewHolder, position: Int) {

        val item = list[position]

        holder.title.text = item.title

        item.publishDate?.let {
            val sfd = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            holder.date.text = sfd.format(Date(it.seconds * 1000))
        }

        item.attachments?.let {
            val str = "Allegati: ${it.size}"
            holder.count.text = str
        }

    }

    override fun getItemCount(): Int = list.size

}

class ArchiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var title: TextView
    var date: TextView
    var count: TextView

    init {
        title = itemView.findViewById(R.id.archive_item_title)
        date = itemView.findViewById(R.id.archive_item_date)
        count = itemView.findViewById(R.id.archive_item_count)
    }

}