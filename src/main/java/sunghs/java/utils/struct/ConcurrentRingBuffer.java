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

    private AtomicInteger head;

    private AtomicInteger tail;

    private final ReentrantLock reentrantLock;

    @SuppressWarnings("unchecked")
    public ConcurrentRingBuffer(final int capacity) {
        concurrentBuffer = (T[]) new Object[capacity];
        head = new AtomicInteger(0);
        tail = new AtomicInteger(0);
        reentrantLock = new ReentrantLock();
    }

    public boolean push(final T data) {
        reentrantLock.lock();
        try {
            int next = (tail.get() + 1) % concurrentBuffer.length;
            if (next == head.get()) {
                return false;
            }
            concurrentBuffer[tail.get()] = data;
            tail.set(next);
            return true;
        } finally {
            reentrantLock.unlock();
        }
    }

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

    public T get(int index) {
        return concurrentBuffer[index % concurrentBuffer.length];
    }

    public int getCapacity() {
        return concurrentBuffer.length;
    }

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
