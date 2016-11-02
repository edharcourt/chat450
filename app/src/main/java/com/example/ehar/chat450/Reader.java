package com.example.ehar.chat450;

import android.util.Log;

import java.io.IOException;

/**
 * Created by ehar on 10/27/2016.
 */

public class Reader implements Runnable {

    // TODO need a Handler connected to the UI thread
    // TODO or some way to communicate back to activity.

    private String LOG_TAG = Reader.class.getName();
    private ConnectionManager conn = null;

    public Reader(ConnectionManager conn) {
        this.conn = conn;
    }

    @Override
    public void run() {

        // Wait for a connection
        while (!conn.connected) {
            synchronized (conn) {
                try {
                    conn.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        Log.i(LOG_TAG, "Reader is awake");

        // TODO figure out how to properly quit
        while (true) {
            String line = null;
            try {
                line = conn.from.readLine();
            } catch (IOException e) {
                Log.e(LOG_TAG, e.toString());
            }
            Log.i(LOG_TAG, line);
        }

    }
}
