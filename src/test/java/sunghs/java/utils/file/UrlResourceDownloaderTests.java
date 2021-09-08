package sunghs.java.utils.file;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class UrlResourceDownloaderTests {

    String url;

    @BeforeEach
    void setUp() {
        url = "https://cdn.pixabay.com/photo/2021/08/22/08/54/bird-6564593__340.jpg";
    }

    @Test
    void fileDownloadTest() {
        UrlResourceDownloader urlResourceDownloader = new UrlResourceDownloader("/user/sunghs/desktop/test.jpg");
        urlResourceDownloader.download(url);
    }
}
