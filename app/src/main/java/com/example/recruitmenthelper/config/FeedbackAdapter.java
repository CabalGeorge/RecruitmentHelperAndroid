package com.example.recruitmenthelper.config;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recruitmenthelper.R;
import com.example.recruitmenthelper.model.Feedback;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {

    LayoutInflater inflater;
    CardView cardView;
    List<Feedback> feedbacks;
    SessionManager sessionManager;

    public FeedbackAdapter(Context context, List<Feedback> feedbacks) {
        this.inflater = LayoutInflater.from(context);
        this.feedbacks = feedbacks;
    }

    @NonNull
    @Override
    public FeedbackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.custom_feedback_layout, parent, false);
        return new FeedbackAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackAdapter.ViewHolder holder, int position) {

        holder.feedbackFrom.setText("Feedback from " + feedbacks.get(position).getUsername());
        holder.feedbackText.setText(feedbacks.get(position).getFeedback());
        holder.feedbackStatus.setText("Status: " + feedbacks.get(position).getStatus());
        sessionManager = new SessionManager(holder.itemView.getContext());
    }

    @Override
    public int getItemCount() {
        return feedbacks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView feedbackFrom, feedbackText, feedbackStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            feedbackFrom = itemView.findViewById(R.id.view_feedback_owner);
            feedbackText = itemView.findViewById(R.id.view_feedback_text);
            feedbackStatus = itemView.findViewById(R.id.view_feedback_status);
            cardView = itemView.findViewById(R.id.feedbackCardView);

            cardView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        }
    }
}
