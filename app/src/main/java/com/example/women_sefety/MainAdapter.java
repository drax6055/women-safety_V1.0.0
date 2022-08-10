package com.example.women_sefety;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends BaseAdapter {
    Contatct_List parentActivity;
    ArrayList<EmergencyContact>arrayList;

    public MainAdapter(Contatct_List parentActivity) {
        this.parentActivity = parentActivity;
        arrayList=new ArrayList<>();
        arrayList=parentActivity.myDbHelper.getAllContacts();
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name=arrayList.get(position).getName();
        String number=arrayList.get(position).getNumber();
        LayoutInflater inflater=LayoutInflater.from(parentActivity);
        convertView=inflater.inflate(R.layout.item_contact,parent,false);
        TextView textname=convertView.findViewById(R.id.tv_name);
        TextView textnumber=convertView.findViewById(R.id.tv_number);
        textname.setText(name);
        textnumber.setText(number);
        convertView.findViewById(R.id.imgdelete).setOnClickListener(view -> {
            parentActivity.myDbHelper.DeleteContact(arrayList.get(position).getNumber());
            refreshadapter();
        });
        return convertView;
    }
    public void  refreshadapter()
    {
        arrayList.clear();
        arrayList=parentActivity.myDbHelper.getAllContacts();
        notifyDataSetChanged();
    }
}
