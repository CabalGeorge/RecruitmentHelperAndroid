package com.example.recruitmenthelper.config;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.recruitmenthelper.model.User;
import com.example.recruitmenthelper.popups.EditUserPopUp;
import com.example.recruitmenthelper.popups.FullProfilePopUp;

import java.util.List;

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.ViewHolder> {

    LayoutInflater inflater;
    CardView cardView;
    List<Candidate> candidates;
    SessionManager sessionManager;

    public CandidateAdapter(Context context, List<Candidate> candidates) {
        this.inflater = LayoutInflater.from(context);
        this.candidates = candidates;
    }

    @NonNull
    @Override
    public CandidateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.custom_candidate_layout, parent, false);
        return new CandidateAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CandidateAdapter.ViewHolder holder, int position) {

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
            menu.add(this.getAdapterPosition(), 1, 1, "Archive this candidate");
            menu.add(this.getAdapterPosition(), 2, 2, "Delete this candidate");
            menu.add(this.getAdapterPosition(), 3, 3, "Schedule an interview");
        }
    }

    public Intent transferCandidateDataToProfilePopUp(int position) {
        Intent intent = new Intent(cardView.getContext(), FullProfilePopUp.class);
        intent.putExtra("id", String.valueOf(candidates.get(position).getCandidateId()));
        return intent;
    }

    public Intent transferCandidateDataToInterviewPopUp(int position) {
        Intent intent = new Intent(cardView.getContext(), FullProfilePopUp.class);
        intent.putExtra("id", String.valueOf(candidates.get(position).getCandidateId()));
        return intent;
    }

    public void deleteCandidate(int position) {
        RequestQueue requestQueue = Volley.newRequestQueue(cardView.getContext());

        String urlDelete = Constant.DELETE_CANDIDATE_URL + "/" + candidates.get(position).getCandidateId();
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, urlDelete, response -> {
            Toast.makeText(cardView.getContext(), "Candidate successfully deleted!", Toast.LENGTH_LONG).show();
        }, error -> Toast.makeText(cardView.getContext(), "error" + error.toString(), Toast.LENGTH_LONG).show()) {};

        requestQueue.add(stringRequest);
        candidates.remove(position);
        notifyItemRemoved(position);

    }

    public void archiveCandidate(int position) {
        RequestQueue requestQueue = Volley.newRequestQueue(cardView.getContext());

        String urlDelete = Constant.ARCHIVE_CANDIDATE_URL + "/" + candidates.get(position).getCandidateId() + "/archive";
        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, urlDelete, response -> {
            Toast.makeText(cardView.getContext(), "Candidate successfully archived!", Toast.LENGTH_LONG).show();
        }, error -> Toast.makeText(cardView.getContext(), "error" + error.toString(), Toast.LENGTH_LONG).show()) {};

        requestQueue.add(stringRequest);
        candidates.remove(position);
        notifyItemRemoved(position);

    }
}


