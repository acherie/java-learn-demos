package acherie.http.client.okhttp3;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class OkhttpAsyncClientTest {

    public static void main(String[] args) throws IOException, InterruptedException {

        CountDownLatch doneLatch = new CountDownLatch(1);

        OkHttpClient client = buildClient();

        Request request = new Request.Builder()
                .url("http://localhost:8080?delay=200")
                .build();

        long start = System.currentTimeMillis();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                doneLatch.countDown();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    System.out.println(responseBody.string());
                    doneLatch.countDown();
                }
            }
        });

        doneLatch.await();

        long end = System.currentTimeMillis();
        System.out.println("Time: " + (end - start) + " ms");
    }

    static OkHttpClient buildClient() {

        return new OkHttpClient();
    }
}
