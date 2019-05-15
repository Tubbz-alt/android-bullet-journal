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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Adapter_yearly extends BaseExpandableListAdapter {
    private Context context;
    private List<String> ListDataHeader;
    private HashMap<String, List<String>> ListHashMap;


    public Adapter_yearly(Context context, List<String> listDataHeader, HashMap<String, List<String>> listHashMap) {
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
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText=(String)getChild(groupPosition,childPosition);
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.yearly_item,null);
        }

        TextView txtListChild=(TextView)convertView.findViewById(R.id.line_text);
        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

