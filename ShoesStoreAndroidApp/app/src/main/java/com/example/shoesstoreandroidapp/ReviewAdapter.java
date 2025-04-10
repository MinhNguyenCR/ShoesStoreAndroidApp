package com.example.shoesstoreandroidapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<ReviewModal> reviewList;
    private Context rContext;
    private LayoutInflater rLayoutInflater;

    public ReviewAdapter(Context context,List<ReviewModal> reviewList) {
        rContext = context;
        this.reviewList = reviewList;
        rLayoutInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = rLayoutInflater.inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewModal review = reviewList.get(position);
        holder.txtReviewerName.setText(review.getUserName());
        holder.ratingBarReview.setRating(review.getRating());
        holder.txtReviewText.setText(review.getComment());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView txtReviewerName, txtReviewText;
        RatingBar ratingBarReview;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            txtReviewerName = itemView.findViewById(R.id.txtReviewerName);
            ratingBarReview = itemView.findViewById(R.id.ratingBarReview);
            txtReviewText = itemView.findViewById(R.id.txtReviewText);
        }
    }
}
