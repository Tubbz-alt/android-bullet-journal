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

import java.io.BufferedReader;
import java.io.File;
import android.content.ContextWrapper;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Dialog;
import android.widget.Toast;

public class Monthly_Log_Notes extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String filename="example.txt";
    private LinearLayout mLayout;
    TextView Text;
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly__log__notes);
        mLayout= (LinearLayout) findViewById(R.id.linearLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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



        Text=findViewById(R.id.textView7);
        String info=load();
        if (info=="false"){
            Text.setText("Nada adicionado");
        }else{
            mLayout.addView(createNewTextView(info));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private TextView createNewTextView(String text) {
        final LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(this);
        textView.setLayoutParams(lparams);
        textView.setText("New text: " + text);
        return textView;
    }

    private void activity2(View view){
        Intent intent=new Intent(this,Monthly_Log_Calendar.class);
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
                final EditText Description = (EditText) myDialog.findViewById(R.id.title);
                final  EditText Title = (EditText) myDialog.findViewById(R.id.title);

                String spinner_save = dropdown.getSelectedItem().toString();
                String description_save= (String) Description.getText().toString();
                String title_save= (String) Title.getText().toString();

                String to_save=spinner_save+"-"+title_save+"-"+description_save;

                String check =save(to_save);
                Toast.makeText(getBaseContext(),check  ,
                        Toast.LENGTH_LONG).show();
                updateView();
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

    public void updateView(){
        String text=load();
        mLayout.addView(createNewTextView(text));

    }

    public String load () {
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

    public String save(String to_save){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        FileOutputStream fos=null;
        File directory= contextWrapper.getDir(getFilesDir().getName(), Context.MODE_PRIVATE);
        File myfile=new File(directory,filename);
        try{
            myfile.createNewFile();
            fos=openFileOutput(filename,MODE_PRIVATE);
            fos.write(to_save.getBytes());

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
