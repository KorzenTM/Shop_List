package pl.edu.pum.shop_list.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

import pl.edu.pum.shop_list.R;
import pl.edu.pum.shop_list.activities.ProductsListViewPagerActivity;
import pl.edu.pum.shop_list.activities.SplashScreen;
import pl.edu.pum.shop_list.fragments.ProductsListFragment;
import pl.edu.pum.shop_list.models.ShoppingList;

public class ShoppingListRecyclerViewAdapter extends RecyclerView.Adapter<ShoppingListRecyclerViewAdapter.ViewHolder>
{
    private final FragmentActivity mContext;
    private final List<ShoppingList> mShoppingList;

    public ShoppingListRecyclerViewAdapter(FragmentActivity context, List<ShoppingList> ShoppingLists)
    {
        this.mContext = context;
        this.mShoppingList = ShoppingLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.shopping_list_item,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        final ShoppingList currentShoppingList = mShoppingList.get(position);
        holder.bind(currentShoppingList);

        holder.mOptionsButton.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view)
            {
                PopupMenu popupMenu = new PopupMenu(mContext, holder.mOptionsButton);
                popupMenu.inflate(R.layout.options_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem)
                    {
                        switch (menuItem.getItemId())
                        {
                            case R.id.menu1:
                                showEditDialog(currentShoppingList);
                                break;
                            case R.id.menu2:
                                SplashScreen.dbHandler.deleteShoppingList(currentShoppingList.getListName());
                                SplashScreen.getShoppingLists();
                                notifyDataSetChanged();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

    }

    private void showEditDialog(ShoppingList currentShoppingList)
    {
        final View view = mContext.getLayoutInflater().inflate(R.layout.edit_list_dialog, null);
        EditText mEditNameEditText = view.findViewById(R.id.list_name_edit_text);
        mEditNameEditText.setText(currentShoppingList.getListName());
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Edit name");
        alertDialog.setMessage("You can rename your shopping list here.");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if (mEditNameEditText.getText().toString().isEmpty())
                {
                    Toast.makeText(mContext, "The field must not be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String name = mEditNameEditText.getText().toString();
                    SplashScreen.dbHandler.updateShoppingList(currentShoppingList.getId(),
                            name, currentShoppingList.getDate().toString(), null,
                            null, 0);
                    SplashScreen.getShoppingLists();
                    notifyDataSetChanged();
                    Toast.makeText(mContext, "Name has been updated", Toast.LENGTH_SHORT).show();
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
        return mShoppingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public final TextView mListNameTextView;
        public final TextView mNumberOfIngredientsTextView;
        public final TextView mCreatedDateTextView;
        public final TextView mOptionsButton;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            mListNameTextView = itemView.findViewById(R.id.list_name_text_view);
            mNumberOfIngredientsTextView = itemView.findViewById(R.id.how_many_bought_text_view);
            mCreatedDateTextView = itemView.findViewById(R.id.time_of_creation_text_view);
            mOptionsButton = itemView.findViewById(R.id.options_text_view);

            itemView.setOnClickListener(this);
        }

        public void bind(ShoppingList currentShoppingList)
        {
            mListNameTextView.setText(currentShoppingList.getListName());
            mCreatedDateTextView.setText(currentShoppingList.getDate().toString());
            String numbers = String.valueOf(currentShoppingList.getNumberOfProductsBought()
                    + "/" +
                    currentShoppingList.getNumberOfProductsList().size());
            mNumberOfIngredientsTextView.setText(numbers);
        }

        @Override
        public void onClick(View view)
        {
            Intent intent = new Intent(mContext, ProductsListViewPagerActivity.class);
            ProductsListFragment.CurrentPosition = getAbsoluteAdapterPosition();
            mContext.startActivity(intent);
        }
    }
}
