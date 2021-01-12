package com.seagalputra.excelreader;

import org.apache.logging.log4j.util.Strings;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ExcelReader {

    public <T> List<T> read(InputStream inputStream, Function<Row, T> transformer) {
        List<T> results = new ArrayList<>();
        try (ReadableWorkbook workbook = new ReadableWorkbook(inputStream)) {
            Sheet sheet = workbook.getFirstSheet();

            try (Stream<Row> rows = sheet.openStream()) {
                results = rows.skip(1L)
                        .map(transformer)
                        .collect(Collectors.toList());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String getStringValue(Row row, int index) {
        return row.getCellAsString(index)
                .orElse(Strings.EMPTY);
    }
}
