package com.vapps.uvpa;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BrandViewHolder extends RecyclerView.ViewHolder
{
    ImageView imageView;
    TextView  textViewBrandName;

  public BrandViewHolder(View itemView)
  {
         super(itemView);

         imageView = itemView.findViewById(R.id.brandImage);
         textViewBrandName = itemView.findViewById(R.id.brandNames);


  }
}
