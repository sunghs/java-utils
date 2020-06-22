package sunghs.java.utils.file;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ExcelParserTest {

    @Test
    public void excelTestWithHeader() throws IOException {
        ExcelParser parser = new ExcelParser(true);
        File excel = new File("/Users/sunghs/header.xlsx");
        List<Map<String, String>> list = parser.parse(excel, 0);

        for(Map<String, String> row : list) {
            for(String key : row.keySet()) {
                log.info("key : {}, value : {}", key, row.get(key));
            }
        }
    }

    @Test
    public void excelTestWithNoHeader() throws IOException {
        ExcelParser parser = new ExcelParser(false);
        File excel = new File("/Users/sunghs/noheader.xlsx");
        List<Map<String, String>> list = parser.parse(excel, 0);

        for(Map<String, String> row : list) {
            for(String key : row.keySet()) {
                log.info("key : {}, value : {}", key, row.get(key));
            }
        }
    }
}
