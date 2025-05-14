package com.example.shoesstoreandroidapp.customer.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoesstoreandroidapp.R;
import com.example.shoesstoreandroidapp.customer.Model.ReviewProductModel;
import com.example.shoesstoreandroidapp.customer.Request.FeedbackRequest;

import java.util.ArrayList;
import java.util.List;

public class ReviewProductAdapter extends RecyclerView.Adapter<ReviewProductAdapter.ViewHolder> {

    private Context context;
    private List<ReviewProductModel> productList;
    private Long userId;

    public ReviewProductAdapter(Context context, List<ReviewProductModel> productList, Long userId) {
        this.context = context;
        this.productList = productList;
        this.userId = userId;
    }

    // Trả về danh sách FeedbackRequest để gửi lên API
    public List<FeedbackRequest> getFeedbackRequests() {
        List<FeedbackRequest> feedbackList = new ArrayList<>();
        for (ReviewProductModel product : productList) {
            FeedbackRequest feedback = new FeedbackRequest();
            feedback.setComment(product.getComment());
            feedback.setRate((int) product.getRating());
            feedback.setUser_id(userId);
            feedback.setProductName(product.getProductName());
            feedbackList.add(feedback);
        }
        return feedbackList;
    }

    @NonNull
    @Override
    public ReviewProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewProductAdapter.ViewHolder holder, int position) {
        ReviewProductModel product = productList.get(position);

        holder.txtProductName.setText(product.getProductName());

        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imgProduct);

        holder.ratingBar.setRating(product.getRating());
        holder.edtComment.setText(product.getComment());

        holder.ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            product.setRating(rating);
        });

        holder.edtComment.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                product.setComment(s.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduct;
        TextView txtProductName;
        RatingBar ratingBar;
        EditText edtComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            edtComment = itemView.findViewById(R.id.edtComment);
        }
    }
}
