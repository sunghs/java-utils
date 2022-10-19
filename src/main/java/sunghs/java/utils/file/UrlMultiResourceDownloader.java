package sunghs.java.utils.file;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 리소스 이름에 일정한 숫자규칙이 있는 여러 파일 (ex : *.ts) 을 병렬로 내려 받는 다운로더
 *
 * @author https://sunghs.tistory.com
 * @literal ex) https://cdn.example.com/test{#0-100#}
 * @see <a href="https://github.com/sunghs/java-utils">source</a>
 */
@Slf4j
public class UrlMultiResourceDownloader {

    private final List<String> urls;

    private final ExecutorService worker;

    private UrlMultiResourceContext urlMultiResourceContext;

    public UrlMultiResourceDownloader() {
        this(1);
    }

    public UrlMultiResourceDownloader(final int workerLimit) {
        this.urls = new ArrayList<>();
        this.worker = Executors.newFixedThreadPool(workerLimit);
        // copy /b *.ts output-file.ts
    }

    public void setUrlMultiResourceContext(UrlMultiResourceContext urlMultiResourceContext) {
        if (urlMultiResourceContext.start < 0 || urlMultiResourceContext.end < urlMultiResourceContext.start) {
            throw new RuntimeException("start는 0보다 커야하며 end 는 start 보다 같거나 커야합니다.");
        }
        this.urlMultiResourceContext = urlMultiResourceContext;

        for (int i = urlMultiResourceContext.start; i <= urlMultiResourceContext.end; i++) {
            String url = urlMultiResourceContext.beforeUrl
                    + String.format(urlMultiResourceContext.format, i)
                    + (urlMultiResourceContext.afterUrl == null ? "" : urlMultiResourceContext.afterUrl);
            urls.add(url);
            log.info("리스트 URL 추가 : {}", url);
        }
    }

    public List<String> download() {
        if (urls.size() == 0) {
            log.info("URL 리스트 0, download 종료");
            return Collections.emptyList();
        }

        List<String> failList = new ArrayList<>();

        for (int i = urlMultiResourceContext.start; i <= urlMultiResourceContext.end; i++) {
            String url = urls.get(i);
            String path = urlMultiResourceContext.downloadPath;
            String fileName = i + urlMultiResourceContext.afterUrl;
            worker.submit(() -> {
                boolean result = download(url, path, fileName);
                log.info("{} :: {} -> {}", Thread.currentThread(), fileName, result);
                if (!result) {
                    failList.add(url);
                }
            });
        }
        return failList;
    }

    private boolean download(String url, String path, String fileName) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            InputStream inputStream = new URL(url).openStream();

            int size = 1048576; // 1MB
            byte[] buff = new byte[size];
            float buffSize = 0;
            float downloadSize = 0;

            while ((buffSize = inputStream.read(buff)) > 0) {
                System.out.flush();
                downloadSize = downloadSize + (buffSize / size);
                fileOutputStream.write(buff, 0, (int) buffSize);
            }
            fileOutputStream.close();
            inputStream.close();
            return true;
        } catch (Exception e) {
            log.error("{} 다운로드 실패, 사유 : {}", url.toString(), e.getLocalizedMessage());
            return false;
        }
    }

    @Getter
    @Builder
    public static class UrlMultiResourceContext {

        private final String beforeUrl;

        private final String afterUrl;

        private final String format;

        private final int start;

        private final int end;

        private final String downloadPath;
    }
}
