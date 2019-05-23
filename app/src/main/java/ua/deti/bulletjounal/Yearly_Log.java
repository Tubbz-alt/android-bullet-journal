package ua.deti.bulletjounal;

import android.app.Dialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.DataSetObserver;
import android.icu.text.UnicodeSetSpanner;
import android.media.Image;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.time.YearMonth;

public class Yearly_Log extends AppCompatActivity
       {
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

        drawer.closeDrawer(GravityCompat.START);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Yearly Log");


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
                        onDeleteDialog(groupPosition, childPosition);
                    }
                });
                return false;
            }
        });
    }

           public void onDeleteDialog(final int groupPosition, final int childPosition)
           {
               final Dialog deleteDialog = new Dialog(this);
               deleteDialog.setContentView(R.layout.pop_window_delete);
               deleteDialog.setCancelable(true);
               deleteDialog.setTitle("Delete item");

               ImageView yes = deleteDialog.findViewById(R.id.Yes);
               ImageView no = deleteDialog.findViewById(R.id.No);

               yes.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       listHash.get(ListDataHeader.get(groupPosition)).get(childPosition).setShow(false);
                       listAdapter.notifyDataSetChanged();
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

    /*public void removeItem(int position){
        .remove(position);
        mAdapter.notifyItemRemoved(position);
    }*/
    private void callAddDialog()
    {
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.pop_window_add_info_year);
        myDialog.setCancelable(true);
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




        ImageView save = myDialog.findViewById(R.id.Save);
        ImageView cancel = myDialog.findViewById(R.id.Cancel);



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final  EditText Title = (EditText) myDialog.findViewById(R.id.editText_Year);

                String spinner_month = dropdown.getSelectedItem().toString();
                String spinner_day = dropdown_2.getSelectedItem().toString();
                String Title_text = Title.getText().toString();
                if(!Title_text.equals(""))
                {
                    insertItem(spinner_month,spinner_day,Title_text);
                    //saveDB();
                    myDialog.dismiss();
                }
                else
                    Toast.makeText(getBaseContext(), "You need to specify a title!", Toast.LENGTH_SHORT).show();
                //String to_display=spinner_save+"-"+title_save+"-"+description_save;

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

    /*public void updateAllView (String month) {
        FileInputStream fis=null;
        File myDir = getApplicationContext().getFilesDir();
        String path="2019/"+month;
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
                    String info=load(fileName,month);
                    String

                    String []  tokens=info.split("\n");
                    for (String b:tokens) {
                        insertItemFile(0,month,b,);

                        //createNewTextView(b);

                    }
                }


            }
        }
    };*/

    public String load (String filename,String currMonth) {
        FileInputStream fis=null;
        File myDir = getApplicationContext().getFilesDir();
        String path="2019/"+currMonth;
        File documentsFolder = new File(myDir,path);
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        FileOutputStream fos=null;
        File myfile=new File(documentsFolder,filename);

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

   /* public String saveDB(){
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
    };*/


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*public void insertItemFile(String month,String day){
        String [] tokens_b=b.split("-");
        String type=tokens_b[0];
        String title=tokens_b[1];
        String description=tokens_b[2];
        if(type=="Event"){
            int image=0;

            image=R.drawable.event_icon;

            exampleList.add(position,new Item(image,description,title,type,true));
            mAdapter.notifyItemInserted(position);
            if(exampleList.size()!=0){
                Text.setText("");
            }
        }
        listHash.get(Month).add(new Item_Yearly(day,title,true));

    }*/

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
       switch (item.getItemId()) {

           // case R.id.action_filter:
           //   Toast.makeText(getBaseContext(),"ola",Toast.LENGTH_SHORT).show();
           //  return true;
           case android.R.id.home:
               // todo: goto back activity from here

               Intent intent=new Intent(this,Yearly_Log_Hub.class);

               startActivity(intent);
               finish();
               return true;

           default:
               Intent intentb=new Intent(this,Yearly_Log_Hub.class);

               startActivity(intentb);
               finish();
               return true;
       }

   }


}
