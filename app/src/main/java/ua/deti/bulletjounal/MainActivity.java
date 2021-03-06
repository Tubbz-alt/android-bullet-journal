package ua.deti.bulletjounal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

import org.threeten.bp.Year;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private  String MyPrefs ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        SharedPreferences sp = getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);
        if (!sp.getBoolean("first", false)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("first", true);
            editor.apply();
            Intent intent = new Intent(this, Intro.class); // Call the AppIntro java class
            startActivity(intent);
       }


   }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent myIntent = null;
        if (id == R.id.dailyIcon) {
            myIntent = new Intent(this, Daily_Log_Hub.class);;
        } else if (id == R.id.monthlyIcon) {
            myIntent = new Intent(this, Monthly_Log_Hub.class);
        } else if (id == R.id.yearlyIcon) {
            myIntent = new Intent(this, Yearly_Log_Hub.class);
        } else if (id == R.id.collectionIcon) {
            myIntent = new Intent(this, CollectionsHub.class);
        } else if(id == R.id.homeIcon) {
            myIntent = new Intent(this, MainActivity.class);
        } else if(id == R.id.helpIcon) {
            myIntent = new Intent(this, Intro.class);

        }
        if(id != R.id.homeIcon)
            startActivity(myIntent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeActivity(View view)
    {
        Intent intent = null;
        int view_id = view.getId();
        if(view_id == R.id.imageView11 || view_id == R.id.textView3)
        {
            intent = new Intent(this, Daily_Log_Hub.class);
        }
        else if(view_id == R.id.imageView12 || view_id == R.id.textView4)
        {
            intent = new Intent(this, Monthly_Log_Hub.class);
        }
        else if(view_id == R.id.imageView10 || view_id == R.id.textView5)
        {
            intent = new Intent(this, Yearly_Log_Hub.class);
        }
        else if(view_id == R.id.imageView6 || view_id == R.id.textView6)
        {
            intent = new Intent(this, CollectionsHub.class);
        }
        startActivity(intent);

    }
}
