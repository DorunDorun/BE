//package shop.dodotalk.dorundorun.hyunjun.redis;
//
//
//import org.springframework.data.redis.core.RedisKeyValueAdapter;
//import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.GenericContainer;
//
//
//
//
//public abstract class AbstractContainerBaseTest {
//    static final String REDIS_IMAGE = "redis:6-alpine";
//    static final GenericContainer REDIS_CONTAINER;
//
//    static {
//        REDIS_CONTAINER = new GenericContainer<>(REDIS_IMAGE)
//                .withExposedPorts(6379)
//                .withReuse(true);
//        REDIS_CONTAINER.start();
//    }
//
//    @DynamicPropertySource
//    public static void overrideProps(DynamicPropertyRegistry registry){
//        registry.add("spring.ec2Redis.host", REDIS_CONTAINER::getHost);
//        registry.add("spring.ec2Redis.port", () -> ""+REDIS_CONTAINER.getMappedPort(6379));
//    }
//}