package pl.edu.pum.shop_list.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import pl.edu.pum.shop_list.R;
import pl.edu.pum.shop_list.fragments.ShopListsFragment;
import pl.edu.pum.shop_list.handlers.DBHandler;
import pl.edu.pum.shop_list.models.ShoppingList;

public class SplashScreen extends AppCompatActivity
{
    public static List<ShoppingList> mShoppingLists = new ArrayList<>();
    public static DBHandler dbHandler;
    private static Cursor mCursor;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = findViewById(R.id.progress);

        ActivityStarter starter = new ActivityStarter();
        starter.start();
    }

    private class ActivityStarter extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                {
                    dbHandler = new DBHandler(getBaseContext());
                    mCursor = dbHandler.getShoppingLists();

                    getShoppingLists();
                    progressBar.setVisibility(View.VISIBLE);
                    Thread.sleep(1000);
                }
            }
            catch (Exception e)
            {
                Log.d("SplashScreen", e.getMessage());
            }

            progressBar.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(SplashScreen.this, ShoppingListsActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public static void getShoppingLists()
    {
        mShoppingLists.clear();
        mCursor = dbHandler.getShoppingLists();

        if (mCursor.getCount() == 0)
        {
            Log.d("DATABASE_STATUS", "EMPTY DATABASE");
        }
        else
        {
            Log.d("DATABASE_STATUS", "THERE IS DATA IN DATABASE");
            while (mCursor.moveToNext())
            {
                ShoppingList shoppingList = new ShoppingList();

                int id = mCursor.getInt(0);
                String list_name = mCursor.getString(1);
                String date = mCursor.getString(2);
                int productBought = mCursor.getInt(5);

                String s = mCursor.getString(3);
                List<String> productsList = new ArrayList<>();
                if (!s.equals("null"))
                {
                    String formattedString = s.substring(1, s.length() - 1).replace(" ", "");
                    productsList = new LinkedList<>(Arrays.asList(formattedString.split(",")));
                }

                String s2 = mCursor.getString((4));
                List<String> numberOfProducts = new ArrayList<>();
                if (!s2.equals("null"))
                {
                    String formattedString2 = s2.substring(1, s2.length() - 1).replace(" ", "");
                    numberOfProducts = new LinkedList<>(Arrays.asList(formattedString2.split(",")));
                }

                String s3 = mCursor.getString((6));
                List<String> ifBoughtProduct = new ArrayList<>();
                if (!s3.equals("null"))
                {
                    String formattedString3 = s3.substring(1, s3.length() - 1).replace(" ", "");
                    ifBoughtProduct = new LinkedList<>(Arrays.asList(formattedString3.split(",")));
                }

                shoppingList.setId(id);
                shoppingList.setListName(list_name);
                shoppingList.setDate(new Date(date));
                shoppingList.setProductList(productsList);
                shoppingList.setNumberOfProductsList(numberOfProducts);
                shoppingList.setNumberOfProductsBought(productBought);
                shoppingList.setIfBoughtProductList(ifBoughtProduct);
                mShoppingLists.add(shoppingList);
            }
        }
    }
}