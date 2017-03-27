package com.jordancuker.thevoid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;


public class VoidAdapter extends RecyclerView.Adapter<VoidAdapter.MyViewHolder> {

    private List<Void> data = Collections.emptyList();
    private LayoutInflater inflater;

    public VoidAdapter(Context context, List<Void> data){
        inflater=LayoutInflater.from(context);
        this.data=data;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_view_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Void current = data.get(position);
        holder.content.setText(current.getText());
        long millisLeft = current.getRemainingTime();
        long secondsLeft = millisLeft / 1000;
        long minutesLeft = secondsLeft / 60;
        long hoursLeft = minutesLeft / 60;
        if(minutesLeft > 60)  holder.remainingTime.setText(String.valueOf(hoursLeft));
        else  holder.remainingTime.setText(String.valueOf(minutesLeft));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView content;
        TextView remainingTime;

        public MyViewHolder(View itemView){
            super(itemView);
            content=(TextView) itemView.findViewById(R.id.void_content);
            remainingTime=(TextView) itemView.findViewById(R.id.remaining_time);
        }
    }
}
