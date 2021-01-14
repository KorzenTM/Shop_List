package pl.edu.pum.shop_list.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

import pl.edu.pum.shop_list.fragments.ProductsListFragment;
import pl.edu.pum.shop_list.models.ShoppingList;

public class ShoppingListViewPagerAdapter extends FragmentStateAdapter
{
    private final List<ShoppingList> mShoppingLists;

    public ShoppingListViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<ShoppingList> ShoppingLists)
    {
        super(fragmentActivity);
        this.mShoppingLists = ShoppingLists;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
        return ProductsListFragment.newInstance(position);
    }

    @Override
    public int getItemCount()
    {
        return mShoppingLists.size();
    }
}
