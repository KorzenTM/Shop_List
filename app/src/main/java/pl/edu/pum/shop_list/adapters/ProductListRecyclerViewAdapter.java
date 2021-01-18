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
    private List<String> mIfBoughtList;

    public ProductListRecyclerViewAdapter(FragmentActivity context, List<String> products, List<String> number_of_products, List<String> ifBought)
    {
        this.mContext = context;
        this.mProductsList = products;
        this.mNumberOfProductList = number_of_products;
        this.mIfBoughtList = ifBought;
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
        final String ifBought = mIfBoughtList.get(position);



        holder.bind(currentProductName, currentNumberOfProduct, ifBought);

        holder.mIsBoughtCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                int currentPagerPosition = ProductsListViewPagerFragment.viewPager2.getCurrentItem();
                ShoppingList currentShoppingList = SplashScreen.mShoppingLists.get(currentPagerPosition);

                int id = currentShoppingList.getId();
                String NameList = currentShoppingList.getListName();
                String Date = currentShoppingList.getDate().toString();

                if (b)
                {
                    holder.setBackgroundDependsOfCheckbox("true");
                    currentShoppingList.incrementNumberOfProductsBought();
                    mIfBoughtList.set(position, "true");
                }
                else
                {
                    holder.setBackgroundDependsOfCheckbox("false");
                    currentShoppingList.decrementNumberOfProductsBought();
                    mIfBoughtList.set(position, "false");
                }

                int numberOfProductBought = currentShoppingList.getNumberOfProductsBought();
                SplashScreen.dbHandler.updateShoppingList(id, NameList, Date, mProductsList,
                        mNumberOfProductList, numberOfProductBought, mIfBoughtList);
                SplashScreen.getShoppingLists();

                setNewAdapter(currentPagerPosition);
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

                    setNewAdapter(ViewPagerSite);

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

    void setNewAdapter(int index)
    {
        ProductListRecyclerViewAdapter newAdapter = new ProductListRecyclerViewAdapter(mContext, mProductsList, mNumberOfProductList, mIfBoughtList);
        ProductsListFragment.productListRecyclerViewAdapterList.set(index, newAdapter);
        ProductsListFragment.productRecyclerViewList.get(index).setAdapter(newAdapter);
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
        public void bind(String productName, String how_many, String ifBought)
        {
            mProductNameTextView.setText(productName);
            mNumberOfProductTextView.setText(how_many);

            setBackgroundDependsOfCheckbox(ifBought);
        }

        public void setBackgroundDependsOfCheckbox(String b)
        {
            if (b.equals("true"))
            {
                mIsBoughtCheckbox.setChecked(true);
                itemView.setBackgroundColor(Color.GRAY);
                mEditButton.setEnabled(false);
            }
            else if (b.equals("false"))
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
