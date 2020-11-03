package com.chakra.shoppinglist.views;

import android.text.TextUtils;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import com.chakra.shoppinglist.R;
import com.chakra.shoppinglist.adapters.ProductsFragmentAdapter;
import com.chakra.shoppinglist.base.BaseView;
import com.chakra.shoppinglist.fragments.ProductsFragment;
import com.chakra.shoppinglist.model.Category;
import com.chakra.shoppinglist.views.AddProductView.AddProductViewObserver;
import com.chakra.shoppinglist.views.AddProductView.ViewContainer;
import com.mauriciotogneri.androidutils.uibinder.annotations.BindView;
import com.mauriciotogneri.androidutils.uibinder.annotations.OnClick;

import java.util.ArrayList;
import java.util.List;

public class AddProductView extends BaseView<AddProductViewObserver, ViewContainer> implements OnPageChangeListener
{
    private String lastCategorySelected;

    public AddProductView(AddProductViewObserver observer)
    {
        super(R.layout.screen_add_product, observer, new ViewContainer());
    }

    @Override
    protected void initialize()
    {
        toolbarTitle(R.string.toolbar_title_add_product);
        enableBack(v -> observer.onBack());

        ui.pagerHeader.setDrawFullUnderline(false);
        ui.pagerHeader.setTabIndicatorColor(color(R.color.primary));
    }

    public void updateLists(FragmentManager fragmentManager, List<Category> categories)
    {
        List<ProductsFragment> fragments = new ArrayList<>();

        for (Category category : categories)
        {
            ProductsFragment fragment = ProductsFragment.create(category.name());
            fragments.add(fragment);
        }

        ui.pager.removeOnPageChangeListener(this);
        ui.pager.addOnPageChangeListener(this);
        ui.pager.setOffscreenPageLimit(fragments.size());
        ProductsFragmentAdapter adapter = new ProductsFragmentAdapter(fragmentManager, fragments);
        ui.pager.setAdapter(adapter);

        if (!TextUtils.isEmpty(lastCategorySelected))
        {
            int position = categories.indexOf(new Category(lastCategorySelected));
            ui.pager.setCurrentItem(position);
        }
    }

    @OnClick(R.id.product_create)
    public void onActionButton()
    {
        observer.onCreateProduct(currentTitle(ui.pager.getCurrentItem()));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
    }

    @Override
    public void onPageSelected(int position)
    {
        lastCategorySelected = currentTitle(position);
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {
    }

    private String currentTitle(int position)
    {
        ProductsFragmentAdapter adapter = (ProductsFragmentAdapter) ui.pager.getAdapter();
        ProductsFragment fragment = (ProductsFragment) adapter.getItem(position);

        return fragment.title();
    }

    public interface AddProductViewObserver
    {
        void onBack();

        void onCreateProduct(String category);
    }

    public static class ViewContainer
    {
        @BindView(R.id.pager)
        public ViewPager pager;

        @BindView(R.id.pager_header)
        public PagerTabStrip pagerHeader;
    }
}