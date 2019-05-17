package ua.deti.bulletjounal;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class CollectionPage extends AppCompatActivity {

    private File collection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("CollectionName"));
        loadCollection(getIntent().getStringExtra("CollectionName"));
    }

    private void loadCollection(String collectionName)
    {
        File myDir = getApplicationContext().getFilesDir();
        collection = new File(myDir, "Collections/" + collectionName);

    }

}
