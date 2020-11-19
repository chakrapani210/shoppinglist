package com.chakra.shoppinglist.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.chakra.shoppinglist.model.Category;

import java.util.List;

public class ProductsFragmentAdapter extends FragmentStateAdapter {
    private final List<Category> categories;

    public ProductsFragmentAdapter(Fragment fragment, List<Category> categories) {
        super(fragment);
        this.categories = categories;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ProductsListFragment.create(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
    }
}