package pl.edu.pum.shop_list.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import pl.edu.pum.shop_list.R;
import pl.edu.pum.shop_list.activities.SplashScreen;
import pl.edu.pum.shop_list.models.ShoppingList;

public class ProductsListFragment extends Fragment
{
    private TextView mNameListTextView;
    private FloatingActionButton mAddProductFab;

    private int mIndex;
    public static int CurrentPosition = 0;
    private ShoppingList shoppingList;
    private List<ShoppingList> mShoppingLists;

    public ProductsListFragment()
    {
        // Required empty public constructor
    }

    public static ProductsListFragment newInstance(int counter)
    {
        ProductsListFragment fragment = new ProductsListFragment();
        Bundle args = new Bundle();
        args.putInt("KEY", counter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mIndex = getArguments().getInt("KEY");
        shoppingList = new ShoppingList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_products_list, container, false);

        mShoppingLists = SplashScreen.mShoppingLists;
        shoppingList = mShoppingLists.get(mIndex);

        mNameListTextView = v.findViewById(R.id.name_list_text_view);
        mAddProductFab = v.findViewById(R.id.add_new_product_fab);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mNameListTextView.setText(shoppingList.getListName());
    }
}