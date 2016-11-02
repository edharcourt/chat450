package com.example.ehar.chat450;

/**
 * Created by ehar on 10/27/2016.
 */

public class Writer implements Runnable {

    ConnectionManager conn = null;

    public Writer(ConnectionManager conn) {
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

        int i = 0;
        while (true) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            conn.to.println("Msg: " + i++);
        }


    }
}
