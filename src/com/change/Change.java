package com.change;
import android.content.Context;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;
import android.os.ServiceManager;
import android.os.IBinder;
import android.hardware.change.IChange;

public class Change extends Activity {
    private static final String LOG_TAG = "CustomChangeActivity";
    private static final String CHANGE_AIDL_INTERFACE = "android.hardware.change.IChange/default";
    private static IChange changeAidl; // AIDL Java

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = findViewById(R.id.editText);
                String inputText = editText.getText().toString();
                Log.d(LOG_TAG, "CustomChangeActivity: Input Text= " + inputText);
                String response = "";
                if (changeAidl != null) {
                    try {
                        changeAidl.putChars(inputText);
                    } catch (android.os.RemoteException e) {
                        Log.e(LOG_TAG, "IChange-AIDL error", e);
                    }
                }
                if (changeAidl != null) {
                    try {
                        response = changeAidl.getChars();
                    } catch (android.os.RemoteException e) {
                        Log.e(LOG_TAG, "IChange-AIDL error", e);
                    }
                }
                Log.d(LOG_TAG, "CustomChangeActivity: Response= " + response);
                TextView textView = findViewById(R.id.textView);
                textView.setText(response);
            }
        });

        IBinder binder = ServiceManager.getService(CHANGE_AIDL_INTERFACE);
        if (binder == null) {
            Log.e(LOG_TAG, "CustomChangeActivity: Getting " + CHANGE_AIDL_INTERFACE + " service daemon binder failed!");
        } else {
            changeAidl = IChange.Stub.asInterface(binder);
            if (changeAidl == null) {
                Log.e(LOG_TAG, "CustomChangeActivity: Getting IChange AIDL daemon interface failed!");
            } else {
                Log.d(LOG_TAG, "CustomChangeActivity: IChange AIDL daemon interface is binded!");
            }
        }
    }
}
