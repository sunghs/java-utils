package sunghs.java.utils.file;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
class ExcelParserTest {

    @Test
    void excelTestWithHeader() throws IOException {
        ExcelParser parser = new ExcelParser(true);
        File excel = new File("/Users/sunghs/excel.xlsx");
        List<Map<String, String>> list = parser.parse(excel, 0);

        for(Map<String, String> row : list) {
            for(String key : row.keySet()) {
                log.info("key : {}, value : {}", key, row.get(key));
            }
        }
    }

    @Test
    void excelTestWithNoHeader() throws IOException {
        ExcelParser parser = new ExcelParser(false);
        File excel = new File("/Users/sunghs/excel.xlsx");
        List<Map<String, String>> list = parser.parse(excel, 1);

        for(Map<String, String> row : list) {
            for(String key : row.keySet()) {
                log.info("key : {}, value : {}", key, row.get(key));
            }
        }
    }
}
