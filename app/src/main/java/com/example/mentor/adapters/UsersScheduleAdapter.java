package com.example.mentor.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentor.R;
import com.example.mentor.databinding.LayoutItemContainerUserProposalBinding;
import com.example.mentor.databinding.LayoutItemContainerUserScheduleBinding;
import com.example.mentor.misc.Proposal;
import com.example.mentor.misc.ProposalListener;
import com.example.mentor.misc.User;
import com.example.mentor.misc.UserListener;

import java.util.List;

public class UsersScheduleAdapter extends RecyclerView.Adapter<UsersScheduleAdapter.UserViewHolder>{

    private final List<Proposal> proposal;
    private final ProposalListener proposalListener;

    public UsersScheduleAdapter(List<Proposal> proposal, ProposalListener proposalListener) {
        this.proposal = proposal;
        this.proposalListener = proposalListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemContainerUserScheduleBinding itemBinding = LayoutItemContainerUserScheduleBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new UserViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(proposal.get(position));
    }

    @Override
    public int getItemCount() {
        return proposal.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        LayoutItemContainerUserScheduleBinding binding;

        UserViewHolder(LayoutItemContainerUserScheduleBinding itemBinding){
            super(itemBinding.getRoot());
            binding = itemBinding;
        }
        void setUserData(Proposal proposal){
            binding.txtReqUID.setText(proposal.uid);
            binding.txtFullName.setText(proposal.fullName);
            if(proposal.picString.trim().isEmpty()){
                binding.imgUserPic.setImageResource(R.drawable.ic_baseline_person_24);
                binding.imgUserPic.setColorFilter(Color.argb(255, 100, 100, 100));
            }else{
                byte[] bytes = Base64.decode(proposal.picString, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
                Bitmap croppedBmp = Bitmap.createBitmap(bitmap, 0, 0, cropSize,cropSize);
                binding.imgUserPic.setImageBitmap(croppedBmp);
            }
            binding.txtSubject.setText(proposal.subject);
            binding.txtDate.setText(proposal.date);

            binding.getRoot().setOnClickListener(view -> proposalListener.onUserClicked(proposal));
        }
    }
}
