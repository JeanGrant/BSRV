package com.example.mentor.adapters;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentor.databinding.LayoutSubjectContainerBinding;
import com.example.mentor.misc.SubjectRates;

import java.util.List;

public class SubjectRatesAdapter extends RecyclerView.Adapter<SubjectRatesAdapter.UserViewHolder>{

    private final List<SubjectRates> subjectRates;

    public SubjectRatesAdapter(List<SubjectRates> subjectRates) {
        this.subjectRates = subjectRates;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutSubjectContainerBinding subjectContainerBinding = LayoutSubjectContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new UserViewHolder(subjectContainerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(subjectRates.get(position));
    }

    @Override
    public int getItemCount() {
        return subjectRates.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        LayoutSubjectContainerBinding binding;

        UserViewHolder(LayoutSubjectContainerBinding subjectContainerBinding){
            super(subjectContainerBinding.getRoot());
            binding = subjectContainerBinding;
        }
        void setUserData(SubjectRates subjectRates){
            binding.imgSubject.setImageDrawable(subjectRates.drawable);
            binding.txtSubject.setText(subjectRates.name);
            if(subjectRates.rate != null) {
                if (!subjectRates.rate.trim().isEmpty()) {binding.txtSubjectRates.setText(subjectRates.rate);}
                else{binding.txtSubjectRates.setVisibility(View.GONE);}
            }else{binding.txtSubjectRates.setVisibility(View.GONE);}
            binding.layoutSubject.getBackground().setColorFilter(
                    subjectRates.hexColor,
                    PorterDuff.Mode.SRC_ATOP);
        }
    }
}
