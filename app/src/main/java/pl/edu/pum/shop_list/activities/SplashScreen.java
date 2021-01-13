package pl.edu.pum.shop_list.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import pl.edu.pum.shop_list.R;

public class SplashScreen extends AppCompatActivity
{
    private static final int SPLASH_SCREEN_TIMEOUT = 2000;

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
                    //TODO read values from database
                    progressBar.setVisibility(View.VISIBLE);
                    Thread.sleep(SPLASH_SCREEN_TIMEOUT);
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
}