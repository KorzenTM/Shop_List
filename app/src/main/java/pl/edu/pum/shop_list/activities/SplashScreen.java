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
import java.util.Date;
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

                    //if (mCursor.getCount() == 0)
                        //test_data(10);

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
            ShopListsFragment.mInformationAboutAddTextView.setVisibility(View.VISIBLE);
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
                String s = mCursor.getString(3);
                String formattedString = s.substring(1, s.length() - 1).replace(" ", "");
                List<String> productsList = new ArrayList<String>(Arrays.asList(formattedString.split(",")));
                shoppingList.setId(id);
                shoppingList.setListName(list_name);
                shoppingList.setDate(new Date(date));
                shoppingList.setProductList(productsList);
                mShoppingLists.add(shoppingList);
            }
        }
    }

    public void test_data(int how_many)
    {
        for(int i = 0; i <= how_many; i++)
        {
            ShoppingList shoppingList = new ShoppingList();
            shoppingList.setId(i);
            shoppingList.setListName("List " + i);
            shoppingList.setDate(new Date());
            dbHandler.addShoppingList(shoppingList);
        }
    }
}