package com.github.legotatsu1985.japanroutefinder.handlers;

import com.github.legotatsu1985.japanroutefinder.App;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ExcelHandler {
    private String filePath;
    private FileInputStream fis;
    private Workbook excelWorkbook;
    private List<Sheet> sheets;

    public ExcelHandler(@NotNull String filePath) {this.filePath = filePath;}

    public void open() throws IOException, EncryptedDocumentException {
        this.fis = new FileInputStream(this.filePath);
        this.excelWorkbook = WorkbookFactory.create(this.fis);

        // 全てのシートをリストに格納
        this.sheets = new LinkedList<>();
        for (Sheet sheet : this.excelWorkbook) {
            this.sheets.add(sheet);
        }
    }

    public void close() throws IOException {
        if (this.excelWorkbook != null) this.excelWorkbook.close();
        if (this.fis != null) this.fis.close();
    }

    @Nullable
    public Sheet getSheet(@NotNull String sheetName) {
        if (this.excelWorkbook == null) {
            App.UI.popupError(null, new IllegalStateException("Excel workbook is not opened. Please call open() method first."));
        }
        if (this.sheets == null || this.sheets.isEmpty()) {
            App.UI.popupError(null, new IllegalStateException("No sheets found in the workbook."));
        }

        for (Sheet sheet : this.sheets) {
            if (sheet.getSheetName().equalsIgnoreCase(sheetName)) return sheet;
        }
        App.UI.popupError(null, new IllegalArgumentException("Sheet with name '" + sheetName + "' not found."));
        return null;
    }
}
