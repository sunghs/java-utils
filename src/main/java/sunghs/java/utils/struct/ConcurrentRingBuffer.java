package sunghs.java.utils.struct;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * thread-safe 한 RingBuffer 입니다.
 * <p>
 * 환형 큐 형태이며, empty 상태의 pop, full 상태의 push 에서 에러가 발생하지 않습니다.
 * RingBuffer 생성 시 크기가 고정됩니다.
 *
 * @author https://sunghs.tistory.com
 * @see <a href="https://github.com/sunghs/java-utils">source</a>
 */
@Slf4j
public class ConcurrentRingBuffer<T> {

    private final T[] concurrentBuffer;

    private final AtomicInteger head;

    private final AtomicInteger tail;

    private final ReentrantLock reentrantLock;

    @SuppressWarnings("unchecked")
    public ConcurrentRingBuffer(final int capacity) {
        concurrentBuffer = (T[]) new Object[capacity];
        head = new AtomicInteger(0);
        tail = new AtomicInteger(0);
        reentrantLock = new ReentrantLock();
    }

    /**
     * 데이터를 넣습니다. capacity 만큼 꽉 찬 경우 에러나지 않습니다.
     * @param data 데이터
     * @return 데이터 삽입 시 true, 꽉 찬 경우 false
     */
    public boolean push(final T data) {
        reentrantLock.lock();
        try {
            int current = tail.get();
            if (concurrentBuffer[current] == null) {
                concurrentBuffer[current] = data;
                tail.set((tail.get() + 1) % concurrentBuffer.length);
                return true;
            }
            return false;
        } finally {
            reentrantLock.unlock();
        }
    }

    /**
     * FIFO 형태로 데이터를 빼고 head 를 한칸 위로 올립니다.
     * @return 데이터, 값이 없는 경우 null
     */
    public T pop() {
        reentrantLock.lock();
        try {
            int current = head.get();
            if (current != tail.get()) {
                T data = concurrentBuffer[current];
                concurrentBuffer[current] = null;
                head.set((head.get() + 1) % concurrentBuffer.length);
                return data;
            }
            return null;
        } finally {
            reentrantLock.unlock();
        }
    }

    /**
     * 특정 위치의 데이터를 조회함. capacity를 넘어가면 다시 처음으로 돌아가 조회합니다.
     * @param index 데이터 순서. 0, 1, 2, ... (capacity - 1)
     * @return 데이터, 값이 없는 경우 null
     */
    public T get(int index) {
        return concurrentBuffer[index % concurrentBuffer.length];
    }

    public int getCapacity() {
        return concurrentBuffer.length;
    }

    /**
     * 큐를 비웁니다.
     * @return 성공시 true, 실패시 false
     */
    public boolean clear() {
        reentrantLock.lock();
        try {
            Arrays.fill(concurrentBuffer, null);
            head.set(0);
            tail.set(0);
            return true;
        } catch (Exception e) {
            log.error("clear error", e);
            return false;
        } finally {
            reentrantLock.unlock();
        }
    }
}
