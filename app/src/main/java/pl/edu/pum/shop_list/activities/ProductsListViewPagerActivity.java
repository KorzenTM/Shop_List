package pl.edu.pum.shop_list.activities;

import androidx.fragment.app.Fragment;

import pl.edu.pum.shop_list.fragments.ProductsListViewPagerFragment;

public class ProductsListViewPagerActivity extends SingleFragmentActivity
{
    @Override
    protected Fragment createFragment()
    {
        return new ProductsListViewPagerFragment();
    }
}
