package ua.deti.bulletjounal;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;
import java.util.TimeZone;


import java.io.File;


public class Monthly_Log_Hub extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Dialog AddDialog;
    private Dialog Check;

    private static final String TAG = "CollectionsHub";
    private ArrayList<HubItem> collections;
    private RecyclerView MonthlyHubRecyclerView;
    private HubAdapter MonthlyHubAdapter;
    private RecyclerView.LayoutManager collectionsLayoutManager;


    private int button_id=0;
    private final Map<Integer,String> year_months=new HashMap()
    {{
        put(1, "January");
        put(2,"February");
        put(3, "March");
        put(4, "April");
        put(5, "May");
        put(6, "June");
        put(7, "July");
        put(8, "August");
        put(9, "September");
        put(10, "October");
        put(11, "November");
        put(12, "December");
    }};
    private LinearLayout mLayout;
    private TextView show;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly__log__hub);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Monthly Log Hub");



        show=(TextView)findViewById(R.id.textView7) ;

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
        String path="2019";
        File documentsFolder = new File(myDir,path);
        File[] files = documentsFolder.listFiles();
        if (files==null){
            Toast.makeText(getBaseContext(),"Nada adicionado",Toast.LENGTH_SHORT).show();
        }
        else{
            for (File inFile : files) {
                if (inFile.isDirectory()) {
                    //Toast.makeText(getBaseContext(),inFile.getName(),Toast.LENGTH_SHORT).show();
                    insertItem(inFile.getName());
                }
            }
        }

        ImageView addMonthBtn = findViewById(R.id.addMonth);

        addMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddDialog();
            }
        });


        if(collections.size()==0){
            show.setText("Empty! Add something :)");
        }
        else
            show.setText("");








    }

    public void createCollectionsList()
    {
        collections = new ArrayList<>();
    }

    public void  buildRecyclerView()
    {
        MonthlyHubRecyclerView = findViewById(R.id.MonthlyHubRecyclerView);
        MonthlyHubRecyclerView.setHasFixedSize(true);
        collectionsLayoutManager = new GridLayoutManager(this, 2);
        MonthlyHubAdapter = new HubAdapter(collections);
        MonthlyHubRecyclerView.setLayoutManager(collectionsLayoutManager);
        MonthlyHubRecyclerView.setAdapter(MonthlyHubAdapter);

        MonthlyHubAdapter.setOnItemClickListener(new HubAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent myIntent = new Intent(getBaseContext(),Monthly_Log_Notes.class);
                myIntent.putExtra("Month",collections.get(position).getItemName());
                startActivity(myIntent);

            }
        });

        MonthlyHubAdapter.setOnItemLongClickListener(new HubAdapter.OnItemLongClickListener() {
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
        Check.setCancelable(true);
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

                deleteFolder(files[position]);
                Check.dismiss();
                Toast.makeText(getBaseContext(),files[position].getName()+" removed.", Toast.LENGTH_SHORT).show();


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
        MonthlyHubAdapter.notifyDataSetChanged();

        if(collections.size()==0){
            show.setText("Empty! Add something :)");
        }
        else
            show.setText("");
    }

    public void removeItem(int position)
    {
        collections.remove(position);
        MonthlyHubAdapter.notifyItemRemoved(position);

        if(collections.size()==0){
            show.setText("Empty! Add something :)");
        }
        else
            show.setText("");
    }

    private void callAddDialog()
    {
        AddDialog = new Dialog(this);
        AddDialog.setContentView(R.layout.pop_window_add_month);
        AddDialog.setCancelable(true);
        AddDialog.setTitle("Add new Line");

        //get the spinner from the xml.
        final Spinner dropdown = AddDialog.findViewById(R.id.spinner1);
        final Spinner dropdown2 = AddDialog.findViewById(R.id.spinner2);

        //create a list of items for the spinner.
        String[] items = new String[]{"2019"};
        String[] month=new String[3];
        //dicionario com key em numero e value em extenso

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);



        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int currMonth=calendar.get(Calendar.MONTH);
        month[0]=year_months.get(currMonth+1);
        month[1]=year_months.get(currMonth+2);
        month[2]=year_months.get(currMonth+3);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, month);
        //set the spinners adapter to the previously created one.
        dropdown2.setAdapter(adapter2);





        ImageView save = AddDialog.findViewById(R.id.Save);
        ImageView cancel = AddDialog.findViewById(R.id.Cancel);



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String spinner_1 = dropdown.getSelectedItem().toString();
                String spinner_2 = dropdown2.getSelectedItem().toString();





                File myDir = getApplicationContext().getFilesDir();
                String path=spinner_1+"/"+spinner_2;
                File documentsFolder = new File(myDir,path);

                if(!documentsFolder.exists()){
                    documentsFolder.mkdirs();
                    insertItem(spinner_2);
                    AddDialog.dismiss();
                    Toast.makeText(getBaseContext(),spinner_2+" added." ,
                            Toast.LENGTH_LONG).show();



                }
                else{
                    Toast.makeText(getBaseContext(),"Monthly Log for that month already exists choose another month or year"  ,
                            Toast.LENGTH_LONG).show();
                }



            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDialog.dismiss();

            }
        });



        AddDialog.show();





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
        getMenuInflater().inflate(R.menu.monthly__log__hub, menu);
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
            myIntent = new Intent(this, Intro.class);

        }
        if(id != R.id.monthlyIcon)
            startActivity(myIntent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
