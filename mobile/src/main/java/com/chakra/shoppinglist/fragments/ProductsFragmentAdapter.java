package com.chakra.shoppinglist.fragments;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class ProductsFragmentAdapter extends FragmentPagerAdapter
{
    private final List<ProductsListFragment> fragments;

    public ProductsFragmentAdapter(FragmentManager fragmentManager, List<ProductsListFragment> fragments) {
        super(fragmentManager);

        this.fragments = fragments;
    }

    @Override
    public int getCount()
    {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position)
    {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return fragments.get(position).getTitle().toUpperCase();
    }
}