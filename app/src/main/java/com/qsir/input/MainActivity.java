package com.qsir.input;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.qsir.input.views.InputView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View nRootView = findViewById(R.id.root_View);
        InputView mInputView = (InputView)findViewById(R.id.input_view);
        View mBackView = findViewById(R.id.back_view);
        mInputView.setRootView(nRootView);
        mInputView.setBackView(mBackView);
    }
}
