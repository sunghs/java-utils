package sunghs.java.utils.file;

import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * URL 리소스 다운로더
 *
 * @author https://sunghs.tistory.com
 * @see <a href="https://github.com/sunghs/java-utils">source</a>
 */
@Slf4j
public class UrlResourceDownloader {

    private final long count;

    private final String fileName;

    public UrlResourceDownloader(String fileName) {
        this.fileName = fileName;
        this.count = Long.MAX_VALUE;
    }

    public UrlResourceDownloader(String fileName, long count) {
        this.fileName = fileName;
        this.count = count;
    }

    public String getFileName() {
        return this.fileName;
    }

    public long getCount() {
        return this.count;
    }

    /**
     * 파일 다운로드
     * @param urlString 다운받을 URL
     */
    public void download(String urlString) {
        try {
            URL url = new URL(urlString);
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());

            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, count);
            fileOutputStream.close();
        } catch (IOException e) {
            log.error("file download error", e);
        }
    }
}
