package pl.edu.pum.shop_list.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import pl.edu.pum.shop_list.R;
import pl.edu.pum.shop_list.activities.SplashScreen;
import pl.edu.pum.shop_list.adapters.ShoppingListRecyclerViewAdapter;
import pl.edu.pum.shop_list.models.ShoppingList;

public class ShopListsFragment extends Fragment
{
    private RecyclerView mShoppingListsRecyclerView;
    private ShoppingListRecyclerViewAdapter mShoppingListRecyclerViewAdapter;
    private FloatingActionButton mAddListFAB;


    private List<ShoppingList> mShoppingLists;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v =  inflater.inflate(R.layout.fragment_shopping_lists, container, false);

        mAddListFAB = v.findViewById(R.id.fab);
        mShoppingListsRecyclerView = v.findViewById(R.id.recycler_view);
        mShoppingLists = SplashScreen.mShoppingLists;

        mShoppingListsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mShoppingListRecyclerViewAdapter = new ShoppingListRecyclerViewAdapter(getActivity(), mShoppingLists);
        mShoppingListsRecyclerView.setAdapter(mShoppingListRecyclerViewAdapter);

        return v;
    }

    @Override
    public void onResume()
    {
        mShoppingListRecyclerViewAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onDestroyView()
    {
        SplashScreen.dbHandler.close();
        super.onDestroyView();
    }
}