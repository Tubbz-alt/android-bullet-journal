package ua.deti.bulletjounal;

import android.app.Dialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.icu.text.UnicodeSetSpanner;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.time.YearMonth;

public class Yearly_Log extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ExpandableListView listView;
    private Adapter_yearly listAdapter;
    private List<String> ListDataHeader;
    private HashMap<String,List<Item_Yearly>> listHash;
    private Dialog myDialog;
    public ImageView mDeleteImage;




    private Map<String, ArrayList<Item_Yearly>> year_map=new LinkedHashMap<String, ArrayList<Item_Yearly>>()
    {{
        put("Janeiro",new ArrayList<Item_Yearly>());
        put("Fevereiro",new ArrayList<Item_Yearly>());
        put("Março",new ArrayList<Item_Yearly>());
        put("Abril",new ArrayList<Item_Yearly>());
        put("Maio",new ArrayList<Item_Yearly>());
        put("Junho",new ArrayList<Item_Yearly>());
        put("Julho",new ArrayList<Item_Yearly>());
        put("Augusto",new ArrayList<Item_Yearly>());
        put("Setembro",new ArrayList<Item_Yearly>());
        put("Outubro",new ArrayList<Item_Yearly>());
        put("Novembro",new ArrayList<Item_Yearly>());
        put("Dezembro",new ArrayList<Item_Yearly>());
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
        navigationView.getMenu().getItem(4).setChecked(true);


        listView=(ExpandableListView)findViewById(R.id.Expandable_List);

        ListDataHeader=new ArrayList<>();
        listHash=new HashMap<>();

        for (String b:year_map.keySet()
             ) {
            ListDataHeader.add(b);
            List<Item_Yearly> list=new ArrayList<>();
            listHash.put(b,list);

        }











        listAdapter=new Adapter_yearly(this,ListDataHeader,listHash);
        listView.setAdapter(listAdapter);





        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, final int groupPosition, final int childPosition, long id) {
                mDeleteImage=v.findViewById(R.id.deleteButton_yearly);
                mDeleteImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listHash.get(ListDataHeader.get(groupPosition)).get(childPosition).setShow(false);

                        listAdapter.notifyDataSetChanged();
                    }
                });
                return false;
            }
        });





    }

    /*public void removeItem(int position){
        .remove(position);
        mAdapter.notifyItemRemoved(position);
    }*/
    private void callAddDialog()
    {
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.pop_window_add_info_year);
        myDialog.setCancelable(false);
        myDialog.setTitle("Add new Line");

        //get the spinner from the xml.
        final Spinner dropdown = myDialog.findViewById(R.id.month_spinner);
        final Spinner dropdown_2 = myDialog.findViewById(R.id.day_spinner);
        //create a list of items for the spinner.
        Set<String> keys = year_map.keySet();
        final String[] array = keys.toArray(new String[keys.size()]);
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,array);;

        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                Calendar mycal = new GregorianCalendar(2019, position, 1);
                int daysMonth=mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                Toast.makeText(getBaseContext(),"Done"+daysMonth  ,
                        Toast.LENGTH_LONG).show();

                String [] days=new String[daysMonth];

                for(int i=0;i<daysMonth;i++){
                    days[i]=Integer.toString(i+1);
                }

                ArrayAdapter<String> adapter_b = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item,days);;
                dropdown_2.setAdapter(adapter_b);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        Button save = (Button) myDialog.findViewById(R.id.Save);
        Button cancel = (Button) myDialog.findViewById(R.id.Cancel);



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final  EditText Title = (EditText) myDialog.findViewById(R.id.editText_Year);

                String spinner_month = dropdown.getSelectedItem().toString();
                String spinner_day = dropdown_2.getSelectedItem().toString();
                String Title_text=Title.getText().toString();





                //String to_display=spinner_save+"-"+title_save+"-"+description_save;






                insertItem(spinner_month,spinner_day,Title_text);
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

    public void insertItem(String Month,String day,String title){
        listHash.get(Month).add(new Item_Yearly(day,title,true));
        listAdapter.notifyDataSetChanged();

        listView.expandGroup(ListDataHeader.indexOf(Month));
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
        startActivity(myIntent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
