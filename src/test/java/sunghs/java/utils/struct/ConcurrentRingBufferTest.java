package sunghs.java.utils.struct;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConcurrentRingBufferTest {

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
    @DisplayName("링버퍼 capacity 100, data insert 1000 테스트")
    void pushMillionDataTest() {
        int capacity = concurrentRingBuffer.getCapacity();
        Assertions.assertEquals(100, capacity);

        for (int i = 0; i < 150; i++) {
            concurrentRingBuffer.push(i);
        }

        Assertions.assertEquals(concurrentRingBuffer.get(0), concurrentRingBuffer.get(capacity));
        Assertions.assertEquals(concurrentRingBuffer.get(10), concurrentRingBuffer.get(capacity + 10));
        Assertions.assertEquals(concurrentRingBuffer.get(50), concurrentRingBuffer.get(capacity + 50));
        Assertions.assertEquals(concurrentRingBuffer.get(101), concurrentRingBuffer.get(1));
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

    @ParameterizedTest
    @ValueSource(ints = {1, 100, 1000, 10000})
    @DisplayName("꽉 찬 경우 데이터 테스트, capacity 까지는 성공, overflow 수만큼 실패일 것이다")
    void isPullTest(int overflow) {
        int successCount = 0;
        int failCount = 0;

        for (int i = 0; i < concurrentRingBuffer.getCapacity() + overflow; i++) {
            if (concurrentRingBuffer.push(i)) {
                successCount++;
            } else {
                failCount++;
            }
        }

        Assertions.assertEquals(concurrentRingBuffer.getCapacity(), successCount);
        Assertions.assertEquals(overflow, failCount);
    }

    // TODO concurrent 테스트 구상
    @Test
    @DisplayName("concurrent 테스트 push/pop 테스트")
    void concurrentTest() {
        ExecutorService producer = Executors.newFixedThreadPool(5);
        ExecutorService consumer = Executors.newFixedThreadPool(5);

        AtomicInteger count = new AtomicInteger(0);
        Random random = new SecureRandom();
    }
}
