package acherie.demos.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioEchoClient {
    private static SocketChannel client;
    private static ByteBuffer buffer;
    private static NioEchoClient instance;

    public static NioEchoClient start() {
        if (instance == null)
            instance = new NioEchoClient();

        return instance;
    }

    public static void stop() throws IOException {
        client.close();
        buffer = null;
    }

    private NioEchoClient() {
        try {
            client = SocketChannel.open(new InetSocketAddress("localhost", 5454));
            buffer = ByteBuffer.allocate(256);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendMessage(String msg) {
        buffer = ByteBuffer.wrap(msg.getBytes());
        String response = null;
        try {
            client.write(buffer);
            buffer.clear();
            client.read(buffer);
            response = new String(buffer.array()).trim();
            System.out.println("response=" + response);
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;

    }

    public static void main(String[] args) throws IOException {
        NioEchoClient client = start();
        String resp1 = client.sendMessage("hello");
        String resp2 = client.sendMessage("world");
        System.out.println("resp1 = " + resp1);
        System.out.println("resp2 = " + resp2);
        stop();
    }
}
