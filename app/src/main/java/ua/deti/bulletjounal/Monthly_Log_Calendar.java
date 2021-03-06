package ua.deti.bulletjounal;

import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Monthly_Log_Calendar extends AppCompatActivity
       {

    private Dialog DetailsDialog;
    private Dialog AddDialog;
    private Dialog MoreDialog;
    private RelativeLayout Slide;
    private RecyclerView mLayout;
    private int button_id=0;

    private TextView infoDisp;
    private Map<Integer,ArrayList<Item>> db=new HashMap<>();
    private MaterialCalendarView cl;
    private EventDecorator eventDec;
    private EventDecorator taskDec;
    private EventDecorator noteDec;
    private String currMonth;
    private TabLayout tablay;

    private Adapter_Calendar mAdapter;
    private RecyclerView.LayoutManager layoutManager;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly__log__calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);







        currMonth=getIntent().getStringExtra("Month");
        getSupportActionBar().setTitle(currMonth);
        int first_day=1;
        int month_number=0;
        int last_day=0;

        switch (currMonth){
            case "January":{
                month_number=0;
                last_day=31;
                break;//tratar dos bissextos
            }
            case "February":{
                month_number=1;
                last_day=28;
                break;//tratar dos bissextos
            }
            case "March":{
                month_number=2;
                last_day=31;
                break;
            }
            case "April":{
                month_number=3;
                last_day=30;
                break;

            }
            case "May":{
                month_number=4;
                last_day=31;
                break;
            }
            case "June":{
                month_number=5;
                last_day=30;

                break;
            }
            case "July":{
                month_number=6;
                last_day=31;
                break;
            }
            case "August":{
                month_number=7;
                last_day=31;
                break;
            }
            case "September":{
                month_number=8;
                last_day=30;
                break;
            }
            case "October":{
                month_number=9;
                last_day=31;
                break;

            }
            case "November":{
                month_number=10;
                last_day=30;
                break;

            }
            case "December":{
                month_number=11;
                last_day=31;
                break;

            }

        }


        int ola=2;
        cl=(MaterialCalendarView) findViewById(R.id.calendarView);
        CalendarDay first=CalendarDay.from(2019,month_number+1,first_day);
        CalendarDay last=CalendarDay.from(2019,month_number+1,last_day);

        cl.state().edit().setMinimumDate(first).setMaximumDate(last).commit();

        cl.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
                createDialogDetails(calendarDay);
            }
        });



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        Collection<CalendarDay> dates=new ArrayList<>();



        for(int i=first_day;i<=last_day;i++)
        {
            ArrayList<Item> arr=new ArrayList<>();

            db.put(i,arr);
        }

        Slide=(RelativeLayout) findViewById(R.id.calendarViewSlide);

        Slide.setOnTouchListener(new OnTouchSwipeListener(this){
            @Override
            public void onSwipeLeft() {
                Intent intent=new Intent(getBaseContext(),Monthly_Log_Notes.class);
                intent.putExtra("Month",currMonth);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }



        });

        tablay=(TabLayout)findViewById(R.id.tab_layout);


        TabLayout.Tab tab = tablay.getTabAt(0);
        tab.select();
        tablay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 1:{
                        Intent intent=new Intent(getBaseContext(),Monthly_Log_Notes.class);
                        intent.putExtra("Month",currMonth);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    }


                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });








    }

    public void insertItem(int position,String b,int dia){
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
        db.get(dia).add(position,new Item(image,description,title,type,true));


        mAdapter.notifyItemInserted(position);

        if(db.get(dia).size()==0){
            infoDisp.setText("Empty! Add something :)");
        }
        else
            infoDisp.setText("");

    }

    public void removeItem(int position,int dia){

        db.get(dia).remove(position);
        mAdapter.notifyItemRemoved(position);

        if(db.get(dia).size()==0){
            infoDisp.setText("Empty! Add something :)");
        }
        else
            infoDisp.setText("");


    }

    public void callMoreDialog(int position,final int dia){
        MoreDialog = new Dialog(this);
        MoreDialog.setContentView(R.layout.pop_window_more);
        MoreDialog.setCancelable(true);
        MoreDialog.setTitle("Details");


        final Item temp =db.get(dia).get(position);

        String type= temp.getStringType();
        String title= temp.getTitle();
        String description=temp.getDescription();

        TextView title_text=(TextView)MoreDialog.findViewById(R.id.TitleText);
        title_text.setText(title);
        TextView description_text=(TextView)MoreDialog.findViewById(R.id.DescriptionText);
        description_text.setText(description);
        TextView type_text=(TextView)MoreDialog.findViewById(R.id.TypeText);
        type_text.setText(type);




        ImageView cancel = (ImageView) MoreDialog.findViewById(R.id.Exit);








        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreDialog.dismiss();

            }
        });



        MoreDialog.show();





    }

    public void createDialogDetails(CalendarDay day){

        DetailsDialog = new Dialog(this);
        DetailsDialog.setContentView(R.layout.pop_window_details);
        DetailsDialog.setCancelable(true);
        DetailsDialog.setTitle("Details");
        mLayout= (RecyclerView) DetailsDialog.findViewById(R.id.RecyclerViewCalendar);
        TextView masterTitle=(TextView)DetailsDialog.findViewById(R.id.masterTitle);
        final int dia=day.getDay();
        masterTitle.setText(dia+" of "+currMonth);

        if(db.get(dia)==null){
            ArrayList<Item> arr=new ArrayList<>();
            db.put(dia,arr);
        }





        mLayout.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        mLayout.setLayoutManager(layoutManager);

        mAdapter = new Adapter_Calendar(db.get(dia));

        mLayout.setAdapter(mAdapter);

        mLayout.setLayoutManager(layoutManager);
        mLayout.setAdapter(mAdapter);
        infoDisp=DetailsDialog.findViewById(R.id.infoDisp);

        boolean check=updateAllView(dia);


        if(db.get(dia).size()==0){
            infoDisp.setText("Empty! Add something :)");
        }
        else
            infoDisp.setText("");

        mAdapter.setOnItemClickListener(new Adapter_Calendar.OnItemClickListener() {
            @Override
            public void OnDelete(int position) {
                removeItem(position,dia);
                saveDB(dia);
            }



            @Override
            public void onItemclick(int position) {
               callMoreDialog(position,dia);
            }

            @Override
            public void onCheckbox(int position, TextView cross, ImageView change, CheckBox check) {
                if(check.isChecked()){
                    cross.setPaintFlags(cross.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                    change.setImageResource(R.drawable.done_icon);

                }
                else{
                    cross.setPaintFlags(cross.getPaintFlags()& ~Paint.STRIKE_THRU_TEXT_FLAG);

                    switch (db.get(dia).get(position).getStringType()){
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










        Button Add= (Button) DetailsDialog.findViewById(R.id.Add_info_calendar);
        Button Exit = (Button) DetailsDialog.findViewById(R.id.Exit_info_calendar);

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLoginDialog(dia);
            }
        });






        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.get(dia).clear();
                DetailsDialog.dismiss();

            }
        });



        DetailsDialog.show();

    }



    private void callLoginDialog(int Day)
    {
        AddDialog = new Dialog(this);
        AddDialog.setContentView(R.layout.pop_window_add_line);
        AddDialog.setCancelable(true);
        AddDialog.setTitle("Add new Line");
        final int dia=Day;
        TextView titleView=(TextView)AddDialog.findViewById(R.id.textView8);

        titleView.setText(currMonth);

        //get the spinner from the xml.
        final Spinner dropdown = AddDialog.findViewById(R.id.spinner1);
        //create a list of items for the spinner.
        String[] items = new String[]{"Task","Event","Note"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        ImageView save = (ImageView) AddDialog.findViewById(R.id.Save);
        ImageView cancel = (ImageView) AddDialog.findViewById(R.id.Cancel);



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText Description = (EditText) AddDialog.findViewById(R.id.description);
                final  EditText Title = (EditText) AddDialog.findViewById(R.id.title);

                String spinner_save = dropdown.getSelectedItem().toString();
                String description_save= (String) Description.getText().toString();
                String title_save= (String) Title.getText().toString();


                if(description_save.equals(""))
                    description_save = "No description";

                if(!title_save.equals(""))
                {
                    String to_display=spinner_save+"-"+title_save+"-"+description_save;

                    insertItem(0,to_display,dia);

                    saveDB(dia);

                    AddDialog.dismiss();
                }
                else
                    Toast.makeText(getBaseContext(), "You need to specify a title!", Toast.LENGTH_SHORT).show();





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

    public boolean updateAllView(int dia){




        String info=load(dia);
        if (info=="false"){


            return false;
        }else{
            String []  tokens=info.split("\n");
            for (String b:tokens) {
                insertItem(0,b,dia);

                //createNewTextView(b);

            }



            return true;

        }

    }

    public String saveDB(int Dia){

        String filename="Calendar_"+currMonth+"_"+Dia+".txt";

        File myDir = getApplicationContext().getFilesDir();
        String path="2019/"+currMonth;
        File documentsFolder = new File(myDir,path);
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());

        File myfile=new File(documentsFolder,filename);
        ArrayList<Item> item=db.get(Dia);
        Iterator it = item.iterator();
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

    public String load (int Dia) {


        FileInputStream fis=null;
        File myDir = getApplicationContext().getFilesDir();
        String path="2019/"+currMonth;
        File documentsFolder = new File(myDir,path);
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        FileOutputStream fos=null;
        File myfile=new File(documentsFolder,"Calendar_"+currMonth+"_"+Dia+".txt");

        try{
            fis=new FileInputStream(myfile);
            InputStreamReader isr=new InputStreamReader(fis);
            BufferedReader br= new BufferedReader(isr);
            StringBuilder sb=new StringBuilder();
            String text;

            while((text=br.readLine())!=null){

                sb.append(text).append("\n");
            }

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

    @Override
    public void onBackPressed() {

    }

    private void activity2(View view){
        Intent intent=new Intent(this,Monthly_Log_Notes.class);
        intent.putExtra("Month",currMonth);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.monthly__log__calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent=new Intent(this,Monthly_Log_Hub.class);

                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
