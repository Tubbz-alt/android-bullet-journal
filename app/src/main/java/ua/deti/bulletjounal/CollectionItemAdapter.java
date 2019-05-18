package ua.deti.bulletjounal;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CollectionItemAdapter extends RecyclerView.Adapter<CollectionItemAdapter.ViewHolder>
{
    private ArrayList<HubItem> items;
    private OnItemClickListener clickListener;

    public CollectionItemAdapter(ArrayList<HubItem> items) { this.items = items; }

    public interface OnItemClickListener
    {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) { clickListener = listener; }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.collection_item, viewGroup, false);
        ViewHolder vw = new ViewHolder(v, clickListener);
        return vw;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        HubItem hb = items.get(i);
        String itemName = hb.getItemName();
        viewHolder.textView.setText(itemName.length() <= 26 ? itemName : itemName.substring(0,23) + "...");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textView;
        public ImageView imageView;
        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener1) {
            super(itemView);
            textView = itemView.findViewById(R.id.collectionItemName);
            imageView = itemView.findViewById(R.id.deleteCollectionItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener1 != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                            listener1.onItemClick(position);
                    }
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener1 != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                            listener1.onDeleteClick(position);
                    }

                }
            });
        }
    }
}
