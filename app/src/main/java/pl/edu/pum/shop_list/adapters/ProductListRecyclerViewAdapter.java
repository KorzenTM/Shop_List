package pl.edu.pum.shop_list.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pl.edu.pum.shop_list.R;
import pl.edu.pum.shop_list.models.ShoppingList;

public class ProductListRecyclerViewAdapter extends RecyclerView.Adapter<ProductListRecyclerViewAdapter.ViewHolder>
{
    private final FragmentActivity mContext;
    private final List<ShoppingList> mShoppingList;
    private List<String> mProductsList = new ArrayList<>();

    public ProductListRecyclerViewAdapter(FragmentActivity context, List<ShoppingList> ShoppingLists, List<String> products)
    {
        this.mContext = context;
        this.mShoppingList = ShoppingLists;
        this.mProductsList = products;
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
        final String currentProductList = mProductsList.get(position);
        holder.bind(currentProductList);
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
        public final CheckBox mIsBoughtCheckbox;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            mProductNameTextView = itemView.findViewById(R.id.product_name_text_view);
            mNumberOfProductTextView = itemView.findViewById(R.id.number_of_product_text_view);
            mIsBoughtCheckbox = itemView.findViewById(R.id.bought_checkbox);

            itemView.setOnClickListener(this);
        }

        @SuppressLint("SetTextI18n")
        public void bind(String productName)
        {
            mProductNameTextView.setText(productName);
            mNumberOfProductTextView.setText(Integer.toString(mProductsList.size()));
        }

        @Override
        public void onClick(View view)
        {

        }
    }
}
