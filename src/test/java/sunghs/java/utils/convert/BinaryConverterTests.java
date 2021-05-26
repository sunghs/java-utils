package sunghs.java.utils.convert;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class BinaryConverterTests {

    private final BinaryConverter binaryConverter = new BinaryConverter(8);

    @Test
    void test() throws IOException {
        RandomAccessFile randomAccessFile1 = new RandomAccessFile("/Users/sunghs/Downloads/test.zip", "r");
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

        RandomAccessFile randomAccessFile2 = new RandomAccessFile("/Users/sunghs/Downloads/result.zip", "rw");
        FileChannel fileChannel2 = randomAccessFile2.getChannel();
        ByteBuffer byteBuffer2 = ByteBuffer.allocate(convertedBytes.length);

        byteBuffer2.put(convertedBytes);
        byteBuffer2.flip();

        while(byteBuffer2.hasRemaining()) {
            fileChannel2.write(byteBuffer2);
        }
        randomAccessFile2.close();
    }
}
