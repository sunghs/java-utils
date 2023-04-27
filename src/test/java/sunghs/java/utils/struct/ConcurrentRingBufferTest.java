package sunghs.java.utils.struct;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConcurrentRingBufferTest {

    final ExecutorService executorService = Executors.newFixedThreadPool(100);

    final ConcurrentRingBuffer<Integer> concurrentRingBuffer = new ConcurrentRingBuffer<>(100);

    @AfterEach
    void resetRingBuffer() {
        concurrentRingBuffer.clear();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 50, 100, 5000, 10000, 100000, 1000000})
    @DisplayName("링버퍼 생성 테스트")
    void createConcurrentRingBufferTest(int capacity) {
        new ConcurrentRingBuffer<>(capacity);
    }

    @Test
    @DisplayName("링버퍼 capacity 100, data insert 1000, 실제로 99개 이후 들어가지 않는 테스트")
    void pushMillionDataTest() {
        Assertions.assertEquals(100, concurrentRingBuffer.getCapacity());

        for (int i = 0; i < 150; i++) {
            concurrentRingBuffer.push(new SecureRandom().nextInt(9999));
        }
    }

    @Test
    @DisplayName("데이터 넣고 빼기 테스트")
    void pushAndPopTest() {
        final int insertData = 100;
        concurrentRingBuffer.push(insertData);
        int result = concurrentRingBuffer.pop();
        Assertions.assertEquals(insertData, result);
    }

    @Test
    @DisplayName("데이터 여러개 넣고 빼기 테스트")
    void multiPushAndPopTest() {
        int[] array = {1, 10, 5, 100, -25, 7440, -2559, Integer.MAX_VALUE, -5005, -99999, 99999};
        for (int data : array) {
            concurrentRingBuffer.push(data);
        }
        for (int data : array) {
            Assertions.assertEquals(data, concurrentRingBuffer.pop());
        }

        Assertions.assertNull(concurrentRingBuffer.pop());
        Assertions.assertThrows(NullPointerException.class, () -> {
            int data = concurrentRingBuffer.pop();
            log.info("expected primitive type {} null", data);
        });
    }
}
