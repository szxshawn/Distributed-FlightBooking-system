package Server;
import handler.*;
import model.User;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ThreadServer {
    private static final int PORT = 8080;
    private static final int BUFFER_SIZE = 1024;
    public void serve(){
        try {
            // 创建一个DatagramSocket，绑定到指定端口
            DatagramSocket serverSocket = new DatagramSocket(PORT);
            byte[] receiveData = new byte[BUFFER_SIZE];
            DatabaseServer databaseServer = new DatabaseServer();
            System.out.println("UDP server starts at "+PORT);

            while (true) {
                // 创建用于接收数据的DatagramPacket
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                // 接收数据
                serverSocket.receive(receivePacket);
                int length = receivePacket.getLength();
                // 从接收到的包中提取数据
                String receivedMessage =  new String(receivePacket.getData(), 0, length);
                System.out.println("\n\n\n\nserver received message: " + receivedMessage);
                // 获取客户端的IP地址和端口
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                User user = new User(clientAddress,clientPort);
                databaseServer.users.add(user);
                // 输出接收到的数据
                String[] arguments = receivedMessage.split(",");
                String header = arguments[0].split("=")[1];
                //System.out.println("request received: " + header);
                switch (header){
                    case "QueryFlightBySrcAndDes":
                        QueryFlightBySrcAndDesHandler.handle(user,databaseServer,receivedMessage,serverSocket);
                        break;
                    case "QueryFlightById":
                        QueryFlightByIdentifierHandler.handle(user,databaseServer,receivedMessage);
                        break;
                    case "MakeReservationById":
                        MakeReservationByIdHandler.handle(user,databaseServer,receivedMessage);
                        break;
                    case "SubscribeById":
                        SubscribeByIdHandler.handle(user,databaseServer,receivedMessage);
                        break;
                    case "RandomChooseSeat":
                        RandomChooseSeat.handle(user,databaseServer,receivedMessage);
                        break;
                    case "GetBookingInfo":
                        GetBookingInfoHandler.handle(user,databaseServer,receivedMessage);
                        break;
                    default:
                        DefaultHandler.handle(user);
                        break;
                }

                // 处理完消息后，可以决定是否要继续接收或进行回复
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
