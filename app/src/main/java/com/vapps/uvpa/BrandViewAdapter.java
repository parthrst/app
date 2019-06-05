package com.vapps.uvpa;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class BrandViewAdapter extends RecyclerView.Adapter<BrandViewHolder>

{
    List<Brands> brandlist;
    Context context;


    public BrandViewAdapter(Context context,List<Brands> brandlist)
    {

        this.brandlist = brandlist;
        this.context = context;

    }



    @Override
    public BrandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_list,parent,false);
        BrandViewHolder brandViewHolder = new BrandViewHolder(view);
        return brandViewHolder;

    }

    @Override
    public void onBindViewHolder(BrandViewHolder holder, int position)
    {
       holder.textViewBrandName.setText(brandlist.get(position).brandName);
       holder.imageView.setImageResource(R.mipmap.rb1);

    }

    @Override
    public int getItemCount()
    {
        return brandlist.size();
    }
}
