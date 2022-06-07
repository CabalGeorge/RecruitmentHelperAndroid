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
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.recruitmenthelper.R;
import com.example.recruitmenthelper.model.User;
import com.example.recruitmenthelper.popups.EditUserPopUp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> implements Filterable {

    LayoutInflater inflater;
    CardView cardView;
    List<User> users;
    List<User> usersListFull;
    SessionManager sessionManager;

    public UserAdapter(Context context, List<User> users) {
        this.inflater = LayoutInflater.from(context);
        this.users = users;
        usersListFull = new ArrayList<>(users);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.custom_user_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.userName.setText("Name: " + users.get(position).getUsername());
        holder.userEmail.setText("Email: " + users.get(position).getEmail());
        holder.userRole.setText("Role: " + users.get(position).getRole());
        sessionManager = new SessionManager(holder.itemView.getContext());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView userName, userEmail, userRole;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.txt_userName);
            userEmail = itemView.findViewById(R.id.txt_userEmail);
            userRole = itemView.findViewById(R.id.txt_userRole);
            cardView = itemView.findViewById(R.id.usersCardView);

            cardView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if (sessionManager.getSessionRole().equals("ADMIN")) {
                menu.setHeaderTitle("Select an action");
                menu.add(this.getAdapterPosition(), 0, 0, "Edit this user");
                menu.add(this.getAdapterPosition(), 1, 1, "Delete this user");
            }
        }
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<User> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(usersListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase(Locale.ROOT).trim();

                for (User user : usersListFull) {
                    if (user.getUsername().toLowerCase(Locale.ROOT).contains(filterPattern)) {
                        filteredList.add(user);
                    } else if (user.getRole().toLowerCase(Locale.ROOT).contains(filterPattern)) {
                        filteredList.add(user);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            users.clear();
            users.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public void deleteUser(int position) {
        RequestQueue requestQueue = Volley.newRequestQueue(cardView.getContext());

        String urlDelete = Constant.DELETE_USER_URL + "/" + users.get(position).getUser_id();
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, urlDelete, response -> {
            Toast.makeText(cardView.getContext(), "User successfully deleted!", Toast.LENGTH_LONG).show();
        }, error -> Toast.makeText(cardView.getContext(), "error" + error.toString(), Toast.LENGTH_LONG).show()) {
        };

        requestQueue.add(stringRequest);
        users.remove(position);
        notifyItemRemoved(position);
    }


    public Intent transferData(int position) {
        Intent intent = new Intent(cardView.getContext(), EditUserPopUp.class);
        intent.putExtra("id", String.valueOf(users.get(position).getUser_id()));
        intent.putExtra("name", users.get(position).getUsername());
        intent.putExtra("email", users.get(position).getEmail());
        intent.putExtra("role", users.get(position).getRole());
        return intent;
    }
}
