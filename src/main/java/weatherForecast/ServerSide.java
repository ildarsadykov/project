package weatherForecast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;



public class ServerSide {
    public static void main(String[] args) {
        try (ServerSocket serverSocket=new ServerSocket(1234)) {
            while (true) {
                Socket socket = serverSocket.accept();
                try{
                System.out.println(socket.getInetAddress().getHostAddress() + " success to connect");
                String header = "Please, enter the city:";
                try (OutputStream out = socket.getOutputStream();
                    InputStream in = socket.getInputStream()) {
                    out.write((header).getBytes());
                    byte[] buf = new byte[1024];
                    int l = 0;
                    String str = "";
                    while ((l = in.read(buf)) != -1) {
                        str = (new String(buf, 0, l));
                        try {
                            out.write(Util.showInfo(str).getBytes());
                        } catch (NullPointerException e) {
                            out.write(("Not found, try again").getBytes());
                        }
                        out.write(("\n" + "Please, enter the city: ").getBytes());
                    }
                }
            }
                catch (SocketException  e) {
                    System.out.println(socket.getInetAddress().getHostAddress()+" was disconnected");
                }
            }

        }
         catch (IOException e) {
          e.printStackTrace();
        }
    }

}
