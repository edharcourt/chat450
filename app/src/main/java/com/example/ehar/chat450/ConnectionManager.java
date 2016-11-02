package com.example.ehar.chat450;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

/**
 * Created by ehar on 10/18/2016.
 */

public class ConnectionManager {

    protected String ip_address = null;
    protected ServerSocket server_sock = null;
    protected Socket client_sock = null;

    public static int PORT = 12345;
    private static String LOG_TAG = ConnectionManager.class.getName();

    public ConnectionManager() {

    }

    BufferedReader from = null;
    PrintWriter to = null;
    boolean connected = false;


    Runnable server_thread = new Runnable() {
        @Override
        public void run() {

            try {
                server_sock = new ServerSocket(PORT);

                // sit and wait for a "call"
                client_sock = server_sock.accept();

                Log.i(LOG_TAG, "Yay, someone called me: " +
                        client_sock.getInetAddress());

                // TODO report back to user that there is now a connection

                // set up the input and output stream readers

                from = new BufferedReader(
                        new InputStreamReader(
                                client_sock.getInputStream()));

                to = new PrintWriter(client_sock.getOutputStream(), true);

            }
            catch (SocketException e) {
                // might not be an error because client
                // may have purposefully closed the server socket.
                Log.i(LOG_TAG, e.toString());
                return;
            }
            catch (IOException e) {
                Log.e(LOG_TAG, e.toString());
                return;
                // report the error back to the user somehow
            }


            // We have a connection!
            Log.i(LOG_TAG, "We have avalid connection");
            connected = true;

            synchronized (ConnectionManager.this) {
                ConnectionManager.this.notifyAll();
            }
        }
    };

    // Client Runnable
    Runnable client_runnable = new Runnable() {
        @Override
        public void run() {
            try {
                client_sock = new Socket("10.60.28.140", PORT);
                //client_sock = new Socket("10.70.23.102", PORT);
                from = new BufferedReader(
                        new InputStreamReader(client_sock.getInputStream()));

                to = new PrintWriter(client_sock.getOutputStream(), true);

                // stop the server thread because we have a connection
                server_sock.close();

            } catch (IOException e) {
                Log.e(LOG_TAG, e.toString());
                return;
                // TODO report back to the user that connection failed.
            }

            Log.i(LOG_TAG, "We have a valid connection with: " +
                client_sock.getInetAddress());
            connected = true;

            synchronized (ConnectionManager.this) {
                ConnectionManager.this.notifyAll();
            }
        }
    };
}
