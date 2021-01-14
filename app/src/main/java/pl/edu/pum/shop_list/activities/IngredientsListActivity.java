package pl.edu.pum.shop_list.activities;

import androidx.fragment.app.Fragment;

import pl.edu.pum.shop_list.fragments.ProductsListFragment;

public class IngredientsListActivity extends SingleFragmentActivity
{
    @Override
    protected Fragment createFragment()
    {
        return new ProductsListFragment();
    }
}
