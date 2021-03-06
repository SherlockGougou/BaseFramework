package com.kongzue.baseframeworkdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.util.JumpParameter;

@Layout(R.layout.activity_transition)
public class TransitionActivity extends BaseActivity {
    
    private Button btnTransition;
    
    @Override
    public void initViews() {
        btnTransition = findViewById(R.id.btn_transition);
    }
    
    @Override
    public void initDatas(JumpParameter paramer) {
    
    }
    
    @Override
    public void setEvents() {
        btnTransition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
