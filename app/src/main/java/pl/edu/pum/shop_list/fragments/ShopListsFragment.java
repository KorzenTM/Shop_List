package pl.edu.pum.shop_list.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

        mAddListFAB.setOnClickListener(new View.OnClickListener()
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
        EditText mEditNameEditText = view.findViewById(R.id.list_name_edit_text);
        mEditNameEditText.setHint("Name of new list");
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Add new list");
        alertDialog.setMessage("You can add your new shopping list here.");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if (mEditNameEditText.getText().toString().isEmpty())
                {
                    Toast.makeText(getActivity(), "The field must not be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String name = mEditNameEditText.getText().toString();
                    ShoppingList shoppingList = new ShoppingList();
                    shoppingList.setId(mShoppingLists.size());
                    shoppingList.setDate(new Date());
                    shoppingList.setListName(name);
                    SplashScreen.dbHandler.addShoppingList(shoppingList);
                    SplashScreen.getShoppingLists();
                    mShoppingListRecyclerViewAdapter.notifyItemInserted(mShoppingLists.size() - 1);
                    mShoppingListsRecyclerView.scrollToPosition(mShoppingLists.size() - 1);
                    Toast.makeText(getActivity(), "The list " + name + " has been added", Toast.LENGTH_SHORT).show();
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