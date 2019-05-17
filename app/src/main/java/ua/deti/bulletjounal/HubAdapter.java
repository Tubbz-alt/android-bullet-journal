package ua.deti.bulletjounal;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class HubAdapter extends RecyclerView.Adapter<HubAdapter.ViewHolder>
{
    private ArrayList<HubItem> collections;
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    public interface OnItemClickListener
    {
        void onItemClick(int position);

    }

    public interface OnItemLongClickListener
    {
        void onItemLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        clickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener)
    {
        longClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textView1;
        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener1, final OnItemLongClickListener listener2 ) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.collectionName);

            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener1 != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener1.onItemClick(position);
                        }
                    }
                }
            });

            textView1.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(listener2 != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener2.onItemLongClick(position);
                        }
                    }
                    return true;
                }
            });

        }
    }

    public HubAdapter(ArrayList<HubItem> collections)
    {
        this.collections = collections;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hub_item, viewGroup, false);
        ViewHolder vw = new ViewHolder(v, clickListener, longClickListener);
        return vw;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        HubItem currentItem = collections.get(i);
        viewHolder.textView1.setText(currentItem.getItemName());
    }

    @Override
    public int getItemCount() {
        return collections.size();
    }


}
