package com.example.recruitmenthelper.config;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recruitmenthelper.R;
import com.example.recruitmenthelper.model.Interview;
import com.example.recruitmenthelper.popups.AddFeedbackPopUp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PastInterviewsAdapter extends RecyclerView.Adapter<PastInterviewsAdapter.ViewHolder> implements Filterable {

   LayoutInflater inflater;
   CardView cardView;
   List<Interview> interviews;
   List<Interview> interviewsListFull;
   SessionManager sessionManager;

   public PastInterviewsAdapter(Context context, List<Interview> interviews) {
      this.inflater = LayoutInflater.from(context);
      this.interviews = interviews;
      interviewsListFull = new ArrayList<>(interviews);
   }

   @NonNull
   @Override
   public PastInterviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      View view = inflater.inflate(R.layout.custom_interview_layout, parent, false);
      return new PastInterviewsAdapter.ViewHolder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull PastInterviewsAdapter.ViewHolder holder, int position) {

      holder.interviewLocation.setText("Location: " + interviews.get(position).getLocation());
      String date = interviews.get(position).getDateTime().toString().substring(0, 10);
      String time = interviews.get(position).getDateTime().toString().substring(11, 16);
      holder.interviewDateTime.setText("Date & time: " + date + " " + time);
      holder.interviewCandidate.setText("Candidate: " + interviews.get(position).getCandidateName());
      List<String> usernames = interviews.get(position).getInterviewers();
      StringBuilder interviewers = new StringBuilder();
      interviewers.append("Interviewers: ");
      for (String username : usernames) {
         interviewers.append(username).append(", ");
      }
      holder.interviewUsers.setText(interviewers.substring(0, interviewers.length() - 2));
      sessionManager = new SessionManager(holder.itemView.getContext());

   }

   @Override
   public int getItemCount() {
      return interviews.size();
   }

   public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

      TextView interviewLocation, interviewDateTime, interviewCandidate, interviewUsers;

      public ViewHolder(@NonNull View itemView) {
         super(itemView);

         interviewLocation = itemView.findViewById(R.id.view_interviewLocation);
         interviewDateTime = itemView.findViewById(R.id.view_interviewDateTime);
         interviewCandidate = itemView.findViewById(R.id.view_interviewCandidate);
         interviewUsers = itemView.findViewById(R.id.view_interviewUsers);
         cardView = itemView.findViewById(R.id.interviewCardView);

         cardView.setOnCreateContextMenuListener(this);

      }

      @Override
      public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
         menu.setHeaderTitle("Select an action");
         menu.add(this.getAdapterPosition(), 0, 0, "Add feedback");
      }
   }

   @Override
   public Filter getFilter() {
      return interviewFilter;
   }

   private Filter interviewFilter = new Filter() {
      @Override
      protected FilterResults performFiltering(CharSequence charSequence) {
         List<Interview> filteredList = new ArrayList<>();

         if (charSequence == null || charSequence.length() == 0) {
            filteredList.addAll(interviewsListFull);
         } else {
            String filterPattern = charSequence.toString().toLowerCase(Locale.ROOT).trim();

            for (Interview interview : interviewsListFull) {
               if (interview.getLocation().toLowerCase(Locale.ROOT).contains(filterPattern)) {
                  filteredList.add(interview);
               } else if (interview.getCandidateName().toLowerCase(Locale.ROOT).contains(filterPattern)) {
                  filteredList.add(interview);
               } else if (interview.getDateTime().toString().toLowerCase(Locale.ROOT).contains(filterPattern)) {
                  filteredList.add(interview);
               }

               for (int i = 0; i < interview.getInterviewers().size(); i++) {
                  if (interview.getInterviewers().get(i).contains(filterPattern)) {
                     filteredList.add(interview);
                     break;
                  }
               }
            }
         }
         FilterResults filterResults = new FilterResults();
         filterResults.values = filteredList;

         return filterResults;
      }

      @Override
      protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
         interviews.clear();
         interviews.addAll((List) filterResults.values);
         notifyDataSetChanged();
      }
   };

   public Intent transferInterviewData(int position) {
      Intent intent = new Intent(cardView.getContext(), AddFeedbackPopUp.class);
      intent.putExtra("interviewId", String.valueOf(interviews.get(position).getInterviewId()));
      return intent;
   }
}