package com.example.mentor.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentor.databinding.LayoutDailytimeCellBinding;

import java.util.List;

public class DailyTimeAdapter extends RecyclerView.Adapter<DailyTimeAdapter.UserViewHolder>{

    private final List<String> dayTime;
    private final List<String> schedule;

    public DailyTimeAdapter(List<String> dateTime, List<String> schedule) {
        this.dayTime = dateTime;
        this.schedule = schedule;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutDailytimeCellBinding binding = LayoutDailytimeCellBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(dayTime.get(position), schedule.get(position));
    }

    @Override
    public int getItemCount() {
        return dayTime.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        LayoutDailytimeCellBinding binding;

        UserViewHolder(LayoutDailytimeCellBinding dailytimeCellBinding){
            super(dailytimeCellBinding.getRoot());
            binding = dailytimeCellBinding;
        }
        void setUserData(String dailyTime, String scheduleName){
            binding.txtDailyTime.setText(dailyTime);
            if(scheduleName.isEmpty()){
                binding.txtSchedule.setBackgroundColor(Color.argb(255, 255,255,255));
            }else{
                binding.txtSchedule.setBackgroundColor(Color.argb(255,0,0,0));
                binding.txtSchedule.setText(scheduleName);
            }
        }
    }
}
