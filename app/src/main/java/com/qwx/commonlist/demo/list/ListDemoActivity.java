package com.qwx.commonlist.demo.list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.qwx.commonlist.demo.MainActivity;
import com.qwx.commonlist.demo.R;
import com.qwx.commonlist.lib.ListFragment;
import com.qwx.commonlist.lib.ListFragmentConfig;

public class ListDemoActivity extends AppCompatActivity {

    public static void start(Context context, ListFragmentConfig config) {
        Intent intent = new Intent(context, ListDemoActivity.class);
        intent.putExtra("config", config);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_demo);

        ListFragmentConfig config = (ListFragmentConfig) getIntent().getSerializableExtra("config");
        ListFragment fragment = ListFragment.newInstance(config);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, fragment).commit();
    }
}