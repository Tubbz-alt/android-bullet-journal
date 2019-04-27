package ua.deti.bulletjounal;

import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
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
        implements NavigationView.OnNavigationItemSelectedListener {

    private Dialog DetailsDialog;
    private Dialog AddDialog;
    private Dialog MoreDialog;
    private LinearLayout mLayout;
    private int button_id=0;

    private TextView infoDisp;
    private Map<Integer,Map<Integer,String>> db=new HashMap<>();
    private MaterialCalendarView cl;
    private EventDecorator eventDec;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly__log__calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        Button notes =(Button) findViewById(R.id.notes);

        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity2(v);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE,Calendar.getInstance().getActualMinimum(Calendar.DATE));
        long date = calendar.getTime().getTime();
        cl=(MaterialCalendarView) findViewById(R.id.calendarView);
        CalendarDay first=CalendarDay.from(2019,4,1);
        CalendarDay last=CalendarDay.from(2019,4,30);

        cl.state().edit().setMinimumDate(first).setMaximumDate(last).commit();

        cl.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
                createDialogDetails(calendarDay);
                Toast.makeText(getBaseContext(),calendarDay.toString(),Toast.LENGTH_LONG).show();
            }
        });



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Collection<CalendarDay> dates=new ArrayList<>();

        eventDec =new EventDecorator(Color.BLACK,dates);






    }

    public void callMoreDialog(final int more_id,final int dia){
        MoreDialog = new Dialog(this);
        MoreDialog.setContentView(R.layout.pop_window_more);
        MoreDialog.setCancelable(false);
        MoreDialog.setTitle("Details");


        final Map tempMap =db.get(dia);
        final String text_disp=(String) tempMap.get(more_id);
        String [] tokens=text_disp.split("-");
        String type=tokens[0];
        String title=tokens[1];
        String description=tokens[2];

        TextView title_text=(TextView)MoreDialog.findViewById(R.id.TitleText);
        title_text.setText(title);
        TextView description_text=(TextView)MoreDialog.findViewById(R.id.DescriptionText);
        description_text.setText(description);
        TextView type_text=(TextView)MoreDialog.findViewById(R.id.TypeText);
        type_text.setText(type);




        Button Delete= (Button) MoreDialog.findViewById(R.id.Delete);
        Button cancel = (Button) MoreDialog.findViewById(R.id.Exit);

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempMap.remove(more_id);

                saveDB(dia);
                tempMap.clear(); //removes all entries in map, so we can fill it again when we cal upadateAllView
                db.put(dia,tempMap);
                mLayout.removeAllViews();
                boolean check=updateAllView(dia);
                if(!check){
                    infoDisp.setText("Nada adicionado");
                }
                else{
                    infoDisp.setText("");
                }

                MoreDialog.dismiss();


            }
        });






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
        DetailsDialog.setCancelable(false);
        DetailsDialog.setTitle("Details");
        mLayout= (LinearLayout) DetailsDialog.findViewById(R.id.linearLayout2);

        final int dia=day.getDay();
        boolean check=updateAllView(dia);
        infoDisp=DetailsDialog.findViewById(R.id.infoDisp);


        if(!check){
            infoDisp.setText("Nada adicionado");
        }else{
            infoDisp.setText("");


        }





        TextView title=(TextView)DetailsDialog.findViewById(R.id.masterTitle);
        title.setText(day.getDay()+" de "+day.getMonth()+" de "+day.getYear());
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
                DetailsDialog.dismiss();

            }
        });



        DetailsDialog.show();

    }

    private void createNewTextView(String text, final int Dia) {

        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(this);
        final Button more =new Button(this);
        more.setText("More");
        more.setId(button_id);
        final int btn_id=more.getId(); //get the button id so I can associate a function
        textView.setLayoutParams(lparams);
        textView.setText(text);
        mLayout.addView(textView);
        mLayout.addView(more);
        if(db.containsKey(Dia)){
            Map tempMap=db.get(Dia);
            tempMap.put(btn_id,text);
            db.put(Dia,tempMap);
        }else{
            Map tempMap=new HashMap<Integer,String>();
            tempMap.put(btn_id,text);
            db.put(Dia,tempMap);
        }

        Button btn= (Button)DetailsDialog.findViewById(btn_id);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callMoreDialog(btn_id,Dia);
            }
        });

        button_id+=1;
        eventDec.addDay(CalendarDay.from(2019,04,Dia));
        cl.addDecorator(eventDec);


    }

    private void callLoginDialog(int Day)
    {
        AddDialog = new Dialog(this);
        AddDialog.setContentView(R.layout.pop_window_add_line);
        AddDialog.setCancelable(false);
        AddDialog.setTitle("Add new Line");
        final int dia=Day;

        //get the spinner from the xml.
        final Spinner dropdown = AddDialog.findViewById(R.id.spinner1);
        //create a list of items for the spinner.
        String[] items = new String[]{"Task","Event","Note"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        Button save = (Button) AddDialog.findViewById(R.id.Save);
        Button cancel = (Button) AddDialog.findViewById(R.id.Cancel);



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText Description = (EditText) AddDialog.findViewById(R.id.description);
                final  EditText Title = (EditText) AddDialog.findViewById(R.id.title);

                String spinner_save = dropdown.getSelectedItem().toString();
                String description_save= (String) Description.getText().toString();
                String title_save= (String) Title.getText().toString();

                String to_display=spinner_save+"-"+title_save+"-"+description_save;


                Toast.makeText(getBaseContext(),"Done"  ,
                        Toast.LENGTH_LONG).show();

                createNewTextView(to_display,dia);

                saveDB(dia);

                AddDialog.dismiss();


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

    public boolean updateAllView(int Dia){

        String info=load(Dia);
        if (info=="false"){
            return false;
        }else{
            String []  tokens=info.split("\n");
            for (String b:tokens) {
                createNewTextView(b,Dia);

            }
            return true;

        }

    }

    public String saveDB(int Dia){
        String filename="file_"+Dia;
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        FileOutputStream fos=null;
        File directory= contextWrapper.getDir(getFilesDir().getName(), Context.MODE_PRIVATE);
        File myfile=new File(directory,filename);
        Map tempMap=db.get(Dia);
        Iterator it = tempMap.entrySet().iterator();
        try{

            myfile.createNewFile();
            fos=openFileOutput(filename,Context.MODE_PRIVATE);

            while(it.hasNext()){

                Map.Entry pair = (Map.Entry)it.next();
                String to_save=(String)pair.getValue()+"\n";

                fos.write(to_save.getBytes());

            }



        }catch (FileNotFoundException e){

            e.printStackTrace();
            return "filenotfound";
        }catch (IOException e){

            return e.toString();
        }
        return myfile.getAbsolutePath();
    };

    public String load (int Dia) {
        String filename="file_"+Dia;

        FileInputStream fis=null;

        try{
            fis=openFileInput(filename);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void activity2(View view){
        Intent intent=new Intent(this,Monthly_Log_Notes.class);
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
