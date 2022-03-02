package acherie.http.client.ahc;

import org.asynchttpclient.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.asynchttpclient.Dsl.*;

public class AhcClientTest {

    final static Logger logger = LoggerFactory.getLogger(AhcClientTest.class);

    final static AtomicInteger count = new AtomicInteger();

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {

        DefaultAsyncHttpClientConfig.Builder config = config()
//                .setMaxConnectionsPerHost(10)
//                .setMaxConnections(1000)
                .setRequestTimeout(60000 * 60)
                .setReadTimeout(60000 * 60)
                .setConnectTimeout(60000);
//        config.setProxyServer(proxyServer("127.0.0.1", 8888));
        AsyncHttpClient asyncHttpClient = asyncHttpClient(config);

//        Request request = get("http://localhost:8080?delay=100").build();
        Request request = get("http://localhost:8888/ping").build();
        CompletableFuture<String> whenResponse = executeGet(asyncHttpClient, request, new DefaultAsyncHandler());
        whenResponse.join();
        String res = whenResponse.get();

        int loop = 10000;
        long start = System.currentTimeMillis();

        CompletableFuture[] futures = new CompletableFuture[loop];
        for (int i = 0; i < loop; i++) {
            logger.info("request: {}", i);
            CompletableFuture<String> future = executeGet(asyncHttpClient, request, new DefaultAsyncHandler());
            futures[i] = future;
        }
        CompletableFuture.allOf(futures).join();

        long end = System.currentTimeMillis();
        logger.info("Time: {}ms", end - start);
        logger.info("Count: {}", count.get());
        logger.info("Tps: {}", count.get() / ((end - start) / 1000));

        asyncHttpClient.close();
    }

    private static CompletableFuture<String> executeGet(AsyncHttpClient asyncHttpClient,
                                                        Request request,
                                                        AsyncHandler<String> asyncHandler) {
        return asyncHttpClient
                .executeRequest(request, asyncHandler)
                .toCompletableFuture();
    }

    static class DefaultAsyncHandler extends AsyncCompletionHandler<String> {
        @Override
        public String onCompleted(Response response) throws Exception {
            String responseBody = response.getResponseBody();
//            logger.info("body: {}", responseBody);
            count.incrementAndGet();
            return responseBody;
        }
    }
}
