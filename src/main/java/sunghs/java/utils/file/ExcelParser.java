package sunghs.java.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 엑셀 파일 파서
 *
 * @author https://sunghs.tistory.com
 * @see <a href="https://github.com/sunghs/java-utils">source</a>
 */
public class ExcelParser {

    private final boolean isHeader;

    private final DataFormatter formatter;

    /**
     * @param isHeader 엑셀의 첫줄을 헤더로 볼 지 여부
     */
    public ExcelParser(final boolean isHeader) {
        this.isHeader = isHeader;
        this.formatter = new DataFormatter();
    }

    /**
     * 엑셀을 읽어 List&lt;Map&gt;로 반환
     *
     * @param excelFile
     * @param sheetIndex
     * @return
     * @throws IOException
     */
    public List<Map<String, String>> parse(final File excelFile, final int sheetIndex)
        throws IOException {
        Workbook workbook = new XSSFWorkbook(new FileInputStream(excelFile));
        Sheet sheet = workbook.getSheetAt(sheetIndex);

        int rowCount = sheet.getPhysicalNumberOfRows();

        if (rowCount <= 1) {
            return null;
        }

        List<String> header = new ArrayList<>();
        Row headerRow = sheet.getRow(0);

        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
            Cell cursor = headerRow.getCell(i);
            if (isHeader) {
                header.add(formatter.formatCellValue(cursor));
            } else {
                header.add(String.valueOf(headerRow.getCell(i).getColumnIndex()));
            }
        }

        List<Map<String, String>> result = new ArrayList<>();

        for (int i = 1; i < rowCount; i++) {
            Row row = sheet.getRow(i);
            result.add(parseRow(header, row));
        }
        return result;
    }

    /**
     * 1개의 행을 읽어 Map으로 반환
     *
     * @param header 헤더 리스트 (첫 행)
     * @param row    데이터 (2번쨰 행부터)
     * @return Map
     */
    private Map<String, String> parseRow(List<String> header, Row row) {
        Map<String, String> data = new HashMap<>();
        for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
            Cell cursor = row.getCell(i);
            data.put(header.get(i), formatter.formatCellValue(cursor));
        }
        return data;
    }
}
