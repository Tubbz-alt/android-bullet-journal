package ua.deti.bulletjounal;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<Item> mList;
    public static class ViewHolder extends  RecyclerView.ViewHolder{
        public ImageView mImagevIew;
        public TextView mTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImagevIew=itemView.findViewById(R.id.line_image);
            mTextView=itemView.findViewById(R.id.line_text);
        }
    }

    public Adapter(ArrayList<Item> list){
        this.mList=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_line,viewGroup,false);
        ViewHolder mvh=new ViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Item currentItem=mList.get(i);
        viewHolder.mImagevIew.setImageResource(currentItem.getType());
        viewHolder.mTextView.setText(currentItem.getTitle());
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void insertItem(Item b){
        mList.add(b);
        notifyItemInserted(getItemCount());
    }
}
