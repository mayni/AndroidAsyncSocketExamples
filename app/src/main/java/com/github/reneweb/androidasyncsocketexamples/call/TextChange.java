package com.github.reneweb.androidasyncsocketexamples.call;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.github.reneweb.androidasyncsocketexamples.TestFragment;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_APPEND;

public class TextChange implements TextWatcher {
    String val,type;
    TestFragment testFragment;
    public  TextChange(EditText editText, TestFragment testFragment,String type) {
        this.val = editText.getText().toString();
        this.testFragment = testFragment;
        this.type=type;
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
        System.out.println(s.toString());
        this.val = s.toString();
        testFragment.passdata(s.toString(),type);

    }




}
