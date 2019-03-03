package io.github.dsckiet.dsc_event_attendance;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AttendeesAdapter extends RecyclerView.Adapter<AttendeesAdapter.view_holder> {
    Context context;
    ArrayList<String> nameList, emailList, attendeIdList;

    public AttendeesAdapter(Context applicationContext, ArrayList<String> nameList, ArrayList<String> emailList, ArrayList<String> attendeIdList) {
        this.context = applicationContext;
        this.nameList = nameList;
        this.emailList = emailList;
        this.attendeIdList = attendeIdList;
    }

    @Override
    public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_attendee, parent, false);
        return new view_holder(view);
    }

    @Override
    public void onBindViewHolder(final AttendeesAdapter.view_holder holder, final int position) {
        holder.nametv.setText(nameList.get(position));
        holder.emailtv.setText(emailList.get(position));
        holder.attendIdtv.setText(attendeIdList.get(position));
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    public class view_holder extends RecyclerView.ViewHolder {

        TextView nametv, emailtv, attendIdtv;

        public view_holder(final View itemView) {
            super(itemView);

            nametv = itemView.findViewById(R.id.attendeeName);
            emailtv = itemView.findViewById(R.id.attendeeEmail);
            attendIdtv = itemView.findViewById(R.id.attendeeID);
        }
    }
}