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
    protected final JFrame parent;

    /*
    private static final int ORIGIN_COLUMN = 2;
    private static final int DESTINATION_COLUMN = 3;
    private static final int TIME_RESTRICTION_COLUMN = 4;
    private static final int REMARKS_COLUMN = 5;
    private static final int ROUTE_COLUMN = 6;
     */

    protected int originColumn = 0;
    protected int destinationColumn = 0;
    protected int timeRestrictionColumn = 0;
    protected int remarksColumn = 0;
    protected int routeColumn = 0;

    protected ExcelHandler excelHandler;
    protected Sheet excelSheet;
    protected List<Route> routes;
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
        this.setColumnIndexes();
    }

    public void openExcelFile() {
        try {
            this.excelHandler.open();
            this.excelSheet = this.excelHandler.getSheet("Domestic");
        } catch (IOException e) {
            App.UI.popupError(this.parent, new RuntimeException("Failed to open excel file:" + e.getMessage()));
        } catch (EncryptedDocumentException e) {
            App.UI.popupError(this.parent, new EncryptedDocumentException("Excel file is encrypted and cannot be opened: " + e.getMessage()));
        } catch (Exception e) {
            App.UI.popupError(this.parent, e);
        }
    }

    public void setColumnIndexes() {
        this.openExcelFile();
        if (this.excelSheet == null) {
            App.UI.popupError(this.parent, new IllegalStateException("Excel file is null"));
            try {
                this.excelHandler.close();
            } catch (IOException e) {
                App.UI.popupError(this.parent, new RuntimeException("Failed to close excel file: " + e.getMessage()));
            }
            return;
        }

        String originColumnName = App.CFG.getExcelColumnNameOrigin();
        String destinationColumnName = App.CFG.getExcelColumnNameDestination();
        String timeRestrictionColumnName = App.CFG.getExcelColumnNameTimeRestriction();
        String remarksColumnName = App.CFG.getExcelColumnNameRemarks();
        String routesColumnName = App.CFG.getExcelColumnNameRoutes();

        if (originColumnName == null || destinationColumnName == null || timeRestrictionColumnName == null || remarksColumnName == null || routesColumnName == null) {
            App.UI.popupError(this.parent, new IllegalStateException("One or more column names are not set in the configuration"));
            return;
        }

        for (Row row : this.excelSheet) {
            for (Cell cell : row) {
                String cellValue;
                try {
                    cellValue = cell.getStringCellValue().trim();
                } catch (Exception e) {
                    continue;
                }
                if (!cellValue.isEmpty()) {
                    if (cellValue.equalsIgnoreCase(originColumnName)) {
                        this.originColumn = cell.getColumnIndex();
                    } else if (cellValue.equalsIgnoreCase(destinationColumnName)) {
                        this.destinationColumn = cell.getColumnIndex();
                    } else if (cellValue.equalsIgnoreCase(timeRestrictionColumnName)) {
                        this.timeRestrictionColumn = cell.getColumnIndex();
                    } else if (cellValue.equalsIgnoreCase(remarksColumnName)) {
                        this.remarksColumn = cell.getColumnIndex();
                    } else if (cellValue.equalsIgnoreCase(routesColumnName)) {
                        this.routeColumn = cell.getColumnIndex();
                    }
                }
                if (this.originColumn != 0 && this.destinationColumn != 0 && this.timeRestrictionColumn != 0 && this.remarksColumn != 0 && this.routeColumn != 0) {
                    break;
                }
            }
        }
        System.out.printf("Column indexes set: origin=%d, destination=%d, timeRestriction=%d, remarks=%d, routes=%d%n",
                this.originColumn, this.destinationColumn, this.timeRestrictionColumn, this.remarksColumn, this.routeColumn);
    }

    public void find(@NotNull String origin, @NotNull String destination) {
        this.routes.clear();
        this.origin = origin;
        this.destination = destination;

        this.openExcelFile();
        if (this.excelSheet == null) {
            App.UI.popupError(this.parent, new IllegalStateException("Excel file is null"));
            try {
                this.excelHandler.close();
            } catch (IOException e) {
                App.UI.popupError(this.parent, new RuntimeException("Failed to close excel file: " + e.getMessage()));
            }
            return;
        }
        if (this.originColumn == 0 || this.destinationColumn == 0 || this.timeRestrictionColumn == 0 || this.remarksColumn == 0 || this.routeColumn == 0) {
            App.UI.popupError(this.parent, new IllegalStateException("One or more column indexes are not set"));
            try {
                this.excelHandler.close();
            } catch (IOException e) {
                App.UI.popupError(this.parent, new RuntimeException("Failed to close excel file: " + e.getMessage()));
            }
            return;
        }
        Cell originCell = null, destinationCell = null, timeRestrictionCell, remarksCell, routeCell;
        String originValue, destinationValue, timeRestrictionValue, remarksValue, routeValue;
        for (Row row : this.excelSheet) {
            originCell = row.getCell(this.originColumn);
            destinationCell = row.getCell(this.destinationColumn);
            if (originCell != null && destinationCell != null) {
                originValue = originCell.getStringCellValue().trim();
                destinationValue = destinationCell.getStringCellValue().trim();
                if (originValue.length() == 4 && destinationValue.length() == 4 && originValue.equalsIgnoreCase(this.origin) && destinationValue.equalsIgnoreCase(this.destination)) {
                    timeRestrictionCell = row.getCell(this.timeRestrictionColumn);
                    remarksCell = row.getCell(this.remarksColumn);
                    routeCell = row.getCell(this.routeColumn);
                    timeRestrictionValue = timeRestrictionCell.getStringCellValue().trim();
                    remarksValue = remarksCell.getStringCellValue().trim();
                    routeValue = routeCell.getStringCellValue().trim();
                    Route route = new Route(originValue, destinationValue, routeValue, timeRestrictionValue, remarksValue);
                    System.out.println("Found route: " + route);
                    this.routes.add(route);
                }
            }
        }
        if (originCell == null || destinationCell == null) {
            App.UI.popupError(this.parent, new IllegalArgumentException("Cannot find origin and destination cells"));
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
