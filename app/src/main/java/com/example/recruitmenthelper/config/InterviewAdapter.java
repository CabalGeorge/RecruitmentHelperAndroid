package com.example.recruitmenthelper.config;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.recruitmenthelper.model.Interview;
import com.example.recruitmenthelper.model.User;

import java.util.List;

public class InterviewAdapter extends RecyclerView.Adapter<InterviewAdapter.ViewHolder> {

    LayoutInflater inflater;
    CardView cardView;
    List<Interview> interviews;
    SessionManager sessionManager;

    public InterviewAdapter(Context context, List<Interview> interviews) {
        this.inflater = LayoutInflater.from(context);
        this.interviews = interviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.custom_interview_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.interviewLocation.setText("Location: " + interviews.get(position).getLocation());
        String date = interviews.get(position).getDateTime().toString().substring(0,10);
        String time = interviews.get(position).getDateTime().toString().substring(11, 16);
        holder.interviewDateTime.setText("Date & time: " + date + " " + time);
        holder.interviewCandidate.setText("Candidate: " + interviews.get(position).getCandidateName());
        List<String> usernames = interviews.get(position).getInterviewers();
        StringBuilder interviewers = new StringBuilder();
        interviewers.append("Interviewers: ");
        for (String username : usernames) {
            interviewers.append(username).append(", ");
        }
        holder.interviewUsers.setText(interviewers);
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
            menu.add(this.getAdapterPosition(), 0, 0, "Cancel this interview");
        }
    }

    public void deleteInterview(int position) {
        RequestQueue requestQueue = Volley.newRequestQueue(cardView.getContext());

        String urlDelete = Constant.DELETE_INTERVIEW_URL + "/" + interviews.get(position).getInterviewId();
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, urlDelete, response -> {
            Toast.makeText(cardView.getContext(), "Interview successfully deleted!", Toast.LENGTH_LONG).show();
        }, error -> Toast.makeText(cardView.getContext(), "error" + error.toString(), Toast.LENGTH_LONG).show()) {
        };
        requestQueue.add(stringRequest);
        interviews.remove(position);
        notifyItemRemoved(position);
    }


//    public Intent transferData(int position) {
//        Intent intent = new Intent(cardView.getContext(), EditUserPopUp.class);
//        intent.putExtra("id", String.valueOf(interviews.get(position).getUser_id()));
//        intent.putExtra("name", interviews.get(position).getUsername());
//        intent.putExtra("email", interviews.get(position).getEmail());
//        intent.putExtra("role", interviews.get(position).getRole());
//        return intent;
//    }
}