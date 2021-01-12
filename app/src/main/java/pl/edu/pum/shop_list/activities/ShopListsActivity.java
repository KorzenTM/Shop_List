package pl.edu.pum.shop_list.activities;

import androidx.fragment.app.Fragment;

import pl.edu.pum.shop_list.fragments.ShopListsFragment;

public class ShopListsActivity extends SingleFragmentActivity
{
    @Override
    protected Fragment createFragment()
    {
        return new ShopListsFragment();
    }
}
