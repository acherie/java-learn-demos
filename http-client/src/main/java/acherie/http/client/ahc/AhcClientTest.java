package acherie.http.client.ahc;

import org.asynchttpclient.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.asynchttpclient.Dsl.*;

public class AhcClientTest {

    final static Logger logger = LoggerFactory.getLogger(AhcClientTest.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {

        DefaultAsyncHttpClientConfig.Builder config = config();
//        config.setProxyServer(proxyServer("127.0.0.1", 8888));
        AsyncHttpClient asyncHttpClient = asyncHttpClient(config);

        CompletableFuture<String> whenResponse = executeGet(asyncHttpClient);
        whenResponse.join(); // wait for completion
        String res = whenResponse.get();

        long start = System.currentTimeMillis();
        whenResponse = executeGet(asyncHttpClient);
        whenResponse.join(); // wait for completion
        whenResponse.get();
        logger.info("res: {}", res);

        long end = System.currentTimeMillis();
        logger.info("Time: {}ms", end - start);

        asyncHttpClient.close();
    }

    private static CompletableFuture<String> executeGet(AsyncHttpClient asyncHttpClient) {
        CompletableFuture<String> whenResponse = asyncHttpClient
                .prepareGet("http://localhost:8080?delay=100")
                .execute(new AsyncCompletionHandler<String>() {
                    @Override
                    public State onStatusReceived(HttpResponseStatus status) throws Exception {
                        logger.info("onStatusReceived: {}", status.getStatusText());
                        return super.onStatusReceived(status);
                    }

                    @Override
                    public String onCompleted(Response response) throws Exception {

                        String responseBody = response.getResponseBody();
                        logger.info("response body: {}", responseBody);
                        return responseBody;
                    }
                })
                .toCompletableFuture();
        return whenResponse;
    }
}
