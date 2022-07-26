package org.experimentalplayers.faraday.ui.fragments

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import org.experimentalplayers.faraday.R

class InfoFragment : Fragment() {

    companion object {
        fun newInstance() = InfoFragment()
    }

    private lateinit var textView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_info, container, false)

        textView = view.findViewById(R.id.info_text)

        // Inflate the layout for this fragment
        return view
    }

    override fun onResume() {
        super.onResume()

        val mark = Markwon.builder(requireContext())
            .usePlugin(object : AbstractMarkwonPlugin() {
            override fun configureTheme(builder: MarkwonTheme.Builder) {
                builder.headingBreakHeight(0)
            }
        }).build()

        val markText = "# Circolari Faraday\n" +
                "## Informazioni\n" +
                "Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrum exercitationem ullamco laboriosam, nisi ut aliquid ex ea commodi consequatur. Duis aute irure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n" +
                "### Circolari\n" +
                "Info su circolari\n" +
                "### Avvisi\n" +
                "Info su avvisi"

        mark.setMarkdown(textView, markText)

    }

}