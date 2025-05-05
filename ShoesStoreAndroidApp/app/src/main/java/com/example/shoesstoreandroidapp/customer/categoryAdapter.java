package com.example.shoesstoreandroidapp.customer;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shoesstoreandroidapp.customer.OnCategoryClickListener;


import com.example.shoesstoreandroidapp.R;

import java.util.List;

public class categoryAdapter extends RecyclerView.Adapter<categoryAdapter.ViewHolder> {
    private List<String> categories;
    private Context context;
    private int selectedPosition = 0; // Chỉ số của nút được chọn
    private OnCategoryClickListener listener;

    public categoryAdapter(Context context, List<String> categories) {
        this.context = context;
        this.categories = categories;
    }

    public categoryAdapter(Context context, List<String> categories, OnCategoryClickListener listener) {
        this.context = context;
        this.categories = categories;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.categoryTextView.setText(categories.get(position));

        int adapterPosition = holder.getAdapterPosition();
        if (adapterPosition == RecyclerView.NO_POSITION) return;

        // Đổi màu khi được chọn
        if (adapterPosition == selectedPosition) {
            holder.categoryTextView.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_selected));
            holder.categoryTextView.setTextColor(Color.WHITE);
        } else {
            holder.categoryTextView.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_unselected));
            holder.categoryTextView.setTextColor(Color.BLACK);
        }

        // Xử lý sự kiện click vào category
        holder.categoryTextView.setOnClickListener(v -> {
            selectedPosition = adapterPosition;
            notifyDataSetChanged();
            if (listener != null) {
                listener.onCategoryClick(categories.get(adapterPosition));
            }
        });
    }


    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
        }
    }
}

