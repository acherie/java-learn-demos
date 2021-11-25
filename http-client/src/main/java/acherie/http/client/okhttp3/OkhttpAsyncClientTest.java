package acherie.http.client.okhttp3;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class OkhttpAsyncClientTest {

    final static Logger logger = LoggerFactory.getLogger(OkhttpAsyncClientTest.class);

    final static AtomicInteger count = new AtomicInteger();

    public static void main(String[] args) throws IOException, InterruptedException {

        int loop = 10000;

        OkHttpClient client = buildClient();

        Request request = new Request.Builder()
                .url("http://localhost:8080?delay=100")
                .build();

        client.newCall(request).enqueue(new FutureCallback());

        long start = System.currentTimeMillis();

        CompletableFuture[] futures = new CompletableFuture[loop];
        for (int i = 0; i < loop; i++) {
            FutureCallback callback = new FutureCallback();
            client.newCall(request).enqueue(callback);
            futures[i] = callback.future;
        }

        CompletableFuture.allOf(futures).join();

        long end = System.currentTimeMillis();
        logger.info("Time: {}ms", end - start);
        logger.info("Count: {}", count.get());
        logger.info("Tps: {}", count.get() / ((end - start) / 1000));

        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
    }

    static OkHttpClient buildClient() {

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequestsPerHost(Integer.MAX_VALUE);

        return new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .build();
    }

    static class FutureCallback implements Callback {

        CompletableFuture<String> future = new CompletableFuture<>();

        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
            future.completeExceptionally(e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try (ResponseBody responseBody = response.body()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                String body = responseBody.string();
                count.incrementAndGet();
                future.complete(body);
            }
        }
    }
}
