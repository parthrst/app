package com.vapps.uvpa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Dataadapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Header> headlist;

    public Dataadapter(Context context, ArrayList<Header> headlist) {
        this.context = context;
        this.headlist = headlist;
    }





    @Override
    public int getGroupCount() {
        return headlist.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<body> displist=headlist.get(groupPosition).getList();

        return displist.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headlist.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<body> displist=headlist.get(groupPosition).getList();
        return displist.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Header headerinfo=(Header)getGroup(groupPosition);
        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.header_layout,null);

        }

        TextView model=convertView.findViewById(R.id.model);
        TextView brand=convertView.findViewById(R.id.brand);
        TextView id=convertView.findViewById(R.id.id);
        model.setText(headerinfo.getModel().trim());
        id.setText(headerinfo.getId().trim());
        brand.setText(headerinfo.getBrand().trim());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        body details=(body)getChild(groupPosition,childPosition);

        if (convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.body_layout ,null);
        }
        TextView total=convertView.findViewById(R.id.total);
        TextView txnId=convertView.findViewById(R.id.txnid);
        TextView address=convertView.findViewById(R.id.address);
        TextView txnStatus=convertView.findViewById(R.id.txnstatus);
        TextView problem=convertView.findViewById(R.id.problem);
        TextView backup=convertView.findViewById(R.id.backup);
        total.setText(details.getTotal().trim());
        txnId.setText(details.getPhoneNo().trim());
        txnStatus.setText(details.getTxnStatus().trim());
        problem.setText(details.getProblemids().trim());
        backup.setText(details.getBackupPhone().trim());
        address.setText(details.getAddress().trim());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
