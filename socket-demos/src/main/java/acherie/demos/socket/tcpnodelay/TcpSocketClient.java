package acherie.demos.socket.tcpnodelay;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class TcpSocketClient {
    public static void main(String[] args) {
        try(Socket socket = new Socket()) {
//            socket.setTcpNoDelay(true);
            socket.connect(new InetSocketAddress("159.75.232.102", 10086));
            System.out.println("Tcp No Delay : " + socket.getTcpNoDelay());
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            String s = "TCP_NO_DELAY_HELLO_WROLD_OutputStream";
            for (byte c : s.getBytes()) {
                TimeUnit.MILLISECONDS.sleep(5);
                os.write(c);
                os.flush();
            }
            TimeUnit.MINUTES.sleep(1);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
