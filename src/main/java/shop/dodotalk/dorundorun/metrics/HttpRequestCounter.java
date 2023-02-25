package shop.dodotalk.dorundorun.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HttpRequestCounter {
    private final Counter httpRequestsTotal;

    @Autowired
    public HttpRequestCounter(MeterRegistry meterRegistry) {
        System.out.println("Request 요청마다 실행되나요???");
        httpRequestsTotal = Counter.builder("http.requests.total")
                .description("Total HTTP requests")
                .register(meterRegistry);
    }

    public void increment() {
        httpRequestsTotal.increment();
    }
}
