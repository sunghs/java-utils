package sunghs.java.utils.convert;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
class BinaryConverterTests {

    private final BinaryConverter binaryConverter = new BinaryConverter(8);

    private String src;

    private String dest;

    @BeforeEach
    void setUp() {
        src = "/Users/sunghs/Downloads/test.zip";
        dest = "/Users/sunghs/Downloads/result.zip";
    }

    /**
     * 테스트 경로에 파일이 없다면 테스트 실패합니다. 주의
     *
     * @throws IOException exception
     */
    @Test
    @EnabledOnOs(OS.MAC)
    void test() throws IOException {
        RandomAccessFile randomAccessFile1;

        try {
            randomAccessFile1 = new RandomAccessFile(src, "r");
        } catch (FileNotFoundException e) {
            log.info("file not found exception, skip test : {}", src);
            return;
        }
        FileChannel fileChannel1 = randomAccessFile1.getChannel();
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(64);

        // given
        List<Byte> zip = new ArrayList<>();

        while (fileChannel1.read(byteBuffer1) != -1) {
            byteBuffer1.flip();
            while (byteBuffer1.hasRemaining()) {
                zip.add(byteBuffer1.get());
            }
            byteBuffer1.clear();
        }
        randomAccessFile1.close();

        byte[] bytes = new byte[zip.size()];

        for (int i = 0; i < zip.size(); i++) {
            bytes[i] = zip.get(i);
        }

        // when (byte -> string)
        String byteStrings = binaryConverter.convertBinaryToString(bytes);

        // when (string -> byte)
        byte[] convertedBytes = binaryConverter.convertStringToBinary(byteStrings);

        RandomAccessFile randomAccessFile2 = new RandomAccessFile(dest, "rw");
        FileChannel fileChannel2 = randomAccessFile2.getChannel();
        ByteBuffer byteBuffer2 = ByteBuffer.allocate(convertedBytes.length);

        byteBuffer2.put(convertedBytes);
        byteBuffer2.flip();

        while (byteBuffer2.hasRemaining()) {
            fileChannel2.write(byteBuffer2);
        }
        randomAccessFile2.close();

        // then
        Assertions.assertTrue(compare(src, dest));
    }

    boolean compare(String src, String dest) {
        try (FileChannel sChannel = new RandomAccessFile(src, "r").getChannel();
             FileChannel dChannel = new RandomAccessFile(dest, "r").getChannel()) {

            if (sChannel.size() != dChannel.size()) {
                return false;
            }

            ByteBuffer sBuffer = sChannel.map(MapMode.READ_ONLY, 0, sChannel.size());
            ByteBuffer dBuffer = sChannel.map(MapMode.READ_ONLY, 0, sChannel.size());

            for (int i = 0; i < sChannel.size(); i++) {
                if (sBuffer.get(i) != dBuffer.get(i)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            log.error("error", e);
            return false;
        }
    }
}
