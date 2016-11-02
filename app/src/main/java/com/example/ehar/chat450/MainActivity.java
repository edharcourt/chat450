package com.example.ehar.chat450;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    ConnectionManager conn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button connect_button = (Button) findViewById(R.id.connect);

        connect_button.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Thread client = new Thread(conn.client_runnable);
                  client.start();
              }
          }

        );

    }

    @Override
    protected void onResume() {
        super.onResume();

        conn = new ConnectionManager();
        Thread server = new Thread(conn.server_thread);
        server.start();

        Thread reader_thread = new Thread(new Reader(conn));
        reader_thread.start();

        Thread writer_thread = new Thread(new Writer(conn));
        writer_thread.start();

    }
}
