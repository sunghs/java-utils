package sunghs.java.utils.memory;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Slf4j
class HeapDumpMakerTests {

    @Test
    void test() throws IOException {
        HeapDumpMaker heapDumpMaker = new HeapDumpMaker();
        Resource resource = heapDumpMaker.dump();

        Assertions.assertTrue(resource.isFile());
        Assertions.assertTrue(resource.contentLength() > 0);
        Assertions.assertTrue(StringUtils.isNotEmpty(resource.getFilename()));

        log.info(resource.getFilename());
    }
}
