package sunghs.java.utils.file;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 테스트 경로에 파일이 없다면 테스트 실패합니다.
 */
@Slf4j
class ExcelParserTest {

    private String excelFilePath;

    @BeforeEach
    void setUp() {
        excelFilePath = "/Users/sunghs/excel.xlsx";
    }

    @Test
    @EnabledOnOs(OS.MAC)
    void excelTestWithHeader() throws IOException {
        ExcelParser parser = new ExcelParser(true);
        File excel = new File(excelFilePath);
        List<Map<String, String>> list = parser.parse(excel, 0);

        for (Map<String, String> row : list) {
            for (String key : row.keySet()) {
                log.info("key : {}, value : {}", key, row.get(key));
            }
        }
    }

    @Test
    @EnabledOnOs(OS.MAC)
    void excelTestWithNoHeader() throws IOException {
        ExcelParser parser = new ExcelParser(false);
        File excel = new File(excelFilePath);
        List<Map<String, String>> list = parser.parse(excel, 1);

        for (Map<String, String> row : list) {
            for (String key : row.keySet()) {
                log.info("key : {}, value : {}", key, row.get(key));
            }
        }
    }
}
