package com.chakra.shoppinglist.tasks.migration;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.chakra.shoppinglist.R;
import com.chakra.shoppinglist.database.AppDatabase;

public class Migration extends AsyncTask<Void, Void, Void>
{
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private final AppDatabase database;
    private final OnMigrationDone callback;
    private final ProgressDialog dialog;

    public Migration(Context context, OnMigrationDone callback) {
        this.context = context;
        this.database = AppDatabase.instance(context);
        this.callback = callback;
        this.dialog = new ProgressDialog(context);
        this.dialog.setMessage(context.getString(R.string.dialog_update_database));
        this.dialog.setCancelable(false);
        this.dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onPreExecute()
    {
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        database.initialize(context);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        dialog.dismiss();

        if (callback != null) {
            callback.onMigrationDone();
        }
    }

    public interface OnMigrationDone
    {
        void onMigrationDone();
    }
}