package com.github.reneweb.androidasyncsocketexamples;

import android.view.View;
import android.widget.TextView;

public class CardViewHolder extends BaseViewHolder{

    private TextView textView;
    public CardViewHolder(View itemView) {
        super(itemView);

        textView = (TextView) itemView.findViewById(R.id.textMessage);
    }

    public void setText(String text) {
        textView.setText(text);
    }
}
