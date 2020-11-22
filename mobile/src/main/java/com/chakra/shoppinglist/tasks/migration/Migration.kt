package com.chakra.shoppinglist.tasks.migration

import com.chakra.shoppinglist.database.AppDatabase.Companion.instance
import android.os.AsyncTask
import android.annotation.SuppressLint
import android.app.Activity
import com.chakra.shoppinglist.database.AppDatabase
import android.app.ProgressDialog
import android.content.Context
import com.chakra.shoppinglist.R

class Migration(@field:SuppressLint("StaticFieldLeak") private val context: Context, callback: OnMigrationDone?)
    : AsyncTask<Void?, Void?, Void?>() {
    private val database: AppDatabase
    private val callback: OnMigrationDone?
    private val dialog: ProgressDialog
    override fun onPreExecute() {
        dialog.show()
    }

    override fun doInBackground(vararg params: Void?): Void? {
        database.initialize(context)
        return null
    }

    override fun onPostExecute(aVoid: Void?) {
        dialog.dismiss()
        callback?.onMigrationDone()
    }

    interface OnMigrationDone {
        fun onMigrationDone()
    }

    init {
        database = instance(context)
        this.callback = callback
        dialog = ProgressDialog(context)
        dialog.setMessage(context.getString(R.string.dialog_update_database))
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
    }
}