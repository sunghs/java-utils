package sunghs.java.utils.file;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import sunghs.java.utils.file.exception.InvalidResourceContextException;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
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
    }

    public void setUrlMultiResourceContext(UrlMultiResourceContext urlMultiResourceContext) {
        if (urlMultiResourceContext.start < 0 || urlMultiResourceContext.end < urlMultiResourceContext.start) {
            throw new InvalidResourceContextException("start는 0보다 커야하며 end 는 start 보다 같거나 커야합니다.");
        }
        this.urlMultiResourceContext = urlMultiResourceContext;
        for (int i = urlMultiResourceContext.start; i <= urlMultiResourceContext.end; i++) {
            String url = urlMultiResourceContext.beforeUrl
                    + (urlMultiResourceContext.format != null ? String.format(urlMultiResourceContext.format, i) : i)
                    + (urlMultiResourceContext.afterUrl != null ? urlMultiResourceContext.afterUrl : "");
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
        List<Callable<Boolean>> workList = new ArrayList<>();

        for (int i = urlMultiResourceContext.start; i <= urlMultiResourceContext.end; i++) {
            String url = urls.get(i);
            String path = urlMultiResourceContext.downloadPath;
            String fileName = i + urlMultiResourceContext.afterUrl;
            workList.add(() -> download(url, path, fileName, failList));
        }

        try {
            worker.invokeAll(workList);
        } catch (InterruptedException e) {
            log.error("worker error", e);
        } finally {
            worker.shutdown();
        }
        return failList;
    }

    /**
     * 현재 디렉토리 내의 여러개의 .ext 파일을 하나로 뭉칩니다.
     * 결과물은 output.ext 입니다.
     * 단순 병합이기에 분할 알고리즘이 다른 경우 사용 할 수 없습니다.
     * @param extension extension
     */
    public void mergeCommand(final String extension) {
        String path = urlMultiResourceContext.downloadPath.replace("\\", "/");
        String command = "cmd /c cd " + path + " & " + "copy /b *." + extension + " output." + extension;
        try {
            Runtime.getRuntime().exec(command);
        } catch (Exception e) {
            log.error("mergeCommand error", e);
        }
    }

    private boolean download(String url, String path, String fileName, List<String> failList) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path + fileName);
            InputStream inputStream = new URL(url).openStream();

            int size = 1048576; // 1MB
            byte[] buff = new byte[size];
            float buffSize;
            float downloadSize = 0;

            while ((buffSize = inputStream.read(buff)) > 0) {
                System.out.flush();
                downloadSize = downloadSize + (buffSize / size);
                fileOutputStream.write(buff, 0, (int) buffSize);
            }
            fileOutputStream.close();
            inputStream.close();
            log.info(" {} -> 성공", fileName);
            return true;
        } catch (Exception e) {
            log.error("{} -> 실패, 사유 {}", fileName, e.getLocalizedMessage());
            failList.add(url);
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
