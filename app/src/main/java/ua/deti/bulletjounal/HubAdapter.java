package ua.deti.bulletjounal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class HubAdapter extends RecyclerView.Adapter<HubAdapter.ViewHolder>
{

    private static final String TAG = "HubAdapter";
    private ArrayList<String> collectionsNames = new ArrayList<>();
    private Context context;

    public HubAdapter(ArrayList<String> collectionsNames, Context context)
    {
        this.collectionsNames = collectionsNames;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hub_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called.");
        viewHolder.collectionName.setText(collectionsNames.get(i));
        
        
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView collectionName;
        ImageView deleteCollection;
        RelativeLayout parentLayout;
        public ViewHolder(View view)
        {
            super(view);
            collectionName = view.findViewById(R.id.collectionName);
            deleteCollection = view.findViewById(R.id.deleteCollection);
            parentLayout = view.findViewById(R.id.parent_layout);
        }
    }
}
