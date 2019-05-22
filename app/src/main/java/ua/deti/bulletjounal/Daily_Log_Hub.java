package ua.deti.bulletjounal;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.zip.InflaterOutputStream;

public class Daily_Log_Hub extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Dialog AddDialog;
    private Dialog Check;
    private ArrayList<HubItem> collections;
    private RecyclerView DailyRecyclerView;
    private HubAdapter DailyAdapter;
    private RecyclerView.LayoutManager collectionsLayoutManager;
    private int button_id=0;
    private String [] days;
    private TextView show;
    private Set<String> month_display=new HashSet<>();
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

        populate_month();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setChecked(true);
        createCollectionsList();
        buildRecyclerView();

        for(String b: days){
            String [] tokens=b.split("/");
            month_display.add(tokens[1]);

        }

        File myDir = getApplicationContext().getFilesDir();
        show=(TextView)findViewById(R.id.textView7) ;
        Iterator it=month_display.iterator();
        while (it.hasNext()){

            String path="2019/"+it.next();
            File documentsFolder = new File(myDir,path);
            File[] files = documentsFolder.listFiles();

            if (files==null){


            }
            else{


                for (File inFile : files) {
                    if (inFile.getName().startsWith("Calendar")){
                        //Toast.makeText(getBaseContext(),inFile.getName(),Toast.LENGTH_SHORT).show();
                        String fileName=inFile.getName();
                        if (fileName.indexOf(".") > 0)
                            fileName = fileName.substring(0, fileName.lastIndexOf("."));
                        insertItem(fileName);
                    }


                }
            }

        }
        if(collections.size()==0){
            show.setText("Empty! Add something :)");
        }
        else
            show.setText("");


        getSupportActionBar().setTitle("Daily Hub");
        ImageView addDay = findViewById(R.id.addDayBtn);

        addDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddDialog();
            }
        });




    }

    public void createCollectionsList()
    {
        collections = new ArrayList<>();
    }

    public void  buildRecyclerView()
    {
        DailyRecyclerView = findViewById(R.id.DailyRecyclerView);
        DailyRecyclerView.setHasFixedSize(true);
        collectionsLayoutManager = new GridLayoutManager(this, 2);
        DailyAdapter = new HubAdapter(collections);
        DailyRecyclerView.setLayoutManager(collectionsLayoutManager);
        DailyRecyclerView.setAdapter(DailyAdapter);

        DailyAdapter.setOnItemClickListener(new HubAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent myIntent = new Intent(getBaseContext(),Daily_Log.class);
                myIntent.putExtra("Day",collections.get(position).getItemName());
                startActivity(myIntent);

            }
        });

        DailyAdapter.setOnItemLongClickListener(new HubAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                callCheckDialog(position);

            }
        });
    }

    public void insertItem(String inputText)
    {
        String [] tokens=inputText.split("_");

        collections.add(new HubItem(tokens[2]+" "+tokens[1]));
        DailyAdapter.notifyItemInserted(collections.size()-1);
        if(collections.size()==0){
            show.setText("Empty! Add something :)");
        }
        else
            show.setText("");
    }

    public void removeItem(int position)
    {

        collections.remove(position);
        DailyAdapter.notifyItemRemoved(position);
        if(collections.size()==0){
            show.setText("Empty! Add something :)");
        }
        else
            show.setText("");
    }

    private void callCheckDialog(final int position){
        Check = new Dialog(this);
        Check.setContentView(R.layout.pop_window_delete);
        Check.setCancelable(false);
        Check.setTitle("Add new Line");

        Button save = (Button) Check.findViewById(R.id.Yes);
        Button cancel = (Button) Check.findViewById(R.id.No);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File myDir = getApplicationContext().getFilesDir();
                String [] tokens=collections.get(position).getItemName().split(" ");
                String day=tokens[0];
                String month=tokens[1];
                String filename="Calendar_"+month+"_"+day+".txt";
                String path=2019+"/"+month;
                File documentsFolder = new File(myDir,path);
                File myfile=new File(documentsFolder,filename);
                myfile.delete();

                removeItem(position);


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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void populate_month(){
        days=new String[5];

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int currMonth=calendar.get(Calendar.MONTH);
        int currDay=calendar.get(Calendar.DAY_OF_MONTH);

        int lastDay=months_days.get(currMonth+1);//

        int temp=lastDay-currDay;
        if(temp<5){

            int indice=0;
            for(int i=temp;i>=0;i--){
                days[indice]=currDay+indice+"/"+year_months.get(currMonth+1);
                indice++;

            }
            int new_day=1;
            if(indice<5){
                days[indice]=new_day+"/"+year_months.get(currMonth+2);
                indice++;
                new_day++;

            }

        }
        else{
            days[0]=currDay+"/"+year_months.get(currMonth+1);
            days[1]=currDay+1+"/"+year_months.get(currMonth+1);
            days[2]=currDay+2+"/"+year_months.get(currMonth+1);
            days[3]=currDay+3+"/"+year_months.get(currMonth+1);
            days[4]=currDay+4+"/"+year_months.get(currMonth+1);


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

        //dicionario com key em numero e value em extenso

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);







        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, days);
        //set the spinners adapter to the previously created one.
        dropdown2.setAdapter(adapter2);





        ImageView save = AddDialog.findViewById(R.id.Save);
        ImageView cancel = AddDialog.findViewById(R.id.Cancel);



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String spinner_1 = dropdown.getSelectedItem().toString();
                String spinner_2 = dropdown2.getSelectedItem().toString();

                String [] tokens=spinner_2.split("/");
                String day=tokens[0];
                String month=tokens[1];





                File myDir = getApplicationContext().getFilesDir();
                String filename="Calendar_"+month+"_"+day+".txt";
                String path=spinner_1+"/"+month;
                File documentsFolder = new File(myDir,path);
                if(!documentsFolder.exists())
                    documentsFolder.mkdir();
                File myfile=new File(documentsFolder,filename);


                if(!myfile.exists()){
                    try{
                        myfile.createNewFile();
                        if (filename.indexOf(".") > 0)
                            filename = filename.substring(0, filename.lastIndexOf("."));
                        insertItem(filename);
                        AddDialog.dismiss();
                    }catch (IOException e){
                        e.printStackTrace();
                    }



                }
                else{
                    Toast.makeText(getBaseContext(),"Daily Log for that day already exists choose another day or year"  ,
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
        if(id != R.id.dailyIcon)
            startActivity(myIntent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
