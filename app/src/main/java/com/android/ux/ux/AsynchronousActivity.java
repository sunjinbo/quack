package com.android.ux.ux;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.ux.ux.asynchronous.FutureActivity;
import com.android.ux.ux.asynchronous.HandlerThreadActivity;
import com.android.ux.ux.asynchronous.LooperActivity;

public class AsynchronousActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asynchronous);
    }

    public void onLooperClick(View view) {
        startActivity(new Intent(this, LooperActivity.class));
    }

    public void onFutureClick(View view) {
        startActivity(new Intent(this, FutureActivity.class));
    }

    public void onHandlerThreadClick(View view) {
        startActivity(new Intent(this, HandlerThreadActivity.class));
    }
}
