package sunghs.java.utils.convert;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class BinaryConverterTests {

    private final BinaryConverter binaryConverter = new BinaryConverter(8);

    @Test
    void test() throws IOException {
        String src = "/Users/sunghs/Downloads/test.zip";
        String dest = "/Users/sunghs/Downloads/result.zip";

        RandomAccessFile randomAccessFile1 = new RandomAccessFile(src, "r");
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

        log.info(byteStrings);

        // when (string -> byte)
        byte[] convertedBytes = binaryConverter.convertStringToBinary(byteStrings);

        RandomAccessFile randomAccessFile2 = new RandomAccessFile(dest, "rw");
        FileChannel fileChannel2 = randomAccessFile2.getChannel();
        ByteBuffer byteBuffer2 = ByteBuffer.allocate(convertedBytes.length);

        byteBuffer2.put(convertedBytes);
        byteBuffer2.flip();

        while(byteBuffer2.hasRemaining()) {
            fileChannel2.write(byteBuffer2);
        }
        randomAccessFile2.close();

        // then
        Assertions.assertTrue(compare(src, dest));
    }

    boolean compare(String src, String dest) {
        try {
            FileChannel sChannel = new RandomAccessFile(src, "r").getChannel();
            FileChannel dChannel = new RandomAccessFile(dest, "r").getChannel();

            if (sChannel.size() != dChannel.size()) {
                return false;
            }

            ByteBuffer sBuffer = sChannel.map(MapMode.READ_ONLY, 0, sChannel.size());
            ByteBuffer dBuffer = sChannel.map(MapMode.READ_ONLY, 0, sChannel.size());

            for (int i = 0; i < sChannel.size(); i ++) {
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
