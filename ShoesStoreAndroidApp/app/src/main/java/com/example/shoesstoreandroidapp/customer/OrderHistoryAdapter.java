package com.example.shoesstoreandroidapp.customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesstoreandroidapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        // Nhóm và lấy đơn hàng như bạn đã có
        Map<Long, List<OrderHistoryResponse>> groupedOrders = groupOrdersById();
        long currentOrderId = getUniqueOrderIds().get(position);
        List<OrderHistoryResponse> ordersInGroup = groupedOrders.get(currentOrderId);

        OrderHistoryResponse order = ordersInGroup.get(0); // Lấy 1 đơn hàng đại diện
        StringBuilder productsDescription = new StringBuilder();
        int totalQuantity = 0;

        for (OrderHistoryResponse groupedOrder : ordersInGroup) {
            productsDescription.append(groupedOrder.getQuantity())
                    .append("x")
                    .append(groupedOrder.getProductName()).append(" ")
                    .append(" (Size: ").append(groupedOrder.getSize()).append(")\n");
            totalQuantity += groupedOrder.getQuantity();
        }

        holder.tvOrderId.setText(String.valueOf(order.getOrderId()));
        holder.tvOrderPrice.setText(String.valueOf(order.getOrderTotal()));
        holder.tvOrderDate.setText(order.getOrderDate());
        holder.tvOrderDescription.setText(productsDescription.toString());
        holder.tvOrderAddress.setText(order.getCommune() + ", " + order.getDetailedAddress() + ", " +
                order.getDistrict() + ", " + order.getProvince());

        if (order.getIsReview() == 1) {
            holder.tvReview.setText("Đã đánh giá");
            holder.tvReview.setTextColor(Color.parseColor("#F44336")); // đỏ
            holder.tvReview.setClickable(false);
            holder.tvReview.setEnabled(false); // tránh click
        } else {
            holder.tvReview.setText("Đánh giá");
            holder.tvReview.setTextColor(Color.parseColor("#00BCD4")); // xanh
            holder.tvReview.setClickable(true);
            holder.tvReview.setEnabled(true);

            // Gán sự kiện click nếu chưa đánh giá
            holder.tvReview.setOnClickListener(v -> {
                Intent intent = new Intent(context, ReviewProductActivity.class);

                List<String> productNamesList = new ArrayList<>();
                List<String> productImagesList = new ArrayList<>();
                Map<String, String> uniqueProductMap = new HashMap<>();

                for (OrderHistoryResponse groupedOrder : ordersInGroup) {
                    String productName = groupedOrder.getProductName();
                    if (!uniqueProductMap.containsKey(productName)) {
                        uniqueProductMap.put(productName, groupedOrder.getImage());
                    }
                }

                for (Map.Entry<String, String> entry : uniqueProductMap.entrySet()) {
                    productNamesList.add(entry.getKey());
                    productImagesList.add(entry.getValue());
                }

                intent.putStringArrayListExtra("productNamesList", new ArrayList<>(productNamesList));
                intent.putStringArrayListExtra("productImagesList", new ArrayList<>(productImagesList));
                intent.putExtra("orderId", currentOrderId);

               //context.startActivity(intent);
                ((Activity) context).startActivityForResult(intent, 1001);


            });
        }
    }


    @Override
    public int getItemCount() {
        return getUniqueOrderIds().size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView tvOrderId, tvOrderPrice, tvOrderDate, tvOrderDescription, tvOrderAddress, tvReview;
        ImageView gpsIcon;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderPrice = itemView.findViewById(R.id.tvOrderPrice);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderDescription = itemView.findViewById(R.id.tvOrderDescription);
            tvOrderAddress = itemView.findViewById(R.id.tvOrderAddress);
            gpsIcon = itemView.findViewById(R.id.gpsIcon);
            tvReview = itemView.findViewById(R.id.tvReview);
        }
    }

    // Nhóm các đơn hàng theo orderId
    private Map<Long, List<OrderHistoryResponse>> groupOrdersById() {
        Map<Long, List<OrderHistoryResponse>> groupedOrders = new HashMap<>();
        for (OrderHistoryResponse order : orderList) {
            if (!groupedOrders.containsKey(order.getOrderId())) {
                groupedOrders.put(order.getOrderId(), new ArrayList<>());
            }
            groupedOrders.get(order.getOrderId()).add(order);
        }
        return groupedOrders;
    }


    // Lấy danh sách các orderId duy nhất
    private List<Long> getUniqueOrderIds() {
        Map<Long, List<OrderHistoryResponse>> groupedOrders = groupOrdersById();
        return new ArrayList<>(groupedOrders.keySet());
    }
}
