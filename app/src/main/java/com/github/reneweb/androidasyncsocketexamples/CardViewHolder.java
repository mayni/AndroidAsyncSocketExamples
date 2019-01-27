package com.github.reneweb.androidasyncsocketexamples;

import android.view.View;
import android.widget.TextView;
public class CardViewHolder extends BaseViewHolder{

    private TextView textView,textView1,textView2,textView3;
    public CardViewHolder(View itemView) {
        super(itemView);

        textView =   itemView.findViewById(R.id.textMessage);
        textView1 = itemView.findViewById(R.id.timeMessage);
        textView2 = itemView.findViewById(R.id.textMessage1);
        textView3 = itemView.findViewById(R.id.timeMessage1);

    }

    public void setText(String text) {
        textView.setText(text);

    }
    public void setText1(String text) {
        textView1.setText(text);

    }
    public void setText2(String text) {
        textView2.setText(text);

    }
    public void setText3(String text) {
        textView3.setText(text);

    }



}
