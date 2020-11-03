package com.chakra.shoppinglist.tasks.category;

import android.content.Context;
import android.os.AsyncTask;

import com.chakra.shoppinglist.database.CategoryDao;
import com.chakra.shoppinglist.database.ProductDao;
import com.chakra.shoppinglist.model.Category;

public class DeleteCategory extends AsyncTask<Void, Void, Boolean>
{
    private final Category category;
    private final CategoryDao categoryDao;
    private final ProductDao productDao;
    private final OnCategoryDeleted callback;

    public DeleteCategory(Context context, Category category, OnCategoryDeleted callback)
    {
        this.category = category;
        this.categoryDao = CategoryDao.instance(context);
        this.productDao = ProductDao.instance(context);
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(Void... voids)
    {
        Boolean result = false;

        if (!productDao.containsWithCategory(category.name()))
        {
            categoryDao.delete(category);
            result = true;
        }

        return result;
    }

    @Override
    protected void onPostExecute(Boolean result)
    {
        callback.onCategoryDeleted(result);
    }

    public interface OnCategoryDeleted
    {
        void onCategoryDeleted(Boolean result);
    }
}