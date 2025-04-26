package com.example.taskbookxpert.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskbookxpert.R
import com.github.barteksc.pdfviewer.PDFView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class PdfViewerActivity : AppCompatActivity() {

    private lateinit var pdfView: PDFView
    private val pdfUrl =
        "https://fssservices.bookxpert.co/GeneratedPDF/Companies/nadc/2024-2025/BalanceSheet.pdf"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_pdf_viewer)

        pdfView = PDFView(this, null)
        setContentView(pdfView)

        downloadAndDisplayPdf(pdfUrl)
    }

    private fun downloadAndDisplayPdf(url: String) {
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@PdfViewerActivity, "Download failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val inputStream = response.body?.byteStream()
                runOnUiThread {
                    inputStream?.let {
                        pdfView.fromStream(it).load()
                    }
                }
            }
        })
    }
}