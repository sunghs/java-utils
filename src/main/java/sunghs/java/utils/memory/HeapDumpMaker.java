package sunghs.java.utils.memory;

import com.sun.management.HotSpotDiagnosticMXBean;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;
import javax.management.MBeanServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

/**
 * 현재 시스템 Java Heap Dump hprof 파일을 만드는 클래스
 */
@Slf4j
public class HeapDumpMaker {

    private static final String HOTSPOT_BEAN_NAME = "com.sun.management:type=HotSpotDiagnostic";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
        .ofPattern("yyyy-MM-dd-HH-mm-ss");

    private static final String NAME_PREFIX = "heap-";

    private static final String EXTENSION_SUFFIX = ".hprof";

    private static final String DEFAULT_SAVE_PATH = "./heap";

    private static HotSpotDiagnosticMXBean DIAGNOSTIC_MX_BEAN;

    public HeapDumpMaker() {
        Path defaultPath = Paths.get(DEFAULT_SAVE_PATH);
        try {
            if (!Files.exists(defaultPath) && !Files.isDirectory(defaultPath)) {
                Files.createDirectory(defaultPath);
            }
        } catch (Exception e) {
            log.error("heap dump instance create error", e);
        }
    }

    private HotSpotDiagnosticMXBean getHotspotMBean() {
        try {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            return ManagementFactory.newPlatformMXBeanProxy(server, HOTSPOT_BEAN_NAME, HotSpotDiagnosticMXBean.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized void initHotspotMBean() {
        if (DIAGNOSTIC_MX_BEAN == null) {
            DIAGNOSTIC_MX_BEAN = getHotspotMBean();
        }
    }

    private String getFileName() {
        return NAME_PREFIX + LocalDateTime.now().format(DATE_TIME_FORMATTER) + EXTENSION_SUFFIX;
    }

    public Resource dump() {
        final Path savePath = Paths.get(DEFAULT_SAVE_PATH);
        final String fileName = getFileName();

        delete(savePath);
        initHotspotMBean();

        try {
            getHotspotMBean().dumpHeap(savePath + "/" + fileName, false);
            log.info("{} file create success", fileName);
            Path path = savePath.toAbsolutePath().normalize().resolve(fileName).normalize();
            return new UrlResource(path.toUri());
        } catch (Exception e) {
            log.error("create dump error", e);
            return null;
        }
    }

    public void delete(Path directory) {
        try (Stream<Path> pathStream = Files.list(directory)) {
            pathStream.filter(path -> path.toString().contains(NAME_PREFIX)).forEach(path -> {
                try {
                    Files.delete(path);
                } catch (Exception e) {
                    log.error("file delete error", e);
                }
            });
        } catch (IOException e) {
            log.error("get path list error", e);
        }
    }
}
