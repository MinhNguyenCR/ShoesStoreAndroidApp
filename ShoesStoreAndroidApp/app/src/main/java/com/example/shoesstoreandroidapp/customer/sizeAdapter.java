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

import com.example.shoesstoreandroidapp.R;

import java.util.List;

public class sizeAdapter extends RecyclerView.Adapter<sizeAdapter.ViewHolder> {
    private List<String> sizes;
    private Context context;
    private int selectedPosition = 0; // Chỉ số của nút được chọn

    public sizeAdapter(Context context, List<String> categories) {
        this.context = context;
        this.sizes = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.size_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.sizeTextView.setText(sizes.get(position));

        int adapterPosition = holder.getAdapterPosition();
        if (adapterPosition == RecyclerView.NO_POSITION) return;

        // Đổi màu khi được chọn
        if (adapterPosition == selectedPosition) {
            holder.sizeTextView.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_selected));
            holder.sizeTextView.setTextColor(Color.WHITE);
        } else {
            holder.sizeTextView.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_unselected));
            holder.sizeTextView.setTextColor(Color.BLACK);
        }

        // Xử lý sự kiện click
        holder.sizeTextView.setOnClickListener(v -> {
            selectedPosition = adapterPosition;
            notifyDataSetChanged(); // Cập nhật UI
        });
    }


    @Override
    public int getItemCount() {
        return sizes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView sizeTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sizeTextView = itemView.findViewById(R.id.sizeTextView);
        }
    }
}
