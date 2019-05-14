package ua.deti.bulletjounal;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class Daily_Log_Hub extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Dialog AddDialog;
    private int button_id=0;
    private final Map<Integer,Integer> months_days=new HashMap()
    {{
        put(1, 31);
        put(2,28);
        put(3, 31);
        put(4, 30);
        put(5, 31);
        put(6, 30);
        put(7, 31);
        put(8, 31);
        put(9, 30);
        put(10, 31);
        put(11, 30);
        put(12, 31);
    }};

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily__log__hub);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

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

    private void callAddDialog()
    {
        AddDialog = new Dialog(this);
        AddDialog.setContentView(R.layout.pop_window_add_month);
        AddDialog.setCancelable(false);
        AddDialog.setTitle("Add new Line");

        //get the spinner from the xml.
        final Spinner dropdown = AddDialog.findViewById(R.id.spinner1);
        final Spinner dropdown2 = AddDialog.findViewById(R.id.spinner2);

        //create a list of items for the spinner.
        String[] items = new String[]{"2019"};

        String[] month=new String[5];
        //dicionario com key em numero e value em extenso

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);



        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int currMonth=calendar.get(Calendar.MONTH);
        int currDay=calendar.get(Calendar.DAY_OF_MONTH);

        int lastDay=months_days.get(currMonth+1);//

        int temp=lastDay-currDay;
        if(temp<5){

            int indice=0;
            for(int i=temp;i>=0;i--){
                month[indice]=currDay+indice+"/"+year_months.get(currMonth+1);
                indice++;

            }
            int new_day=1;
            if(indice<5){
                month[indice]=new_day+"/"+year_months.get(currMonth+2);
                indice++;
                new_day++;

            }

        }
        else{
            month[0]=currDay+"/"+year_months.get(currMonth+1);
            month[1]=currDay+1+"/"+year_months.get(currMonth+1);
            month[2]=currDay+2+"/"+year_months.get(currMonth+1);
            month[3]=currDay+3+"/"+year_months.get(currMonth+1);
            month[4]=currDay+4+"/"+year_months.get(currMonth+1);


        }



        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, month);
        //set the spinners adapter to the previously created one.
        dropdown2.setAdapter(adapter2);





        Button save = (Button) AddDialog.findViewById(R.id.Save);
        Button cancel = (Button) AddDialog.findViewById(R.id.Cancel);



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
                    createNewTextView(spinner_2);
                    AddDialog.dismiss();


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

    private void createNewTextView(final String text) {

        LayoutInflater inflater=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(this);
        final Button more =new Button(this);
        more.setText(text);
        more.setId(button_id);
        final int btn_id=more.getId(); //get the button id so I can associate a function

        mLayout.addView(more);
        Button btn= (Button)findViewById(btn_id);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getBaseContext(),Monthly_Log_Notes.class);
                myIntent.putExtra("Month",text);
                startActivity(myIntent);
            }
        });


        button_id+=1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.daily__log__hub, menu);
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
