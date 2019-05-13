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
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void OnDelete(int position);
        void OnInfo(int position);
    }


    public static class ViewHolder extends  RecyclerView.ViewHolder{
        public ImageView mImagevIew;
        public ImageView mDeleteImage;
        public ImageView mInfoImage;


        public TextView mTextView;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImagevIew=itemView.findViewById(R.id.line_image);
            mTextView=itemView.findViewById(R.id.line_text);
            mDeleteImage= itemView.findViewById(R.id.deleteButton);
            mInfoImage=itemView.findViewById(R.id.InfoButton);

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
            mInfoImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position= getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.OnInfo(position);
                        }
                    }

                }
            });


        }
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
