package com.example.shoesstoreandroidapp.customer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoesstoreandroidapp.R;

public class shoes_item_adapter extends RecyclerView.Adapter<shoes_item_adapter.ViewHolder> {

    private Context context;
    private List<shoesModel> mList;

    public shoes_item_adapter(Context context, List<shoesModel> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.shoes_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        shoesModel model = mList.get(position);

        holder.tvName.setText(model.getShoesName());
        holder.tvPrice.setText("₫"+String.valueOf(model.getPrice()));
        holder.tvRating.setText(String.valueOf(model.getRating()));

        if (model.getImage() != null && !model.getImage().isEmpty()) {
            Glide.with(context)
                    .load(model.getImage())
                    .into(holder.imgvImage);

        }
    }


    @Override
    public int getItemCount() {
        if(mList != null)
            return mList.size();
        return 0;
    }
    public void updateList(List<shoesModel> newList) {
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
