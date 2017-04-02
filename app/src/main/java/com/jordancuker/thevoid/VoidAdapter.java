package com.jordancuker.thevoid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class VoidAdapter extends RecyclerView.Adapter<VoidAdapter.MyViewHolder> {

    private ArrayList<Void> data;
    private static ClickListener clickListener;
    private LayoutInflater inflater;

    public VoidAdapter(Context context, ArrayList<Void> data){
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
        long millisLeft = current.getRemainingTime(0);
        int seconds = (int) (millisLeft / 1000) ;
        int minutes = (int) ((millisLeft / (1000*60)));
        int hours   = (int) ((millisLeft / (1000*60*60)));
        if(current.isLocked()){
            if(hours != 0)  holder.remainingTime.setText(String.valueOf(current.getRemainingTime(1)) + "h");

            holder.lock.setImageResource(R.drawable.lock);
        }
        else{
            holder.remainingTime.setText(" ");
            holder.lock.setImageResource(R.drawable.lock_three);
        }
        switch (data.get(position).getMood()){
            case 0:
                holder.profile.setImageResource(R.drawable.smile);
                break;
            case 1:
                holder.profile.setImageResource(R.drawable.neutral);
                break;
            case 2:
                holder.profile.setImageResource(R.drawable.crying);
                break;
            case 3:
                holder.profile.setImageResource(R.drawable.angry);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        TextView content;
        TextView remainingTime;
        ImageView lock,profile;
        public MyViewHolder(View itemView){
            super(itemView);
            content=(TextView) itemView.findViewById(R.id.void_content);
            remainingTime=(TextView) itemView.findViewById(R.id.remaining_time);
            lock = (ImageView) itemView.findViewById(R.id.lock);
            profile = (ImageView) itemView.findViewById(R.id.profile_image);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }


        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }


    }
    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}




//http://stackoverflow.com/questions/24471109/recyclerview-onclick