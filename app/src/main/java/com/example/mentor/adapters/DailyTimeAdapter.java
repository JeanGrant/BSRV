package com.example.mentor.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentor.R;
import com.example.mentor.databinding.LayoutDailytimeCellBinding;
import com.example.mentor.misc.ClickedUser_Schedule;

import java.util.List;

public class DailyTimeAdapter extends RecyclerView.Adapter<DailyTimeAdapter.UserViewHolder>{

    private final List<String> dayTime;
    private final List<ClickedUser_Schedule> schedule;

    public DailyTimeAdapter(List<String> dateTime, List<ClickedUser_Schedule> schedule) {
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
        if(position>0) {
            if (schedule.get(position).reqName.equals(schedule.get(position - 1).reqName)) {
                schedule.get(position).txtColor = schedule.get(position).bgColor;
            }
        }
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
        void setUserData(String dailyTime, ClickedUser_Schedule scheduleName){
            binding.txtDailyTime.setText(dailyTime);
            if(scheduleName.reqName.isEmpty()){
                Log.i("scheduleName", "isEmpty " + scheduleName.reqName);
                binding.txtSchedule.setBackgroundColor(Color.argb(255, 255,255,255));
            }else{

                Log.i("scheduleName", "!isEmpty " + scheduleName.reqName);
                binding.txtSchedule.setBackgroundColor(scheduleName.bgColor);
                String header = scheduleName.reqName + " " + scheduleName.reqSubject;
                binding.txtSchedule.setText(header);
                binding.txtSchedule.setTextColor(scheduleName.txtColor);
            }
        }
    }
}
