package ua.deti.bulletjounal;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class Yearly_Log_Hub extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Dialog AddDialog;
    private Dialog Check;
    private ArrayList<HubItem> collections;
    private RecyclerView YearlyRecyclerView;
    private HubAdapter YearlyAdapter;
    private RecyclerView.LayoutManager collectionsLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yearly__log__hub);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);






        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(2).setChecked(true);
        createCollectionsList();
        buildRecyclerView();

        File myDir = getApplicationContext().getFilesDir();
        String path="";
        File documentsFolder = new File(myDir,path);
        File[] files = documentsFolder.listFiles();


        if (files==null){
            Toast.makeText(getBaseContext(),"Nada adicionado",Toast.LENGTH_SHORT).show();
        }
        else{
            for (File inFile : files) {
                if (inFile.isDirectory()) {
                    if(inFile.getName().matches("([0-9]{4})"))
                        insertItem(inFile.getName());
                }
            }
        }

        getSupportActionBar().setTitle("Yearly Hub");
    }

    public void createCollectionsList()
    {
        collections = new ArrayList<>();
    }

    public void  buildRecyclerView()
    {
        YearlyRecyclerView = findViewById(R.id.YearlyRecyclerView);
        YearlyRecyclerView.setHasFixedSize(true);
        collectionsLayoutManager = new GridLayoutManager(this, 2);
        YearlyAdapter = new HubAdapter(collections);
        YearlyRecyclerView.setLayoutManager(collectionsLayoutManager);
        YearlyRecyclerView.setAdapter(YearlyAdapter);

        YearlyAdapter.setOnItemClickListener(new HubAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent myIntent = new Intent(getBaseContext(),Yearly_Log.class);
                myIntent.putExtra("Month",collections.get(position).getItemName());
                startActivity(myIntent);

            }
        });

        YearlyAdapter.setOnItemLongClickListener(new HubAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                callCheckDialog(position);

            }
        });
    }


    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    private void callCheckDialog(final int position){
        Check = new Dialog(this);
        Check.setContentView(R.layout.pop_window_delete);
        Check.setCancelable(false);
        Check.setTitle("Add new Line");

        ImageView save = Check.findViewById(R.id.Yes);
        ImageView cancel = Check.findViewById(R.id.No);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File myDir = getApplicationContext().getFilesDir();
                String path="2019";
                File documentsFolder = new File(myDir,path);
                File[] files = documentsFolder.listFiles();

                removeItem(position);

                Toast.makeText(getBaseContext(),"fdfd"+files[position], Toast.LENGTH_SHORT).show();
                deleteFolder(files[position]);
                Check.dismiss();

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check.dismiss();

            }
        });


        Check.show();
    }

    public void insertItem(String inputText)
    {
        collections.add(new HubItem(inputText));
        YearlyAdapter.notifyItemInserted(collections.size()-1);
    }

    public void removeItem(int position)
    {
        collections.remove(position);
        YearlyAdapter.notifyItemRemoved(position);
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
        getMenuInflater().inflate(R.menu.yearly__log__hub, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



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

        }
        if(id != R.id.yearlyIcon)
            startActivity(myIntent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
