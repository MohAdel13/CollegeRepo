package com.example.onlineshop.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.R;
import com.example.onlineshop.databinding.FeedbackItemBinding;
import com.example.onlineshop.pojo.Models.FeedbackModel;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {
    List<FeedbackModel> feedbacks;
    Context context;

    public FeedbackAdapter(Context context)
    {
        this.context = context;
    }

    @NonNull
    @Override
    public FeedbackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //link the view holder to the layout
        FeedbackItemBinding binding = FeedbackItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FeedbackAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackAdapter.ViewHolder holder, int position) {
        FeedbackItemBinding binding = holder.holderBinding;

        //write the data on views of feedback item
        binding.feedbackMsgTV.setText(feedbacks.get(position).message);

        //make the rating stars as the count of rating
        binding.feedbackRate1StarBTN.setImageDrawable(context.getDrawable(R.drawable.baseline_star_rate_26));

        if(feedbacks.get(position).rating > 1)
        {
            binding.feedbackRate2StarBTN.setImageDrawable(context.getDrawable(R.drawable.baseline_star_rate_26));
        }

        if(feedbacks.get(position).rating > 2)
        {
            binding.feedbackRate3StarBTN.setImageDrawable(context.getDrawable(R.drawable.baseline_star_rate_26));
        }

        if(feedbacks.get(position).rating > 3)
        {
            binding.feedbackRate4StarBTN.setImageDrawable(context.getDrawable(R.drawable.baseline_star_rate_26));
        }

        if(feedbacks.get(position).rating > 4)
        {
            binding.feedbackRate5StarBTN.setImageDrawable(context.getDrawable(R.drawable.baseline_star_rate_26));
        }
    }

    @Override
    public int getItemCount() {
        return feedbacks.size();
    }

    public void setFeedbacks(List<FeedbackModel> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FeedbackItemBinding holderBinding;
        public ViewHolder(@NonNull FeedbackItemBinding binding) {
            super(binding.getRoot());
            holderBinding = binding;
        }
    }
}
