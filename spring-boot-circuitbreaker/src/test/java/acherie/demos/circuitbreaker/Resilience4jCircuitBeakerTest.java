package acherie.demos.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.junit.jupiter.api.Test;

import java.time.Duration;

public class Resilience4jCircuitBeakerTest {

    @Test
    public void testCircuitBeaker() {

        // Create a custom configuration for a CircuitBreaker
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                // 失败比例 默认50%
                .failureRateThreshold(50)
                // 慢调用比例 默认100%
                .slowCallRateThreshold(100)
                // 超过多少秒的请求为慢调用
                .slowCallDurationThreshold(Duration.ofSeconds(3))
                // open状态保持时间 三分钟，默认60秒，之后转为half open
                .waitDurationInOpenState(Duration.ofSeconds(60 * 3))
                // 是否自动从open转换为half-open当没有调用发生时
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                // half open状态下允许通过的请求个数
                .permittedNumberOfCallsInHalfOpenState(1)
                // 状态转换的最小计算量基数，少于该数量则不会进行状态转换
                .minimumNumberOfCalls(1)
                // 计算规则，基于失败次数
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                // 每个滑动窗口的容量 次或秒
                .slidingWindowSize(2)
                // 哪些异常统计为失败
                .recordExceptions(RuntimeException.class)
                .build();

        CircuitBreaker customCircuitBreaker = CircuitBreaker
                .of("testName", circuitBreakerConfig);
    }
}
