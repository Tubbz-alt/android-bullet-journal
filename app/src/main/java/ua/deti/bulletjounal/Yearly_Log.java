package ua.deti.bulletjounal;

import android.app.Dialog;
import android.database.DataSetObserver;
import android.os.Bundle;
import java.util.Calendar;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Yearly_Log extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ExpandableListView listView;
    private Adapter_yearly listAdapter;
    private List<String> ListDataHeader;
    private HashMap<String,List<String>> listHash;
    private Dialog myDialog;



    private Map<String, ArrayList<Item_Yearly>> year_map=new HashMap()
    {{
        put("janeiro",new ArrayList<Item_Yearly>());
        put("fevereiro",new ArrayList<Item_Yearly>());
        put("março",new ArrayList<Item_Yearly>());
        put("abril",new ArrayList<Item_Yearly>());
        put("maio",new ArrayList<Item_Yearly>());
        put("junho",new ArrayList<Item_Yearly>());
        put("julho",new ArrayList<Item_Yearly>());
        put("augusto",new ArrayList<Item_Yearly>());
        put("setembro",new ArrayList<Item_Yearly>());
        put("outubro",new ArrayList<Item_Yearly>());
        put("novembro",new ArrayList<Item_Yearly>());
        put("dezembro",new ArrayList<Item_Yearly>());
    }};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yearly__log);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView add_info_year =  findViewById(R.id.add_info_year);
        add_info_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAddDialog();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        listView=(ExpandableListView)findViewById(R.id.Expandable_List);

        ListDataHeader=new ArrayList<>();
        listHash=new HashMap<>();

        ListDataHeader.add("Janeiro");
        ListDataHeader.add("Fevereiro");

        List<String>  Janeiro=new ArrayList<>();
        Janeiro.add("ola");


        List<String>  Fevereiro=new ArrayList<>();
        Janeiro.add("ola");
        Janeiro.add("adeus");

        listHash.put(ListDataHeader.get(0),Janeiro);
        listHash.put(ListDataHeader.get(1),Janeiro);





        listAdapter=new Adapter_yearly(this,ListDataHeader,listHash);
        listView.setAdapter(listAdapter);






    }

    public void removeItem(int position){

        //.remove(position);
        //mAdapter.notifyItemRemoved(position);
    }
    private void callAddDialog()
    {
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.pop_window_add_info_year);
        myDialog.setCancelable(false);
        myDialog.setTitle("Add new Line");

        //get the spinner from the xml.
        final Spinner dropdown = myDialog.findViewById(R.id.year_spinner);
        //create a list of items for the spinner.
        Set<String> keys = year_map.keySet();
        String[] array = keys.toArray(new String[keys.size()]);
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,array);;

        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        final Spinner dropdown_2 = myDialog.findViewById(R.id.day_spinner);
        //create a list of items for the spinner.
        //int
        //String[] array_2 = new String[Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)];
        //for(int i=0;i<)




        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,array);;

        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        Button save = (Button) myDialog.findViewById(R.id.Save);
        Button cancel = (Button) myDialog.findViewById(R.id.Cancel);



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText Description = (EditText) myDialog.findViewById(R.id.description);
                final  EditText Title = (EditText) myDialog.findViewById(R.id.title);

                String spinner_save = dropdown.getSelectedItem().toString();
                String description_save= (String) Description.getText().toString();
                String title_save= (String) Title.getText().toString();

                String to_display=spinner_save+"-"+title_save+"-"+description_save;


                Toast.makeText(getBaseContext(),"Done"  ,
                        Toast.LENGTH_LONG).show();

                //createNewTextView(to_display);


                //insertItem(0,to_display);
                //saveDB();
                myDialog.dismiss();


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();

            }
        });



        myDialog.show();





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
        getMenuInflater().inflate(R.menu.yearly__log, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
