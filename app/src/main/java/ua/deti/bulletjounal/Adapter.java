package ua.deti.bulletjounal;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<Item> mList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void OnDelete(int position);

        void onItemclick(int position);
        void onCheckbox(int position, TextView cross,ImageView change,CheckBox check);
    }





    public static class ViewHolder extends  RecyclerView.ViewHolder{
        public ImageView mImagevIew;
        public ImageView mDeleteImage;
        public CheckBox mCheckbox;


        public TextView mTextView;

        public ViewHolder(@NonNull final View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImagevIew=itemView.findViewById(R.id.line_image);
            mTextView=itemView.findViewById(R.id.line_text);
            mDeleteImage= itemView.findViewById(R.id.deleteButton);
            mCheckbox=itemView.findViewById(R.id.checkBox);

            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position= getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.OnDelete(position);
                        }
                    }

                }
            });
            mCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position= getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onCheckbox(position,mTextView,mImagevIew,mCheckbox);
                        }
                    }





                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position= getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemclick(position);
                        }
                    }
                }
            });


        }
    }

    public String getItemtype(int position){
        return  mList.get(position).getStringType();
    }

    public Adapter(ArrayList<Item> list){
        this.mList=list;
    }

    public void setOnItemClickListener(OnItemClickListener listener){mListener=listener;}


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_line,viewGroup,false);
        ViewHolder mvh=new ViewHolder(v,mListener);
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
