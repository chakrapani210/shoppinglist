package com.chakra.shoppinglist.fragments;

import android.content.Intent;
import android.os.Bundle;

import com.chakra.shoppinglist.activities.CreateProductActivity;
import com.chakra.shoppinglist.R;
import com.chakra.shoppinglist.base.BaseFragment;
import com.chakra.shoppinglist.model.Product;
import com.chakra.shoppinglist.tasks.product.DeleteProduct;
import com.chakra.shoppinglist.tasks.product.LoadProductsByCategory;
import com.chakra.shoppinglist.tasks.product.LoadProductsByCategory.OnProductsLoaded;
import com.chakra.shoppinglist.tasks.product.UpdateProducts;
import com.chakra.shoppinglist.utils.Analytics;
import com.chakra.shoppinglist.views.Dialogs;
import com.chakra.shoppinglist.views.ProductsListView;
import com.chakra.shoppinglist.views.ProductsListView.ProductListViewObserver;

import java.util.Arrays;
import java.util.List;

public class ProductsFragment extends BaseFragment<ProductsListView> implements ProductListViewObserver, OnProductsLoaded
{
    private static final String PARAM_CATEGORY = "category";

    public static ProductsFragment create(String category)
    {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_CATEGORY, category);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected void initialize()
    {
        reloadProducts();
    }

    @Override
    public void onProductsLoaded(List<Product> products)
    {
        view.updateList(products);
    }

    public String title()
    {
        return parameter(PARAM_CATEGORY, "");
    }

    @Override
    public void onProductSelected(Product product)
    {
        UpdateProducts task = new UpdateProducts(getContext());
        task.moveToCart(product);

        view.removeProduct(product);

        Analytics analytics = new Analytics(getContext());
        analytics.cartItemAdded(product);
    }

    @Override
    public void onProductsOptions(Product product)
    {
        List<String> options = Arrays.asList(
                getString(R.string.button_update),
                getString(R.string.button_remove)
        );

        Dialogs dialogs = new Dialogs(getContext());
        dialogs.options(product.name(), options, option ->
        {
            if (option == 0)
            {
                editProduct(product);
            }
            else if (option == 1)
            {
                confirmRemoveProduct(product);
            }
        });
    }

    private void editProduct(Product product)
    {
        Intent intent = CreateProductActivity.intent(getContext(), product);
        startActivity(intent);
    }

    private void confirmRemoveProduct(Product product)
    {
        Dialogs dialogs = new Dialogs(getContext());
        dialogs.confirmation(product.name(), getString(R.string.dialog_remove_product), () -> removeProduct(product));
    }

    private void removeProduct(Product product)
    {
        DeleteProduct task = new DeleteProduct(getContext(), product, this::reloadProducts);
        task.execute();
    }

    private void reloadProducts()
    {
        LoadProductsByCategory task = new LoadProductsByCategory(getContext(), parameter(PARAM_CATEGORY, ""), this);
        task.execute();
    }

    @Override
    protected ProductsListView view()
    {
        return new ProductsListView(getContext(), this);
    }
}