package com.github.reneweb.androidasyncsocketexamples;

import android.view.View;
import android.widget.TextView;

public class CardViewHolder extends BaseViewHolder{

    private TextView textView,textView1;
    public CardViewHolder(View itemView) {
        super(itemView);

        textView =   itemView.findViewById(R.id.textMessage);
        textView1 = itemView.findViewById(R.id.timeMessage);

    }

    public void setText(String text ,String text1) {
        textView.setText(text);
        textView1.setText(text1);
    }


}
