package sunghs.java.utils.file;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

@Slf4j
class UrlResourceDownloaderTests {

    private String url;

    private String dest;

    @BeforeEach
    void setUp() {
        url = "https://cdn.pixabay.com/photo/2021/08/22/08/54/bird-6564593__340.jpg";
        dest = "/user/sunghs/desktop/test.jpg";
    }

    @Test
    @EnabledOnOs(OS.MAC)
    void fileDownloadTest() {
        UrlResourceDownloader urlResourceDownloader = new UrlResourceDownloader(dest);
        urlResourceDownloader.download(url);
    }
}
