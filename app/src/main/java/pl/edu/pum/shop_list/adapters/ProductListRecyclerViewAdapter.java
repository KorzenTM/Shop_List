package pl.edu.pum.shop_list.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pl.edu.pum.shop_list.R;
import pl.edu.pum.shop_list.activities.SplashScreen;
import pl.edu.pum.shop_list.fragments.ProductsListFragment;
import pl.edu.pum.shop_list.fragments.ProductsListViewPagerFragment;
import pl.edu.pum.shop_list.models.ShoppingList;

public class ProductListRecyclerViewAdapter extends RecyclerView.Adapter<ProductListRecyclerViewAdapter.ViewHolder>
{
    private final FragmentActivity mContext;
    private List<String> mProductsList;
    private List<String> mNumberOfProductList;

    public ProductListRecyclerViewAdapter(FragmentActivity context, List<String> products, List<String> number_of_products)
    {
        this.mContext = context;
        this.mProductsList = products;
        this.mNumberOfProductList = number_of_products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ProductListRecyclerViewAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.product_list_item,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListRecyclerViewAdapter.ViewHolder holder, int position)
    {
        final String currentProductName = mProductsList.get(position);
        final String currentNumberOfProduct = mNumberOfProductList.get(position);
        int currentPagerPosition = ProductsListViewPagerFragment.viewPager2.getCurrentItem();
        ShoppingList currentShoppingList = SplashScreen.mShoppingLists.get(currentPagerPosition);
        Boolean ifBought = Boolean.parseBoolean(currentShoppingList.getIfBoughtProductList().get(position));

        holder.bind(currentProductName, currentNumberOfProduct, Boolean.parseBoolean(currentShoppingList.getIfBoughtProductList().get(position)));

        holder.setBackgroundDependsOfCheckbox(ifBought);

        holder.mIsBoughtCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                int id = currentShoppingList.getId();
                String NameList = currentShoppingList.getListName();
                String Date = currentShoppingList.getDate().toString();
                String productName = currentProductName;
                String numberOfProduct = currentNumberOfProduct;
                if (b)
                {
                    holder.setBackgroundDependsOfCheckbox(true);
                    currentShoppingList.incrementNumberOfProductsBought();
                    currentShoppingList.getIfBoughtProductList().set(position, "true");
                }
                else
                {
                    holder.setBackgroundDependsOfCheckbox(false);
                    currentShoppingList.getIfBoughtProductList().set(position, "false");
                    currentShoppingList.decrementNumberOfProductsBought();
                }

                int numberOfProductBought = SplashScreen.mShoppingLists.get(currentPagerPosition).getNumberOfProductsBought();
                SplashScreen.dbHandler.updateShoppingList(id, NameList, Date, mProductsList,
                        mNumberOfProductList, numberOfProductBought, currentShoppingList.getIfBoughtProductList());
                SplashScreen.getShoppingLists();
                notifyDataSetChanged();
            }
        });

        holder.mEditButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int currentPagerPosition = ProductsListViewPagerFragment.viewPager2.getCurrentItem();
                System.out.println("Strona: " + currentPagerPosition);
                showEditDialog(SplashScreen.mShoppingLists.get(currentPagerPosition), position, currentPagerPosition);

            }
        });
    }

    private void showEditDialog(ShoppingList currentShoppingList, int RecyclerViewPosition, int ViewPagerSite)
    {
        final View view = Objects.requireNonNull(mContext).getLayoutInflater().inflate(R.layout.edit_product_dialog, null);
        EditText newProductName = view.findViewById(R.id.product_name_edit_text);
        EditText newNumberOfProduct = view.findViewById(R.id.number_of_product_edit_text);
        newProductName.setText(currentShoppingList.getProductsList().get(RecyclerViewPosition));
        newNumberOfProduct.setText(currentShoppingList.getNumberOfProductsList().get(RecyclerViewPosition));
        newProductName.setHint("Edit product");
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Edit product");
        alertDialog.setMessage("You can edit the product here.");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Edit", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if (newProductName.getText().toString().isEmpty() && newNumberOfProduct.getText().toString().isEmpty())
                {
                    Toast.makeText(mContext, "The field must not be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    int id = currentShoppingList.getId();
                    String NameList = currentShoppingList.getListName();
                    String Date = currentShoppingList.getDate().toString();
                    String productName = newProductName.getText().toString();
                    String numberOfProduct = newNumberOfProduct.getText().toString();
                    int numberOfProductBought = currentShoppingList.getNumberOfProductsBought();

                    mProductsList.set(RecyclerViewPosition, productName);
                    mNumberOfProductList.set(RecyclerViewPosition, numberOfProduct);

                    SplashScreen.dbHandler.updateShoppingList(id, NameList, Date, mProductsList,
                            mNumberOfProductList, numberOfProductBought, currentShoppingList.getIfBoughtProductList());
                    SplashScreen.getShoppingLists();

                    ProductListRecyclerViewAdapter newAdapter = new ProductListRecyclerViewAdapter(mContext, mProductsList, mNumberOfProductList);
                    ProductsListFragment.productListRecyclerViewAdapterList.set(ViewPagerSite, newAdapter);
                    ProductsListFragment.productRecyclerViewList.get(ViewPagerSite).setAdapter(newAdapter);

                    Toast.makeText(mContext, "The product has been updated", Toast.LENGTH_SHORT).show();
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
    public int getItemCount()
    {
        return mProductsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public final TextView mProductNameTextView;
        public final TextView mNumberOfProductTextView;
        public final Button mEditButton;
        public final CheckBox mIsBoughtCheckbox;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            mProductNameTextView = itemView.findViewById(R.id.product_name_text_view);
            mNumberOfProductTextView = itemView.findViewById(R.id.number_of_product_text_view);
            mIsBoughtCheckbox = itemView.findViewById(R.id.bought_checkbox);
            mEditButton = itemView.findViewById(R.id.edit_button);

            itemView.setOnClickListener(this);
        }

        @SuppressLint("SetTextI18n")
        public void bind(String productName, String how_many, Boolean ifBought)
        {
            mProductNameTextView.setText(productName);
            mNumberOfProductTextView.setText(how_many);

            setBackgroundDependsOfCheckbox(ifBought);
        }

        public void setBackgroundDependsOfCheckbox(Boolean b)
        {
            if (b)
            {
                mIsBoughtCheckbox.setChecked(true);
                itemView.setBackgroundColor(Color.GRAY);
                mEditButton.setEnabled(false);
            }
            else
            {
                mIsBoughtCheckbox.setChecked(false);
                itemView.setBackgroundColor(Color.WHITE);
                mEditButton.setEnabled(true);
            }
        }

        @Override
        public void onClick(View view)
        {

        }
    }
}
