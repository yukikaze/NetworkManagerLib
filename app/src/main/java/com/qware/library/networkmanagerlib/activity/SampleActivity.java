package com.qware.library.networkmanagerlib.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.pwittchen.reactivenetwork.library.Connectivity;
import com.qware.library.networkmanagerlib.NetworkManager;
import com.qware.library.networkmanagerlib.R;

import rx.functions.Action1;

public class SampleActivity extends AppCompatActivity {

    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        message = (TextView) findViewById(R.id.messageTextView);

        NetworkManager.getInstance(this).subscribe();
        NetworkManager.getInstance(this).subscribeNetworkConnectedListener(new Action1<Connectivity>() {
            @Override
            public void call(Connectivity connectivity) {
                message.setText("Network Connected!");
            }
        });
        NetworkManager.getInstance(this).subscribeNetworkDisconnectedListener(new Action1<Connectivity>() {
            @Override
            public void call(Connectivity connectivity) {
                message.setText("Network Disconnected!");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkManager.getInstance(this).unsubscribe();
    }
}
