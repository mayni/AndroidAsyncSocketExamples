package com.github.reneweb.androidasyncsocketexamples.call;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class TextChange implements TextWatcher {

    public  TextChange(EditText editText) {
       editText.addTextChangedListener(this);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        System.out.println(s);
    }
}
