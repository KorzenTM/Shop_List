package pl.edu.pum.shop_list.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

import pl.edu.pum.shop_list.R;
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
                                Toast.makeText(mContext, "Option1", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.menu2:
                                Toast.makeText(mContext, "Option2", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

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
            mNumberOfIngredientsTextView = itemView.findViewById(R.id.number_of_ingredients_text_view);
            mCreatedDateTextView = itemView.findViewById(R.id.time_of_creation_text_view);
            mOptionsButton = itemView.findViewById(R.id.options_text_view);

            itemView.setOnClickListener(this);
        }

        public void bind(ShoppingList currentShoppingList)
        {
            mListNameTextView.setText(currentShoppingList.getListName());
            mCreatedDateTextView.setText(currentShoppingList.getDate().toString());
            //TODO add number of ingredients after implementation
            mNumberOfIngredientsTextView.setText("15/20");
        }

        @Override
        public void onClick(View view)
        {
            //TODO after click ingredients list will be show
            Toast.makeText(mContext, "Clicked list number " + mShoppingList.get(getAbsoluteAdapterPosition()).getId() , Toast.LENGTH_LONG).show();
        }
    }
}
