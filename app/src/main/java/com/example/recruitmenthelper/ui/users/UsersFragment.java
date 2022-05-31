package com.example.recruitmenthelper.ui.users;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.recruitmenthelper.config.UserAdapter;
import com.example.recruitmenthelper.config.Constant;
import com.example.recruitmenthelper.config.SessionManager;
import com.example.recruitmenthelper.R;
import com.example.recruitmenthelper.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {

    private UsersViewModel mViewModel;
    RecyclerView recyclerView;
    List<User> usersList;
    UserAdapter userAdapter;
    SessionManager sessionManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_users, container, false);
        getActivity().setTitle("Users");


        usersList = new ArrayList<>();
        sessionManager = new SessionManager(getActivity().getApplicationContext());


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = Constant.GET_ALL_USERS_URL;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {

            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject userObject = response.getJSONObject(i);

                    User user = new User();
                    user.setUser_id(userObject.getInt("userId"));
                    user.setUsername(userObject.getString("name"));
                    user.setEmail(userObject.getString("email"));
                    user.setRole(userObject.getString("role"));
                    usersList.add(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            userAdapter = new UserAdapter(getActivity().getApplicationContext(), usersList);
            recyclerView = root.findViewById(R.id.usersList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            recyclerView.setAdapter(userAdapter);

        }, error -> Log.d("tag", "onErrorResponse" + error.getMessage())) {
        };
        requestQueue.add(jsonArrayRequest);
        return root;

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Intent intent = userAdapter.transferData(item.getGroupId());
                startActivity(intent);
                break;
            case 1:
                userAdapter.deleteUser(item.getGroupId());
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return super.onContextItemSelected(item);
    }
}