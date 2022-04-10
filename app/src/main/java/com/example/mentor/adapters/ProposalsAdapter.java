package com.example.mentor.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentor.databinding.LayoutItemContainerProposalBinding;
import com.example.mentor.misc.Proposal;
import com.example.mentor.misc.ProposalListener;

import java.util.List;

public class ProposalsAdapter extends RecyclerView.Adapter<ProposalsAdapter.UserViewHolder>{

    private final List<Proposal> proposals;
    private final ProposalListener proposalListener;

    public ProposalsAdapter(List<Proposal> proposals, ProposalListener proposalListener) {
        this.proposals = proposals;
        this.proposalListener = proposalListener;
    }

    @NonNull
    @Override
    public ProposalsAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemContainerProposalBinding proposalBinding = LayoutItemContainerProposalBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new ProposalsAdapter.UserViewHolder(proposalBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProposalsAdapter.UserViewHolder holder, int position) {
        holder.setUserData(proposals.get(position));
    }

    @Override
    public int getItemCount() {
        return proposals.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        LayoutItemContainerProposalBinding binding;

        UserViewHolder(LayoutItemContainerProposalBinding itemContainerProposalBinding){
            super(itemContainerProposalBinding.getRoot());
            binding = itemContainerProposalBinding;
        }
        void setUserData(Proposal proposal){
            binding.txtDate.setText(proposal.date);
            binding.txtSubject.setText(proposal.subject);

            String time = proposal.startTime+" - "+proposal.endTime;

            binding.txtTime.setText(time);

            binding.getRoot().setOnClickListener(view -> proposalListener.onUserClicked(proposal));
        }
    }
}