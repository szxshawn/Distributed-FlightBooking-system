package model;

import java.net.InetAddress;

public class User {
    public InetAddress ip;
    public int port;
    public User(){

    }
    public User(InetAddress ip, int port){
        this.ip = ip;
        this.port = port;
    }
    public String toString(){
        return this.ip+":"+this.port;
    }
}
