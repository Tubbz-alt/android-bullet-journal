package ua.deti.bulletjounal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.media.Image;
import android.os.Bundle;

import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.zip.Inflater;

import android.app.Dialog;
import android.widget.Toast;


public class Monthly_Log_Notes extends AppCompatActivity
         {
    private static final String filename="example.txt";
    private RecyclerView recyclerView;
    private Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //private LinearLayout mLayout;
    private ImageView addInfo;
    private RelativeLayout slide;
    private TextView Text;
    private Dialog myDialog;
    private int button_id=0;
    private TabLayout tablay;
    private TabItem tab1;
    private  TabItem tab2;
    private String currMonth;
    private ArrayList<Item> exampleList=new ArrayList<>();
    private float x1,x2,y1,y2;

    private Map<Integer,String> db=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly__log__notes);

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

        mAdapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void OnDelete(int position) {
                onDeleteDialog(position);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        currMonth=getIntent().getStringExtra("Month");
        getSupportActionBar().setTitle(currMonth);


        Text=findViewById(R.id.textView7);
        boolean check=updateAllView();

        if(!check){
            Text.setText("Empty! Add Something :)");
        }
        else{
            Text.setText("");
        }


        slide=(RelativeLayout)findViewById(R.id.slide_notes);
        tablay=(TabLayout)findViewById(R.id.tab_layout);

        TabLayout.Tab tab = tablay.getTabAt(1);
        tab.select();
        tablay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:{
                        Intent intent=new Intent(getBaseContext(),Monthly_Log_Calendar.class);
                        intent.putExtra("Month",currMonth);

                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
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



        slide.setOnTouchListener(new OnTouchSwipeListener(this){


            @Override
            public void onSwipeRight() {
                Intent intent=new Intent(getBaseContext(),Monthly_Log_Calendar.class);
                intent.putExtra("Month",currMonth);

                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });







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

        ImageView save = myDialog.findViewById(R.id.Save);
        ImageView cancel = myDialog.findViewById(R.id.Cancel);



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText Description = (EditText) myDialog.findViewById(R.id.description);
                final  EditText Title = (EditText) myDialog.findViewById(R.id.title);

                String spinner_save = dropdown.getSelectedItem().toString();
                String title_save = Title.getText().toString();
                String description_save = Description.getText().toString();

                if(description_save.equals(""))
                    description_save = "No description";

                if(!title_save.equals(""))
                {
                    String to_display=spinner_save+"-"+title_save+"-"+description_save;
                    Toast.makeText(getBaseContext(),"Done"  ,
                            Toast.LENGTH_LONG).show();
                    insertItem(0,to_display);
                    saveDB();
                    myDialog.dismiss();

                }
                Toast.makeText(getBaseContext(), "You need to specify a title!", Toast.LENGTH_SHORT).show();
                //createNewTextView(to_display);
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

        final TextView title_text=(TextView)myDialog.findViewById(R.id.TitleText);
        title_text.setText(title);
        TextView description_text=(TextView)myDialog.findViewById(R.id.DescriptionText);
        description_text.setText(description);
        TextView type_text=(TextView)myDialog.findViewById(R.id.TypeText);
        type_text.setText(type);




        ImageView Delete = myDialog.findViewById(R.id.Edit);
        ImageView cancel = myDialog.findViewById(R.id.Exit);

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText TitleEdit=(EditText)myDialog.findViewById(R.id.TitleTextEdit);
                title_text.setVisibility(View.GONE);
                TitleEdit.setVisibility(View.VISIBLE);
                /*db.remove(more_id);
                saveDB();
                db.clear(); //removes all entries in map, so we can fill it again when we cal upadateAllView
                //mLayout.removeAllViews();
                boolean check=updateAllView();
                if(!check){
                    Text.setText("Empty! Add something :)");
                }
                else{
                    Text.setText("");
                }

                myDialog.dismiss();*/


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
        if(exampleList.size()!=0){
            Text.setText("");
        }
    }

    public void removeItem(int position){

        exampleList.remove(position);
        if(exampleList.size()==0){
            Text.setText("Empty! Add something :)");
        }
        mAdapter.notifyItemRemoved(position);
    }



    public boolean updateAllView(){


        String info=load();
        if (info=="false"){


            return false;
        }else{
            String []  tokens=info.split("\n");
            for (String b:tokens) {
                insertItem(0,b);

                //createNewTextView(b);

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
    public void onDeleteDialog(final int position)
    {
        final Dialog deleteDialog = new Dialog(this);
        deleteDialog.setContentView(R.layout.pop_window_delete);
        deleteDialog.setCancelable(false);
        deleteDialog.setTitle("Delete item");

        ImageView yes = deleteDialog.findViewById(R.id.Yes);
        ImageView no = deleteDialog.findViewById(R.id.No);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(position);
                saveDB();
                deleteDialog.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.dismiss();
            }
        });
        deleteDialog.show();
    }
}
