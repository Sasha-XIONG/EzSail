package com.example.ezsail.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.ezsail.R
import com.example.ezsail.databinding.FragmentWebviewBinding

class WebViewFragment: Fragment(R.layout.fragment_webview) {

    // Initialise view binding
    private var webViewbinding: FragmentWebviewBinding? = null
    private val binding get() = webViewbinding!!

    private val args: WebViewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        webViewbinding = FragmentWebviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mimeType = "text/html";
        val encoding = "UTF-8";
        val html = args.html!!

        // Load html file
        binding.webView.loadDataWithBaseURL("", html, mimeType, encoding, "")
    }
}