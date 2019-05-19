package ua.deti.bulletjounal;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

public class CollectionPage extends AppCompatActivity {

    private File collection;
    private RecyclerView itemsRecyclerView;
    private CollectionItemAdapter itemAdapter;
    private RecyclerView.LayoutManager itemLayout;
    private ArrayList<HubItem> items;

    private Context thisContext;
    private View thisView;

    private ImageView saveBtn;
    private ImageView cancelBtn;
    private ImageView deleteBtn;
    private EditText editText;
    private String inputText;
    private TextView show;

    private String collectionName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collectionName = getIntent().getStringExtra("CollectionName");
        getSupportActionBar().setTitle(collectionName);

        show=(TextView)findViewById(R.id.textView7);
        ImageView addItemBtn = findViewById(R.id.addCollectionItem);


        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAndDialog(view);
            }
        });

        createItemsList();
        buildRecyclerView();
        loadCollection();
    }

    public void createItemsList()
    {
        items = new ArrayList<>();
    }

    public void  buildRecyclerView() {
        itemsRecyclerView = findViewById(R.id.collectionItemsRV);
        itemsRecyclerView.setHasFixedSize(true);
        itemLayout = new LinearLayoutManager(this);
        itemAdapter = new CollectionItemAdapter(items);
        itemsRecyclerView.setLayoutManager(itemLayout);
        itemsRecyclerView.setAdapter(itemAdapter);

        itemAdapter.setOnItemClickListener(new CollectionItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                collectionInfoDialog(position);
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
                saveCollections();
            }
        });
    }

    public void callAndDialog(View view)
    {
        final Dialog addDialog = new Dialog(this);
        addDialog.setContentView(R.layout.pop_window_add_collection_item);
        addDialog.setCancelable(true);
        addDialog.setTitle("Add collection item");


        saveBtn = addDialog.findViewById(R.id.saveItemBtn);
        cancelBtn = addDialog.findViewById(R.id.cancelItemBtn);

        // dismiss modal
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.dismiss();
            }
        });

        // save
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText = addDialog.findViewById(R.id.editText3);
                inputText = editText.getText().toString();
                if(inputText.length() == 0)
                {
                    Toast.makeText(addDialog.getContext(), "Please, add a name to the collection item.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    insertItem(inputText);
                    saveCollections();
                    addDialog.dismiss();
                }

            }
        });

        addDialog.show();
    }

    public void insertItem(String inputText)
    {



        items.add(new HubItem(inputText));
        itemAdapter.notifyItemInserted(items.size()-1);
        if(items.size()==0)
            show.setText("Empty! Add something :)");
        else
            show.setText("");
    }

    public void removeItem(int position)
    {


        items.remove(position);
        itemAdapter.notifyItemRemoved(position);
        if(items.size()==0)
            show.setText("Empty! Add something :)");
        else
            show.setText("");
    }

    private void loadCollection()
    {
        File myDir = getApplicationContext().getFilesDir();
        File documentsFolder = new File(myDir, "Collections");
        FileInputStream fis = null;
        File myFile = new File(documentsFolder, collectionName + ".txt");
        try
        {
            fis = new FileInputStream(myFile);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;
            if (br.readLine()==null){
                show.setText("Empty! Add something :)");
            }
            else {
                show.setText("");
            }
            while ((text=br.readLine()) != null)
                if(text != "")
                    insertItem(text);
            fis.close();
        }
        catch (IOException e){ e.printStackTrace(); }
        finally
        {
            if (fis !=null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

    }

    public void saveCollections()
    {
        File myDir = getApplicationContext().getFilesDir();
        File documentsFolder = new File(myDir,"Collections");
        File myFile = new File(documentsFolder, collectionName + ".txt");
        Iterator it = items.iterator();

        FileOutputStream fos = null;
        try
        {
            myFile.createNewFile();
            fos = new FileOutputStream(myFile);
            while (it.hasNext())
                fos.write(it.next().toString().getBytes());
            fos.close();
        }
        catch (Exception IOException) { }

    }

    private void collectionInfoDialog(int position)
    {
        final Dialog infoDialog = new Dialog(this);
        infoDialog.setContentView(R.layout.collection_info);
        infoDialog.setCancelable(true);
        infoDialog.setTitle("Collection info");

        ImageView CloseBtn = infoDialog.findViewById(R.id.closeItemDialog);
        TextView infoFullName = infoDialog.findViewById(R.id.itemFullName);
        infoFullName.setText("" + items.get(position).getItemName());

        CloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoDialog.dismiss();
            }
        });



        infoDialog.show();
    }


}
