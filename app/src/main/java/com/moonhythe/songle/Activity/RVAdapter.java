package com.moonhythe.songle.Activity;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moonhythe.songle.R;
import com.moonhythe.songle.Structure.Badge;

import java.util.List;

/**
 * Created by kris on 15/11/17.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.BadgeViewHolder> {

    public static class BadgeViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView song_name;
        TextView total_time;
        TextView badge_text;
        ImageView badge_img;

        BadgeViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            song_name = (TextView)itemView.findViewById(R.id.song_name);
            badge_text = (TextView)itemView.findViewById(R.id.badge_text);
            total_time = (TextView)itemView.findViewById(R.id.total_time);
            badge_img = (ImageView)itemView.findViewById(R.id.badge_img);
        }
    }

    List<Badge> badges;

    RVAdapter(List<Badge> badges){
        this.badges = badges;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public BadgeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.badge, viewGroup, false);
        BadgeViewHolder bvh = new BadgeViewHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(BadgeViewHolder badgeViewHolder, int i) {
        badgeViewHolder.song_name.setText(badges.get(i).getArtist_title());
        badgeViewHolder.badge_text.setText(badges.get(i).getBadge_text());
        badgeViewHolder.total_time.setText(badges.get(i).getTotal_time());
        switch(badges.get(i).getBadge()){
            case "Gold":
                badgeViewHolder.badge_img.setImageResource(R.drawable.gold);
                break;
            case "Silver":
                badgeViewHolder.badge_img.setImageResource(R.drawable.silver);
                break;
            case "Bronze":
                badgeViewHolder.badge_img.setImageResource(R.drawable.bronze);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return badges.size();
    }
}