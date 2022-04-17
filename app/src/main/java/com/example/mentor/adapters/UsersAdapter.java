package com.example.mentor.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentor.R;
import com.example.mentor.databinding.LayoutItemContainerUserBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.misc.User;
import com.example.mentor.misc.UserListener;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>{

    private final List<User> users;
    private final UserListener userListener;

    public UsersAdapter(List<User> users, UserListener userListener) {
        this.users = users;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemContainerUserBinding itemContainerUserBinding = LayoutItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new UserViewHolder(itemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        LayoutItemContainerUserBinding binding;

        UserViewHolder(LayoutItemContainerUserBinding itemContainerUserBinding){
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;
        }
        void setUserData(User user){
            binding.txtFullName.setText(user.fullName);
            binding.txtFullName.setEllipsize(TextUtils.TruncateAt.END);
            binding.txtFullName.setPadding(0,0,20,0);
            binding.txtEmail.setText(user.email);
            binding.txtEmail.setEllipsize(TextUtils.TruncateAt.END);
            binding.txtEmail.setPadding(0,0,20,0);
            if(user.pictureStr.trim().isEmpty()){
                binding.imgUserPic.setImageResource(R.drawable.ic_baseline_person_24);
                binding.imgUserPic.setColorFilter(Color.argb(255, 100, 100, 100));
            }else{
                byte[] bytes = Base64.decode(user.pictureStr, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
                Bitmap croppedBmp = Bitmap.createBitmap(bitmap, 0, 0, cropSize,cropSize);
                binding.imgUserPic.setImageBitmap(croppedBmp);
            }
            if(user.isMentor) {
                binding.ratingMentor.setVisibility(View.VISIBLE);
                if(user.rating != null) {
                    binding.ratingMentor.setRating(user.rating);
                }
                String priceRange="";
                if(user.minFee.equals(user.maxFee)){
                    priceRange = "Price Range: " + user.minFee;
                }else {
                    priceRange = "Price Range: " + user.minFee + " - " + user.maxFee;
                }
                binding.txtPriceRange.setVisibility(View.VISIBLE);
                binding.txtPriceRange.setText(priceRange);
            }else {
                binding.ratingMentor.setVisibility(View.GONE);
                binding.txtPriceRange.setVisibility(View.GONE);
            }

            binding.getRoot().setOnClickListener(view -> userListener.onUserClicked(user));
        }
    }
}
