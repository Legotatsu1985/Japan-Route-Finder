package com.github.legotatsu1985.japanroutefinder.handlers;

import com.github.legotatsu1985.japanroutefinder.App;
import com.github.legotatsu1985.japanroutefinder.util.Route;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.IOException;

public class InternationalRouteHandler extends RouteHandler {
    public static final int OUTBOUND = 0;
    public static final int INBOUND = 1;

    public InternationalRouteHandler(JFrame parent, String xlsxFilePath) {
        super(parent, xlsxFilePath);
    }

    public void openExcelFile() {
        try {
            super.excelHandler.open();
            super.excelSheet = super.excelHandler.getSheet("International Overflight");
        } catch (IOException e) {
            App.UI.popupError(super.parent, new RuntimeException("Failed to open excel file:" + e.getMessage()));
        } catch (EncryptedDocumentException e) {
            App.UI.popupError(super.parent, new EncryptedDocumentException("Excel file is encrypted and cannot be opened: " + e.getMessage()));
        } catch (Exception e) {
            App.UI.popupError(super.parent, e);
        }
    }

    public void find(@NotNull String arg1, @NotNull String arg2, int direction) {
        super.routes.clear();

        this.openExcelFile();
        if (super.excelSheet == null) {
            App.UI.popupError(super.parent, new IllegalStateException("Excel file is null"));
            try {
                super.excelHandler.close();
            } catch (IOException e) {
                App.UI.popupError(super.parent, new RuntimeException("Failed to close excel file: " + e.getMessage()));
            }
            return;
        }
        if (super.originColumn == 0 || super.destinationColumn == 0 || super.timeRestrictionColumn == 0 || super.remarksColumn == 0 || super.routeColumn == 0) {
            App.UI.popupError(super.parent, new IllegalStateException("One or more column indexes are not set"));
            try {
                super.excelHandler.close();
            } catch (IOException e) {
                App.UI.popupError(super.parent, new RuntimeException("Failed to close excel file: " + e.getMessage()));
            }
            return;
        }

        Cell originCell = null, destinationCell = null, timeRestrictionCell, remarksCell, routeCell = null;
        String originValue, destinationValue, timeRestrictionValue, remarksValue, routeValue;
        if (direction == OUTBOUND) {
            // arg1 = Origin ICAO;
            // arg2 = Airspace Exit Waypoint Name;
            for (Row row : super.excelSheet) {
                originCell = row.getCell(super.originColumn);
                routeCell = row.getCell(super.routeColumn);
                if (originCell != null && routeCell != null) {
                    originValue = originCell.getStringCellValue().trim();
                    routeValue = routeCell.getStringCellValue().trim();
                    if (originValue.length() == 4 && arg2.length() == 5 && originValue.equalsIgnoreCase(arg1) && routeValue.endsWith(arg2)) {
                        destinationCell = row.getCell(super.destinationColumn);
                        timeRestrictionCell = row.getCell(super.timeRestrictionColumn);
                        remarksCell = row.getCell(super.remarksColumn);
                        destinationValue = destinationCell.getStringCellValue().trim();
                        timeRestrictionValue = timeRestrictionCell.getStringCellValue().trim();
                        remarksValue = remarksCell.getStringCellValue().trim();
                        Route route = new Route(originValue, destinationValue, routeValue, timeRestrictionValue, remarksValue);
                        System.out.println("Found route: " + route);
                        super.routes.add(route);
                    }
                }
            }
            if (originCell == null || routeCell == null) {
                App.UI.popupError(super.parent, new IllegalArgumentException("Cannot find origin and route cells"));
            }
        } else if (direction == INBOUND) {
            // arg1 = Destination ICAO;
            // arg2 = Airspace Entry Waypoint Name;
            for (Row row : super.excelSheet) {
                destinationCell = row.getCell(super.destinationColumn);
                routeCell = row.getCell(super.routeColumn);
                if (destinationCell != null && routeCell != null) {
                    destinationValue = destinationCell.getStringCellValue().trim();
                    routeValue = routeCell.getStringCellValue().trim();
                    if (destinationValue.length() == 4 && arg2.length() == 5 && destinationValue.equalsIgnoreCase(arg1) && routeValue.startsWith(arg2)) {
                        originCell = row.getCell(super.originColumn);
                        timeRestrictionCell = row.getCell(super.timeRestrictionColumn);
                        remarksCell = row.getCell(super.remarksColumn);
                        originValue = originCell.getStringCellValue().trim();
                        timeRestrictionValue = timeRestrictionCell.getStringCellValue().trim();
                        remarksValue = remarksCell.getStringCellValue().trim();
                        Route route = new Route(originValue, destinationValue, routeValue, timeRestrictionValue, remarksValue);
                        System.out.println("Found route: " + route);
                        super.routes.add(route);
                    }
                }
            }
            if (destinationCell == null || routeCell == null) {
                App.UI.popupError(super.parent, new IllegalArgumentException("Cannot find destination and route cells"));
            }
        } else {
            App.UI.popupError(super.parent, new IllegalArgumentException("Invalid direction value: " + direction));
        }
        try {
            super.excelHandler.close();
        } catch (IOException e) {
            App.UI.popupError(super.parent, new RuntimeException("Failed to close excel file: " + e.getMessage()));
        }
    }
}
