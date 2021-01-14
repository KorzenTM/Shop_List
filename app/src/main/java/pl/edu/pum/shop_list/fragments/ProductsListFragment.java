package pl.edu.pum.shop_list.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import pl.edu.pum.shop_list.R;
import pl.edu.pum.shop_list.activities.SplashScreen;
import pl.edu.pum.shop_list.adapters.ProductListRecyclerViewAdapter;
import pl.edu.pum.shop_list.adapters.ShoppingListRecyclerViewAdapter;
import pl.edu.pum.shop_list.models.ShoppingList;

public class ProductsListFragment extends Fragment
{
    private TextView mNameListTextView;
    private FloatingActionButton mAddProductFab;

    private int mIndex;
    public static int CurrentPosition = 0;
    private ShoppingList shoppingList;
    private List<ShoppingList> mShoppingLists;
    private RecyclerView mProductListsRecyclerView;
    private ProductListRecyclerViewAdapter mProductListRecyclerViewAdapter;

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
        mProductListsRecyclerView = v.findViewById(R.id.products_recycler_view);

        mProductListsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mProductListRecyclerViewAdapter = new ProductListRecyclerViewAdapter(getActivity(), mShoppingLists, shoppingList.getProductsList());
        mProductListsRecyclerView.setAdapter(mProductListRecyclerViewAdapter);

        mAddProductFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showAddDialog();
            }
        });


        return v;
    }

    private void showAddDialog()
    {
        final View view = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.list_dialog, null);
        EditText newProductName = view.findViewById(R.id.list_name_edit_text);
        newProductName.setHint("Name of new product");
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Add new product");
        alertDialog.setMessage("You can add the product you want to buy here.");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if (newProductName.getText().toString().isEmpty())
                {
                    Toast.makeText(getActivity(), "The field must not be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String name = newProductName.getText().toString();
                    shoppingList.addProduct(name);
                    SplashScreen.dbHandler.updateProducts(shoppingList);
                    SplashScreen.getShoppingLists();
                    mProductListRecyclerViewAdapter.notifyItemInserted(shoppingList.getProductsList().size() - 1);
                    mProductListsRecyclerView.scrollToPosition(shoppingList.getProductsList().size() - 1);
                    Toast.makeText(getActivity(), "The product " + name + " has been added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                alertDialog.dismiss();
            }
        });

        alertDialog.setView(view);
        alertDialog.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mNameListTextView.setText(shoppingList.getListName());
    }

    @Override
    public void onResume()
    {
        mProductListRecyclerViewAdapter.notifyDataSetChanged();
        super.onResume();
    }
}