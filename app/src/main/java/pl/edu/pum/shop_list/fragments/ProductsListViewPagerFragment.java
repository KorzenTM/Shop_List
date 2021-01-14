package pl.edu.pum.shop_list.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.edu.pum.shop_list.R;
import pl.edu.pum.shop_list.activities.SplashScreen;
import pl.edu.pum.shop_list.adapters.ShoppingListViewPagerAdapter;

public class ProductsListViewPagerFragment extends Fragment
{

    public ProductsListViewPagerFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_products_list_view_pager, container, false);

        final ViewPager2 viewPager2 = v.findViewById(R.id.view_pager);
        ShoppingListViewPagerAdapter shoppingListViewPagerAdapter = new ShoppingListViewPagerAdapter(requireActivity(), SplashScreen.mShoppingLists);
        viewPager2.setAdapter(shoppingListViewPagerAdapter);
        viewPager2.setCurrentItem(ProductsListFragment.CurrentPosition, true);

        return v;
    }
}