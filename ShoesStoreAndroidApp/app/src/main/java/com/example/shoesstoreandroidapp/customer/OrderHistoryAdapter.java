package com.example.shoesstoreandroidapp.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesstoreandroidapp.R;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private Context context;
    private List<OrderHistoryResponse> orderList;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<OrderHistoryResponse> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderHistoryResponse> orderList) {
        this.orderList = orderList;
    }

    public OrderHistoryAdapter(Context context, List<OrderHistoryResponse> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderHistoryResponse order = orderList.get(position);
        holder.tvOrderId.setText(order.getOrderId().toString());
        holder.tvOrderPrice.setText(order.getOrderTotal().toString());
        holder.tvOrderDate.setText(order.getOrderDate());
        holder.tvOrderDescription.setText(order.getQuantity()+"x"+order.getProductName());
        holder.tvOrderAddress.setText(order.getCommune()+","+order.getDetailedAddress()+","+
                order.getDistrict()+","+order.getProvince());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView tvOrderId, tvOrderPrice, tvOrderDate, tvOrderDescription, tvOrderAddress;
        ImageView gpsIcon;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderPrice = itemView.findViewById(R.id.tvOrderPrice);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderDescription = itemView.findViewById(R.id.tvOrderDescription);
            tvOrderAddress = itemView.findViewById(R.id.tvOrderAddress); // Bạn cần thêm ID này trong XML
            gpsIcon = itemView.findViewById(R.id.gpsIcon); // Tương tự
        }
    }
}
