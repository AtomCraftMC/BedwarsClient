package me.deadlight.bedwarsclient.Oldway;
import me.deadlight.bedwarsclient.ProcessData;
import org.bukkit.Bukkit;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.*;
public class SocketClient {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) {


        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null && clientSocket.isConnected()) {
                try {
                    ProcessData.ProcessIncomingData(inputLine);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public void sendMessage(String msg) {
        try {
            out.println(msg);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void stopConnection() {
        try {
            clientSocket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            System.out.println(e);
        }

    }

}
