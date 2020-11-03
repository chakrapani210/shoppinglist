package com.chakra.shoppinglist.tasks.product;

import android.content.Context;
import android.os.AsyncTask;

import com.chakra.shoppinglist.database.ProductDao;
import com.chakra.shoppinglist.model.Product;

public class DeleteProduct extends AsyncTask<Void, Void, Void>
{
    private final Product product;
    private final ProductDao dao;
    private final OnProductDeleted callback;

    public DeleteProduct(Context context, Product product, OnProductDeleted callback)
    {
        this.product = product;
        this.dao = ProductDao.instance(context);
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        dao.delete(product);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        callback.onProductDeleted();
    }

    public interface OnProductDeleted
    {
        void onProductDeleted();
    }
}