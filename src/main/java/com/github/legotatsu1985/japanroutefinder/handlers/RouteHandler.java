package com.github.legotatsu1985.japanroutefinder.handlers;

import com.github.legotatsu1985.japanroutefinder.App;
import com.github.legotatsu1985.japanroutefinder.util.Route;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class RouteHandler {
    private JFrame parent;

    private static final int ORIGIN_COLUMN = 2;
    private static final int DESTINATION_COLUMN = 3;
    private static final int TIME_RESTRICTION_COLUMN = 4;
    private static final int REMARKS_COLUMN = 5;
    private static final int ROUTE_COLUMN = 6;

    private ExcelHandler excelHandler;
    private Sheet excelSheet;
    private List<Route> routes;
    private String origin;
    private String destination;

    public RouteHandler(JFrame parent, String xlsxFilePath) {
        this.parent = parent;
        if (xlsxFilePath == null) {
            App.UI.popupError(this.parent, new IllegalArgumentException("Excel file path cannot be null"));
            return;
        }
        this.excelHandler = new ExcelHandler(xlsxFilePath);
        this.routes = new LinkedList<>();
    }

    public void find(@NotNull String origin, @NotNull String destination) {
        this.routes.clear();
        this.origin = origin;
        this.destination = destination;

        try {
            this.excelHandler.open();
        } catch (IOException e) {
            // throw new RuntimeException("Failed to open excel file: " + e.getMessage());
            App.UI.popupError(this.parent, new RuntimeException("Failed to open excel file:" + e.getMessage()));
        } catch (EncryptedDocumentException e) {
            // throw new EncryptedDocumentException("Excel file is encrypted and cannot be opened: " + e.getMessage());
            App.UI.popupError(this.parent, new EncryptedDocumentException("Excel file is encrypted and cannot be opened: " + e.getMessage()));
        } catch (Exception e) {
            App.UI.popupError(this.parent, e);
        }
        this.excelSheet = this.excelHandler.getSheet("Domestic");
        if (this.excelSheet == null) App.UI.popupError(this.parent, new IllegalStateException("Excel file is null"));

        Cell originCell, destinationCell, timeRestrictionCell, remarksCell, routeCell;
        String originValue, destinationValue, timeRestrictionValue, remarksValue, routeValue;
        for (Row row : this.excelSheet) {
            originCell = row.getCell(ORIGIN_COLUMN);
            destinationCell = row.getCell(DESTINATION_COLUMN);
            if (originCell != null && destinationCell != null) {
                originValue = originCell.getStringCellValue().trim();
                destinationValue = destinationCell.getStringCellValue().trim();
                if (originValue.length() == 4 && destinationValue.length() == 4 && originValue.equalsIgnoreCase(this.origin) && destinationValue.equalsIgnoreCase(this.destination)) {
                    timeRestrictionCell = row.getCell(TIME_RESTRICTION_COLUMN);
                    remarksCell = row.getCell(REMARKS_COLUMN);
                    routeCell = row.getCell(ROUTE_COLUMN);
                    timeRestrictionValue = timeRestrictionCell.getStringCellValue().trim();
                    remarksValue = remarksCell.getStringCellValue().trim();
                    routeValue = routeCell.getStringCellValue().trim();
                    Route route = new Route(originValue, destinationValue, routeValue, timeRestrictionValue, remarksValue);
                    System.out.println("Found route: " + route);
                    this.routes.add(route);
                }
            }
        }
        try {
            this.excelHandler.close();
        } catch (IOException e) {
            App.UI.popupError(this.parent, new RuntimeException("Failed to close excel file: " + e.getMessage()));
        }
    }

    public int getRouteCount() {return this.routes.size();}

    public List<Route> getAllRoutes() {return this.routes;}
}
