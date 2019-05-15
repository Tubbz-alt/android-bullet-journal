package ua.deti.bulletjounal;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Adapter_yearly extends BaseExpandableListAdapter {
    private Context context;
    private List<String> ListDataHeader;
    public ImageView mDeleteImage;
    public RelativeLayout layoutHide;


    private HashMap<String, List<Item_Yearly>> ListHashMap;
    public interface OnItemClickListener{
        void OnDelete(int position);

    }



    public Adapter_yearly(Context context, List<String> listDataHeader, HashMap<String, List<Item_Yearly>> listHashMap) {
        this.context = context;
        ListDataHeader = listDataHeader;
        ListHashMap = listHashMap;
    }



    @Override
    public int getGroupCount() {
        return ListDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return ListHashMap.get(ListDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return ListDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return ListHashMap.get(ListDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle=(String)getGroup(groupPosition);
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.list_group,null);
        }

        TextView lbListHeader=(TextView)convertView.findViewById(R.id.listHeader);
        lbListHeader.setTypeface(null, Typeface.BOLD);
        lbListHeader.setText(headerTitle);
        return convertView;

    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        Item_Yearly item=(Item_Yearly) getChild(groupPosition,childPosition);
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.yearly_item,null);
            mDeleteImage=convertView.findViewById(R.id.deleteButton_yearly);
            layoutHide=convertView.findViewById(R.id.layout_to_hide);

            final View finalConvertView = convertView;

        }

        TextView txtListChild=(TextView)convertView.findViewById(R.id.line_text);
        TextView txtListChild_b=(TextView)convertView.findViewById(R.id.line_text_info);
        mDeleteImage=convertView.findViewById(R.id.deleteButton_yearly);
        layoutHide=convertView.findViewById(R.id.layout_to_hide);

        txtListChild.setText(item.getDay());
        txtListChild_b.setText(item.getTitle());
        Toast.makeText(parent.getContext(),"ola dssa "+item.getShow()  ,
                Toast.LENGTH_LONG).show();

        if (item.getShow()) {

            layoutHide.setVisibility(View.VISIBLE);
        } else {
            layoutHide.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

