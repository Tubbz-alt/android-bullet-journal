package ua.deti.bulletjounal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import java.io.BufferedReader;
import java.io.File;
import android.content.ContextWrapper;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.zip.Inflater;

import android.app.Dialog;
import android.widget.Toast;

public class Monthly_Log_Notes extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String filename="example.txt";
    private LinearLayout mLayout;
    private TextView Text;
    private Dialog myDialog;
    private int button_id=0;
    private String currMonth;
    private Map<Integer,String> db=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly__log__notes);
        mLayout= (LinearLayout) findViewById(R.id.linearLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button add_line = (Button) findViewById(R.id.Add_info);
        Button Calendar= (Button) findViewById(R.id.Calendar);


        add_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLoginDialog();
            }
        });
        Calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity2(v);
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        currMonth=getIntent().getStringExtra("Month");
        getSupportActionBar().setTitle(currMonth);


        Text=findViewById(R.id.textView7);
        boolean check=updateAllView();

        if(!check){
            Text.setText("Nada adicionado");
        }
        else{
            Text.setText("");
        }






    }


    private void createNewTextView(String text) {

        LayoutInflater inflater=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(this);
        final Button more =new Button(this);
        more.setText("More");
        more.setId(button_id);
        final int btn_id=more.getId(); //get the button id so I can associate a function
        textView.setLayoutParams(lparams);
        textView.setText(text);
        mLayout.addView(textView);
        mLayout.addView(more);
        db.put(btn_id,text);
        Button btn= (Button)findViewById(btn_id);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callMoreDialog(btn_id);
            }
        });

        if(Text.getText()=="Nada adicionado"){
            Text.setText("");
        }
        button_id+=1;
    }

    private void activity2(View view){
        Intent intent=new Intent(this,Monthly_Log_Calendar.class);
        intent.putExtra("Month",currMonth);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

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

                createNewTextView(to_display);

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


        String text_disp=db.get(more_id);
        String [] tokens=text_disp.split("-");
        String type=tokens[0];
        String title=tokens[1];
        String description=tokens[2];

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
                db.remove(more_id);
                saveDB();
                db.clear(); //removes all entries in map, so we can fill it again when we cal upadateAllView
                mLayout.removeAllViews();
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



    public boolean updateAllView(){


        String info=load();
        if (info=="false"){


            return false;
        }else{
            String []  tokens=info.split("\n");
            for (String b:tokens) {
                createNewTextView(b);

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
        File myfile=new File(documentsFolder,"Notes_"+currMonth+".txt");

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

    public String saveDB(){
        File myDir = getApplicationContext().getFilesDir();
        String path="2019/"+currMonth;
        File documentsFolder = new File(myDir,path);
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File myfile=new File(documentsFolder,"Notes_"+currMonth+".txt");
        Iterator it = db.entrySet().iterator();


        try{

            myfile.createNewFile();
            FileOutputStream fos=new FileOutputStream(myfile);
            while(it.hasNext()){

                Map.Entry pair = (Map.Entry)it.next();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                String to_save=(String)pair.getValue()+db.size()+"\n";

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

                Intent intent=new Intent(this,Monthly_Log_Hub.class);

                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
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
