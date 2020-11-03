package com.chakra.shoppinglist.tasks.migration;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.chakra.shoppinglist.R;
import com.chakra.shoppinglist.database.AppDatabase;
import com.chakra.shoppinglist.database.CategoryDao;
import com.chakra.shoppinglist.database.ProductDao;
import com.chakra.shoppinglist.model.Category;
import com.chakra.shoppinglist.model.Product;
import com.chakra.shoppinglist.utils.ResourceUtils;
import com.chakra.shoppinglist.old.CartItem;
import com.orm.query.Select;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Migration extends AsyncTask<Void, Void, Void>
{
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private final AppDatabase database;
    private final OnMigrationDone callback;
    private final ProgressDialog dialog;

    public Migration(Context context, OnMigrationDone callback)
    {
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
        List<com.chakra.shoppinglist.old.Category> categories = Select.from(com.chakra.shoppinglist.old.Category.class).list();

        if (!categories.isEmpty())
        {
            CategoryDao categoryDao = database.categoryDao();
            ProductDao productDao = database.productDao();

            for (com.chakra.shoppinglist.old.Category category : categories)
            {
                try
                {
                    categoryDao.insert(new Category(category.getName()));
                }
                catch (Exception e)
                {
                    // error creating category
                }
            }

            Set<Long> productsInCart = new HashSet<>();
            List<CartItem> cartItems = Select.from(CartItem.class).list();

            for (CartItem cartItem : cartItems)
            {
                try
                {
                    productDao.insert(product(
                            cartItem.getCategory(),
                            cartItem.getName(),
                            cartItem.getImage(),
                            true
                    ));

                    productsInCart.add(cartItem.productId());
                }
                catch (Exception e)
                {
                    // error creating product
                }
            }

            List<com.chakra.shoppinglist.old.Product> products = Select.from(com.chakra.shoppinglist.old.Product.class).list();

            for (com.chakra.shoppinglist.old.Product product : products)
            {
                try
                {
                    if (!productsInCart.contains(product.getId()))
                    {
                        productDao.insert(product(
                                product.getCategory(),
                                product.getName(),
                                product.getImage(),
                                false
                        ));
                    }
                }
                catch (Exception e)
                {
                    // error creating product
                }
            }

            CartItem.deleteAll(CartItem.class);
            com.chakra.shoppinglist.old.Product.deleteAll(com.chakra.shoppinglist.old.Product.class);
            com.chakra.shoppinglist.old.Category.deleteAll(com.chakra.shoppinglist.old.Category.class);
        }
        else
        {
            database.initialize(context);
        }

        return null;
    }

    private Product product(String category, String name, byte[] image, Boolean inCart) throws Exception
    {
        File imageFile = ResourceUtils.createFile(context, image);

        return new Product(
                category,
                name,
                imageFile.getAbsolutePath(),
                inCart,
                false
        );
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        dialog.dismiss();

        callback.onMigrationDone();
    }

    public interface OnMigrationDone
    {
        void onMigrationDone();
    }
}