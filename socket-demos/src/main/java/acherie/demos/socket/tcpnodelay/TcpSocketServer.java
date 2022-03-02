package acherie.demos.socket.tcpnodelay;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class TcpSocketServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(10086));//wildcard ip
            Socket socket = serverSocket.accept();
            System.out.println("Accept New Socket");
            System.out.println("Tcp No Delay : " + socket.getTcpNoDelay());
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            int result;
            while((result = is.read()) != -1) {
                System.out.println((char)result);
            }
            TimeUnit.MINUTES.sleep(1);
        }
    }
}
