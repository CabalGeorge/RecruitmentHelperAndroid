package com.example.unitbvevents.config;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unitbvevents.R;
import com.example.unitbvevents.model.Event;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    LayoutInflater inflater;
    List<Event> events;

    public Adapter(Context context, List<Event> events) {
        this.inflater = LayoutInflater.from(context);
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.custom_event_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.eventName.setText("Name: "+events.get(position).getName());
        holder.eventLocation.setText("Location: "+events.get(position).getLocation());
        holder.eventTime.setText("Date and time: "+events.get(position).getDateTime());

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView eventName, eventLocation, eventTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.txt_eventName);
            eventLocation = itemView.findViewById(R.id.txt_eventLocation);
            eventTime = itemView.findViewById(R.id.txt_eventTime);
        }

    }
}
