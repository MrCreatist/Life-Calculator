package com.creatist.abhi.lifecalculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Abhi on 12/6/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<CardLayout> cardLayoutList;
    private Context context;

    public MyAdapter(List<CardLayout> cardLayoutList, Context context) {
        this.cardLayoutList = cardLayoutList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final CardLayout cardLayoutObject = cardLayoutList.get(position);

        holder.userName.setText(cardLayoutObject.getUserNameString());
        holder.dayValue.setText(cardLayoutObject.getDayValueString());
        holder.monthValue.setText(cardLayoutObject.getMonthValueString());
        holder.horoscopeSign.setText(cardLayoutObject.getHoroscopeSign());
        holder.imageHoroscope.setImageResource(cardLayoutObject.getHoroscopeImage());
    }

    @Override
    public int getItemCount() {
        return cardLayoutList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView userName, dayValue, monthValue, horoscopeSign;
        public CardView cardView;
        public ImageView imageHoroscope;

        public ViewHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            userName = itemView.findViewById(R.id.userName);
            dayValue = itemView.findViewById(R.id.dayValue);
            monthValue = itemView.findViewById(R.id.monthValue);
            horoscopeSign = itemView.findViewById(R.id.horoscopeSign);
            imageHoroscope = itemView.findViewById(R.id.imageHoroscope);
            imageHoroscope.setScaleType(ImageView.ScaleType.CENTER_CROP);
            cardView = itemView.findViewById(R.id.cardView);
        }

        @Override
        public void onClick(View v) {
            int cardPosition = getAdapterPosition();
            Bundle bundle = new Bundle();
            bundle.putInt("ID", cardPosition);
            Intent myIntent = new Intent(context.getApplicationContext(), Insight.class);
            myIntent.putExtras(bundle);
            context.startActivity(myIntent);
        }
    }
}
