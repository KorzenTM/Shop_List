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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pl.edu.pum.shop_list.R;
import pl.edu.pum.shop_list.activities.ProductsListViewPagerActivity;
import pl.edu.pum.shop_list.activities.SplashScreen;
import pl.edu.pum.shop_list.adapters.ProductListRecyclerViewAdapter;
import pl.edu.pum.shop_list.models.ShoppingList;

public class ProductsListFragment extends Fragment
{
    private TextView mNameListTextView;
    private FloatingActionButton mAddProductFab;

    private int mIndex;
    public static int CurrentPosition = 0;
    private ShoppingList shoppingList;
    private List<ShoppingList> mShoppingLists;
    public RecyclerView mCurrentProductListsRecyclerView;
    public ProductListRecyclerViewAdapter mCurrentRecyclerViewAdapter;
    public static List<ProductListRecyclerViewAdapter> productListRecyclerViewAdapterList = new ArrayList<>();
    public static  List<RecyclerView> productRecyclerViewList = new ArrayList<>();


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
        {
            mIndex = getArguments().getInt("KEY");
        }

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
        mCurrentProductListsRecyclerView = v.findViewById(R.id.products_recycler_view);

        mCurrentProductListsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        productRecyclerViewList.add(mCurrentProductListsRecyclerView);

        mCurrentRecyclerViewAdapter = new ProductListRecyclerViewAdapter(getActivity(),
                shoppingList.getProductsList(),
                shoppingList.getNumberOfProductsList());
        productListRecyclerViewAdapterList.add(mCurrentRecyclerViewAdapter);
        productRecyclerViewList.get(mIndex).setAdapter(productListRecyclerViewAdapterList.get(mIndex));

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
        final View view = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.edit_product_dialog, null);
        EditText newProductName = view.findViewById(R.id.product_name_edit_text);
        EditText newNumberOfProduct = view.findViewById(R.id.number_of_product_edit_text);
        newProductName.setHint("Name of new product");
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Add new product");
        alertDialog.setMessage("You can add the product you want to buy here.");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if (newProductName.getText().toString().isEmpty() && newNumberOfProduct.getText().toString().isEmpty())
                {
                    Toast.makeText(getActivity(), "The field must not be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    int currentPagerPosition = ProductsListViewPagerFragment.viewPager2.getCurrentItem();
                    int id = shoppingList.getId();
                    String NameList = shoppingList.getListName();
                    String Date = shoppingList.getDate().toString();
                    String productName = newProductName.getText().toString();
                    String numberOfProduct = newNumberOfProduct.getText().toString();
                    int numberOfProductsBought = shoppingList.getNumberOfProductsBought();

                    shoppingList.addProduct(productName, numberOfProduct);
                    SplashScreen.dbHandler.updateShoppingList(id, NameList, Date,
                            shoppingList.getProductsList(), shoppingList.getNumberOfProductsList(),
                            numberOfProductsBought, shoppingList.getIfBoughtProductList());
                    SplashScreen.getShoppingLists();

                    mShoppingLists = SplashScreen.mShoppingLists;

                    ProductListRecyclerViewAdapter newAdapter = new ProductListRecyclerViewAdapter(
                            getActivity(),
                            shoppingList.getProductsList(),
                            shoppingList.getNumberOfProductsList());

                    System.out.println(mShoppingLists.get(currentPagerPosition).getProductsList());
                    productListRecyclerViewAdapterList.set(currentPagerPosition, newAdapter);
                    productRecyclerViewList.get(currentPagerPosition).setAdapter(newAdapter);


                    Toast.makeText(getActivity(), "The product " + productName + " has been added", Toast.LENGTH_SHORT).show();
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
    public void onDestroyView()
    {
        super.onDestroyView();
        productListRecyclerViewAdapterList.clear();
        productRecyclerViewList.clear();
        System.out.println("Wyszedlem");
    }
}