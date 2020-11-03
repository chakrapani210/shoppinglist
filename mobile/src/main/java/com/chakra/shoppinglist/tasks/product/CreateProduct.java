package com.chakra.shoppinglist.tasks.product;

import android.content.Context;
import android.os.AsyncTask;

import com.chakra.shoppinglist.database.ProductDao;
import com.chakra.shoppinglist.model.Product;

public class CreateProduct extends AsyncTask<Void, Void, Boolean>
{
    private final Product product;
    private final ProductDao dao;
    private final OnProductCreated callback;

    public CreateProduct(Context context, Product product, OnProductCreated callback)
    {
        this.product = product;
        this.dao = ProductDao.instance(context);
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(Void... voids)
    {
        Boolean result = false;

        if (!dao.containsWithName(product.name()))
        {
            dao.insert(product);
            result = true;
        }

        return result;
    }

    @Override
    protected void onPostExecute(Boolean result)
    {
        callback.onProductsCreated(result);
    }

    public interface OnProductCreated
    {
        void onProductsCreated(Boolean result);
    }
}