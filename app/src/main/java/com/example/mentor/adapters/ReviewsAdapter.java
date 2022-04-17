package com.example.mentor.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentor.databinding.LayoutItemContainerReviewsBinding;
import com.example.mentor.misc.Reviews;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.UserViewHolder>{

    private final List<Reviews> reviews;

    public ReviewsAdapter(List<Reviews> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemContainerReviewsBinding itemContainerReviewsBinding = LayoutItemContainerReviewsBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new UserViewHolder(itemContainerReviewsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        LayoutItemContainerReviewsBinding binding;

        UserViewHolder(LayoutItemContainerReviewsBinding itemContainerReviewsBinding){
            super(itemContainerReviewsBinding.getRoot());
            binding = itemContainerReviewsBinding;
        }
        void setUserData(Reviews reviews){
            binding.txtFullName.setText(reviews.fullName);
            binding.ratingMentor.setRating(reviews.rating);
            binding.txtDateTime.setText(reviews.dateTime);
            if(reviews.comment.trim().isEmpty()){
                binding.txtComment.setVisibility(View.GONE);
            }else {
                binding.txtComment.setVisibility(View.VISIBLE);
                binding.txtComment.setText(reviews.comment);
            }
        }
    }
}
