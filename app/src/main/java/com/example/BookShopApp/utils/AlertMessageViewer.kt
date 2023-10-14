package com.example.BookShopApp.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.core.text.isDigitsOnly

object AlertMessageViewer {
    fun showAlertDialogMessage(context: Context, message: String) {
        AlertDialog.Builder(context)
            .setTitle(null)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Close") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    fun showAlertZalopay(context: Context, title: String, message: String) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }
}