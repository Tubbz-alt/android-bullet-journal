package ua.deti.bulletjounal;

import android.app.Dialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Daily_Log extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String filename="example.txt";
    private RecyclerView recyclerView;
    private TextView cross;
    private Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //private LinearLayout mLayout;
    private ImageView addInfo;
    private TextView Text;
    private TextView Text_Day_Week;
    private TextView Text_Day_Month;
    private Dialog myDialog;
    private int button_id=0;
    private String currDay;
    private String currMonth;
    private ArrayList<Item> exampleList=new ArrayList<>();

    private Map<Integer,String> db=new HashMap<>();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily__log);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new Adapter(exampleList);
        recyclerView.setAdapter(mAdapter);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        String temp=getIntent().getStringExtra("Day");
        String [] tokens=temp.split(" ");
        currDay=tokens[0];
        currMonth=tokens[1];
        Date date=null;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            date = dateFormat.parse("2019-05-"+currDay+" 13:45:20");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            Calendar c = Calendar.getInstance();

            DateFormatSymbols dfs = new DateFormatSymbols();



            Text_Day_Week=(TextView)findViewById(R.id.textView8);
            Text_Day_Week.setText(dfs.getWeekdays()[calendar.get(Calendar.DAY_OF_WEEK)]);
            Text_Day_Month=(TextView)findViewById(R.id.textView9);
            Text_Day_Month.setText(temp);

        }catch (ParseException e){
            e.printStackTrace();
        }





        mAdapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void OnDelete(int position) {
                removeItem(position);
                saveDB();
            }



            @Override
            public void onItemclick(int position) {
                callMoreDialog(position);

            }

            @Override
            public void onCheckbox(int position, TextView cross, ImageView change, CheckBox check) {
                if(check.isChecked()){
                    cross.setPaintFlags(cross.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                    change.setImageResource(R.drawable.done_icon);

                }
                else{
                    cross.setPaintFlags(cross.getPaintFlags()& ~Paint.STRIKE_THRU_TEXT_FLAG);
                    Toast.makeText(getBaseContext(),exampleList.get(position).getStringType() ,
                            Toast.LENGTH_LONG).show();
                    switch (exampleList.get(position).getStringType()){
                        case "Task":

                            change.setImageResource(R.drawable.task_icon);
                            break;
                        case "Event":
                            change.setImageResource(R.drawable.event_icon);
                            break;
                        case "Note":
                            change.setImageResource(R.drawable.note_icon);
                            break;

                    }

                }
            }
        });






        //mLayout= (LinearLayout) findViewById(R.id.linearLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Spinner filter=toolbar.findViewById(R.id.filter_spinner);
        final String[] items = new String[]{"Todos","Task","Event","Note"};


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        filter.setAdapter(adapter);

        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String op=items[position];
                Iterator it = exampleList.iterator();

                while (it.hasNext()){
                    Item item=(Item)it.next();
                    int pos=exampleList.indexOf(item);

                    if(op=="Todos" & item.getShow()==true)
                        break;
                    else if(op=="Todos" & item.getShow()==false){
                        Toast.makeText(getBaseContext(),"hidden", Toast.LENGTH_SHORT).show();
                        item.setShow(true);
                    }
                    else if(!item.getStringType().equals(op)){
                        Toast.makeText(getBaseContext(),"diff"+item.getStringType()+"-"+op, Toast.LENGTH_SHORT).show();

                        item.setShow(false);

                    }
                    else{
                        item.setShow(true);

                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addInfo=findViewById(R.id.Add_Info);


        addInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLoginDialog();
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //currMonth=getIntent().getStringExtra("Month"); GET DAY
        //getSupportActionBar().setTitle(currMonth);


        Text=findViewById(R.id.textView7);
        boolean check=updateAllView();

        if(!check){
            Text.setText("Empty! Add something :)");
        }
        else{
            Text.setText("");
        }






    }






    private void callLoginDialog()
    {
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.pop_window_add_line);
        myDialog.setCancelable(false);
        myDialog.setTitle("Add new Line");

        //get the spinner from the xml.
        final Spinner dropdown = myDialog.findViewById(R.id.spinner1);
        //create a list of items for the spinner.
        String[] items = new String[]{"Task","Event","Note"};

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        Button save = (Button) myDialog.findViewById(R.id.Save);
        Button cancel = (Button) myDialog.findViewById(R.id.Cancel);
        TextView title=(TextView)myDialog.findViewById(R.id.textView8);
        title.setText(currMonth);



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



                insertItem(0,to_display);
                saveDB();
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

    public void callMoreDialog(final int more_id){
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.pop_window_more);
        myDialog.setCancelable(false);
        myDialog.setTitle("Details");


        Item text_disp=exampleList.get(more_id);

        String type=text_disp.getStringType();
        String title=text_disp.getTitle();
        String description= text_disp.getDescription();

        TextView title_text=(TextView)myDialog.findViewById(R.id.TitleText);
        title_text.setText(title);
        TextView description_text=(TextView)myDialog.findViewById(R.id.DescriptionText);
        description_text.setText(description);
        TextView type_text=(TextView)myDialog.findViewById(R.id.TypeText);
        type_text.setText(type);




        Button Delete= (Button) myDialog.findViewById(R.id.Delete);
        Button cancel = (Button) myDialog.findViewById(R.id.Exit);

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //removeItem();
                //saveDB();
                db.clear(); //removes all entries in map, so we can fill it again when we cal upadateAllView
                //mLayout.removeAllViews();
                boolean check=updateAllView();
                if(!check){
                    Text.setText("Nada adicionado");
                }
                else{
                    Text.setText("");
                }

                myDialog.dismiss();


            }
        });






        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(db);
                myDialog.dismiss();

            }
        });



        myDialog.show();





    }

    public void insertItem(int position,String b){
        String [] tokens_b=b.split("-");
        String type=tokens_b[0];
        String title=tokens_b[1];
        String description=tokens_b[2];
        int image=0;
        switch (type){
            case "Task": image=R.drawable.task_icon;
                break;
            case "Event":image=R.drawable.event_icon;
                break;
            case "Note":image=R.drawable.note_icon;
                break;
        }
        exampleList.add(position,new Item(image,description,title,type,true));
        mAdapter.notifyItemInserted(position);
    }

    public void removeItem(int position){

        exampleList.remove(position);
        mAdapter.notifyItemRemoved(position);
        if(exampleList.size()==0){
            Text.setText("Empty! Add something :)");

        }
    }



    public boolean updateAllView(){


        String info=load();
        if (info=="false"){


            return false;
        }else{
            String []  tokens=info.split("\n");
            for (String b:tokens) {
                insertItem(0,b);


            }



            return true;

        }

    }

    public String load () {
        FileInputStream fis=null;
        File myDir = getApplicationContext().getFilesDir();
        String path="2019/"+currMonth;
        File documentsFolder = new File(myDir,path);
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        FileOutputStream fos=null;
        File myfile=new File(documentsFolder,"Calendar_"+currMonth+"_"+currDay+".txt");

        try{
            fis=new FileInputStream(myfile);
            InputStreamReader isr=new InputStreamReader(fis);
            BufferedReader br= new BufferedReader(isr);
            StringBuilder sb=new StringBuilder();
            String text;

            while((text=br.readLine())!=null){


                sb.append(text).append("\n");
            }
            Toast.makeText(getBaseContext(),sb.toString(),Toast.LENGTH_SHORT).show();

            if(sb.toString()==""){
                return "false";
            }

            return sb.toString();


        }catch (FileNotFoundException e){

            e.printStackTrace();
            return "false";
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (fis !=null){
                try{
                    fis.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        return null;
    };

    public String saveDB(){
        File myDir = getApplicationContext().getFilesDir();
        String path="2019/"+currMonth;
        File documentsFolder = new File(myDir,path);
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File myfile=new File(documentsFolder,"Calendar_"+currMonth+"_"+currDay+".txt");
        Toast.makeText(getBaseContext(),myfile.getName(),Toast.LENGTH_SHORT).show();
        Iterator it = exampleList.iterator();


        try{

            myfile.createNewFile();
            FileOutputStream fos=new FileOutputStream(myfile);
            while(it.hasNext()){


                String to_save=(String)it.next().toString();

                fos.write(to_save.getBytes());

            }



        }catch (FileNotFoundException e){

            e.printStackTrace();
            return "filenotfound";
        }catch (IOException e){

            return e.toString();
        }
        return myfile.getAbsolutePath();
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
        getMenuInflater().inflate(R.menu.monthly__log__notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent=new Intent(this,Daily_Log_Hub.class);

                startActivity(intent);
                finish();
                return true;

            default:
                Intent intentb=new Intent(this,Daily_Log_Hub.class);

                startActivity(intentb);
                finish();
                return true;
        }
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
