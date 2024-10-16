package utils;

import model.User;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Sender {
    InetAddress address;
    int port;
    public Sender(User user){
        this.address = user.ip;
        this.port = user.port;
    }
    public void send(String response){
        DatagramSocket serverSocket = null;
        try {
            // Create the server socket
            serverSocket = new DatagramSocket();
            serverSocket.setSoTimeout(3000);  // Optional timeout, can be adjusted as needed

            System.out.println("Sending response: " + response);

            // Prepare data to send back to the client
            byte[] sendData = response.getBytes(StandardCharsets.UTF_8);
            InetAddress clientAddress = this.address;
            int clientPort = this.port;

            // Create a packet containing the data, destination IP, and port
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);

            // Send the response to the client
            serverSocket.send(sendPacket);
            System.out.println("Response sent to client at " + this.address + ":" + this.port);

        } catch (SocketTimeoutException e) {
            System.err.println("Timeout: Unable to send response to client within the designated time.");
        } catch (UnknownHostException e) {
            System.err.println("Invalid client IP address: " + this.port);
        } catch (IOException e) {
            System.err.println("I/O error occurred: " + e.getMessage());
        } finally {
            // Ensure the socket is closed properly
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        }
    }
}
