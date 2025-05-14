package com.example.shoesstoreandroidapp.customer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoesstoreandroidapp.customer.ProductDetailActivity;
import com.example.shoesstoreandroidapp.R;
import com.example.shoesstoreandroidapp.customer.Model.ProductModel;


import java.util.List;

public class ListProductAdapter extends RecyclerView.Adapter<ListProductAdapter.ViewHolder> {

    private Context context;
    private List< ProductModel> mList;

    public ListProductAdapter(Context context, List< ProductModel> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoes_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel listProductModel = mList.get(position);
        holder.tvName.setText(listProductModel.getName());
        holder.tvPrice.setText(String.format("%,.0f VND", listProductModel.getPrice()));

        // Làm tròn số sao đánh giá đến 1 chữ số sau dấu phẩy
        double rating = listProductModel.getFeedbackStar();
        String formattedRating = String.format("%.1f", rating);  // Làm tròn đến 1 chữ số sau dấu phẩy
        holder.tvRating.setText(formattedRating);

        Glide.with(context).load(listProductModel.getImage()).into(holder.imgvImage);

        holder.imgvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khi click ảnh mở ProductDetailActivity truyền tên sản phẩm
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("productName", listProductModel.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mList != null)
            return mList.size();
        return 0;
    }
    public void updateList(List< ProductModel> newList) {
        this.mList = newList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvPrice;
        private TextView tvRating;
        private ImageView imgvImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.shoe_name);
            tvPrice = (TextView) itemView.findViewById(R.id.shoe_price);
            tvRating = (TextView) itemView.findViewById(R.id.shoe_rating);
            imgvImage = (ImageView) itemView.findViewById(R.id.shoe_image);

        }
    }
}

