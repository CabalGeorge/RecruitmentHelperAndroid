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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.recruitmenthelper.R;
import com.example.recruitmenthelper.model.Candidate;
import com.example.recruitmenthelper.popups.FullProfilePopUp;
import com.example.recruitmenthelper.popups.InterviewCreationPopUp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyCandidateAdapter extends RecyclerView.Adapter<MyCandidateAdapter.ViewHolder> implements Filterable {

    LayoutInflater inflater;
    CardView cardView;
    List<Candidate> candidates;
    List<Candidate> candidatesListFull;
    SessionManager sessionManager;

    public MyCandidateAdapter(Context context, List<Candidate> candidates) {
        this.inflater = LayoutInflater.from(context);
        this.candidates = candidates;
        candidatesListFull = new ArrayList<>(candidates);
    }

    @NonNull
    @Override
    public MyCandidateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.custom_candidate_layout, parent, false);
        return new MyCandidateAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCandidateAdapter.ViewHolder holder, int position) {

        holder.candidateName.setText("Name: " + candidates.get(position).getFirstName() + " " + candidates.get(position).getLastName());
        holder.candidateEmail.setText("Email: " + candidates.get(position).getEmail());
        holder.candidatePhone.setText("Phone: " + candidates.get(position).getPhone());
        holder.candidateCity.setText("City: " + candidates.get(position).getCity());
        holder.candidatePosition.setText("Position: " + candidates.get(position).getInterestPosition());
        sessionManager = new SessionManager(holder.itemView.getContext());
    }

    @Override
    public int getItemCount() {
        return candidates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView candidateName, candidateEmail, candidatePhone, candidateCity, candidatePosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            candidateName = itemView.findViewById(R.id.txt_candidateName);
            candidateEmail = itemView.findViewById(R.id.txt_candidateEmail);
            candidatePhone = itemView.findViewById(R.id.txt_candidatePhone);
            candidateCity = itemView.findViewById(R.id.txt_candidateCity);
            candidatePosition = itemView.findViewById(R.id.txt_candidatePosition);
            cardView = itemView.findViewById(R.id.candidateCardView);

            cardView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select an action");
            menu.add(this.getAdapterPosition(), 0, 0, "View full profile");
        }
    }

    @Override
    public Filter getFilter() {
        return candidateFilter;
    }

    private Filter candidateFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Candidate> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(candidatesListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase(Locale.ROOT).trim();

                for (Candidate candidate : candidatesListFull) {
                    if (candidate.getFirstName().toLowerCase(Locale.ROOT).contains(filterPattern)) {
                        filteredList.add(candidate);
                    } else if (candidate.getLastName().toLowerCase(Locale.ROOT).contains(filterPattern)) {
                        filteredList.add(candidate);
                    } else if (candidate.getCity().toLowerCase(Locale.ROOT).contains(filterPattern)) {
                        filteredList.add(candidate);
                    } else if (candidate.getInterestPosition().toLowerCase(Locale.ROOT).contains(filterPattern)) {
                        filteredList.add(candidate);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            candidates.clear();
            candidates.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public Intent transferCandidateDataToProfilePopUp(int position) {
        Intent intent = new Intent(cardView.getContext(), FullProfilePopUp.class);
        intent.putExtra("id", String.valueOf(candidates.get(position).getCandidateId()));
        return intent;
    }
}
