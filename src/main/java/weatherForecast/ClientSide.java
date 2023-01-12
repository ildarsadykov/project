package weatherForecast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Scanner;

public class ClientSide{

    public static void main(String[] args) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 1234));
            new Thread(new Chat(socket)).start();
            InputStream s = socket.getInputStream();
            byte[] buf = new byte[1024];
            try {
                int len = 0;
                while ((len = s.read(buf)) != -1) {
                    System.out.println(new String(buf, 0, len));
                }
            }
            catch (SocketException e) {
                System.out.println("Please, repeat connecting");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class Chat implements  Runnable{
    Socket socket;
    public Chat(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        Scanner scanner=new Scanner(System.in);
        try (OutputStream out = socket.getOutputStream()){
            String in="";
            while (!in.equals("exit")){
                in = scanner.next();
                out.write((in).getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}