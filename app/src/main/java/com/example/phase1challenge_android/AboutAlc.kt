package com.example.phase1challenge_android

import android.app.Activity
import android.content.Context
import android.net.http.SslError
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.ConsoleMessage
import android.webkit.SslErrorHandler
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_about_alc.*
import android.webkit.WebView
import android.widget.ProgressBar
import org.jetbrains.anko.alert
import org.jetbrains.anko.indeterminateProgressDialog


class AboutAlc : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_alc)
        webview.getSettings().setLoadsImagesAutomatically(true)
        webview.getSettings().setJavaScriptEnabled(true)
        webview.setWebViewClient(AlcWebCliet(this))
        webview.loadUrl(getString(R.string.alcUrl))
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = getString(R.string.about_alc)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }



    class AlcWebCliet(context : Context) : WebViewClient() {



        private val progressBar by lazy{
            context.indeterminateProgressDialog("Loading from ALC. Please Wait....")
        }

        init {
            progressBar.show()
        }

        val con = context

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            progressBar.dismiss()
        }

        override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
            progressBar.dismiss()
            con.alert{
                message = "Error ${errorCode} occured"
                title = "Error"
                positiveButton(buttonText = "Retry", onClicked = {
                    view!!.reload()
                    it.dismiss()
                })
                negativeButton(buttonText = "Error Details", onClicked = {
                    displayMessage(con,"Error ${errorCode} details",description!!)
                    it.dismiss()
                })
            }.show()
        }

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            handler!!.proceed()
        }

        fun displayMessage(context : Context, titles : String, messages: String){
            context.alert{
                message = messages
                title  = titles
                positiveButton(buttonText = "OK",onClicked = {
                     val activity_only = con as Activity
                    activity_only.onBackPressed()
                    it.dismiss()
                })
            }.show()
        }
    }

}
