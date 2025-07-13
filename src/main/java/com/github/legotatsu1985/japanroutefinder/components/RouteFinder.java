package com.github.legotatsu1985.japanroutefinder.components;

import com.github.legotatsu1985.japanroutefinder.ui.textlabels.LangJsonLoader;

import java.io.FileInputStream;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.*;

public class RouteFinder {
    public static String findRoute(String xlsxFilePath,String originICAO,String destICAO) throws Exception {
        String langCode = LangJsonLoader.checkLang();
        LangJsonLoader lang = new LangJsonLoader(langCode);

        FileInputStream fis = new FileInputStream(xlsxFilePath);
        Workbook workbook = WorkbookFactory.create(fis);
        Sheet DomesticSheet = workbook.getSheetAt(1);
        StringBuilder finalRoute = new StringBuilder();
        String finalRouteLongString = null;

        if (DomesticSheet == null) {
            System.out.println("Domestic sheet not found in the provided Excel file.");
            return lang.getText("main_searchRouteSheetNotFound");
        }
        if (originICAO == null || destICAO == null || originICAO.isEmpty() || destICAO.isEmpty()) {
            System.out.println("Origin or destination ICAO code is null or empty.");
            return lang.getText("main_searchRouteAirportsNullOrEmpty");
        }
        for (Row row : DomesticSheet) {
            Cell originCell = row.getCell(1); // 出発空港のセル
            Cell destCell = row.getCell(2); // 到着空港のセル
            if (originCell != null && destCell != null) {
                String origin = originCell.getStringCellValue().trim();
                String dest = destCell.getStringCellValue().trim();

                if (origin.equalsIgnoreCase(originICAO) && dest.equalsIgnoreCase(destICAO)) {
                    Cell routeCell = row.getCell(5); // ルートのセル
                    if (routeCell != null) {
                        String route = routeCell.getStringCellValue().trim();
                        if (!route.isEmpty()) {
                            finalRoute.append(route).append("\n");
                            finalRouteLongString = finalRoute.toString();
                        }
                    }
                }
            }
        }
        fis.close();
        if (finalRouteLongString != null && !finalRouteLongString.isEmpty()) {
            System.out.println("Route found:");
            System.out.println(finalRouteLongString);
            return finalRouteLongString;
        } else {
            System.out.println("No route found.");
            return lang.getText("main_searchRouteNotFound");
        }
    }
    public static String findRouteRemarks(String xlsxFilePath,String originICAO,String destICAO) throws Exception {
        String langCode = LangJsonLoader.checkLang();
        LangJsonLoader lang = new LangJsonLoader(langCode);

        FileInputStream fis = new FileInputStream(xlsxFilePath);
        Workbook workbook = WorkbookFactory.create(fis);
        Sheet DomesticSheet = workbook.getSheetAt(1);
        StringBuilder finalRouteRemarks = new StringBuilder();
        String finalRouteRemarksLongString = null;

        if (DomesticSheet == null) {
            System.out.println("Domestic sheet not found in the provided Excel file.");
            return lang.getText("main_searchRouteSheetNotFound");
        }
        if (originICAO == null || destICAO == null || originICAO.isEmpty() || destICAO.isEmpty()) {
            System.out.println("Origin or destination ICAO code is null or empty.");
            return lang.getText("main_searchRouteAirportsNullOrEmpty");
        }
        for (Row row : DomesticSheet) {
            Cell originCell = row.getCell(1);
            Cell destCell = row.getCell(2);
            if (originCell != null && destCell != null) {
                String origin = originCell.getStringCellValue().trim();
                String dest = destCell.getStringCellValue().trim();

                if (origin.equalsIgnoreCase(originICAO) && dest.equalsIgnoreCase(destICAO)) {
                    Cell remarksCell = row.getCell(4);
                    String remarks = remarksCell.getStringCellValue().trim();
                    //System.out.println(remarks);
                    String regexE = "only for pre-coordination flight"; // 正規表現パターン
                    String regex = "only for pre-coordination flight$"; // 正規表現パターン
                    boolean isExactMatch = Pattern.matches(regexE, remarks);
                    boolean isPartMatch = Pattern.matches(".*" + regex, remarks);
                    boolean isEndWithString = remarks.endsWith(regexE);
                    //System.out.println("Exact match: " + isExactMatch);
                    //System.out.println("Part match: " + isPartMatch);
                    if (!remarks.isEmpty()) { // ルートの備考が空でない場合
                        if (isExactMatch) { // 特定の備考の場合 <- なぜか動作しない :(
                            finalRouteRemarks.append("\n");
                        } else if (isPartMatch || isEndWithString) {
                            String modifiedRemarks = remarks.substring(0, remarks.length() - regexE.length());
                            finalRouteRemarks.append(modifiedRemarks.trim()).append("\n");
                        } else {
                            finalRouteRemarks.append(remarks).append("\n");
                        }
                    } else {
                        finalRouteRemarks.append("\n");
                    }
                    finalRouteRemarksLongString = finalRouteRemarks.toString();
                }
            }
        }
        fis.close();
        if (finalRouteRemarksLongString != null && !finalRouteRemarksLongString.isEmpty()) {
            System.out.println("Route remarks found:");
            System.out.println(finalRouteRemarksLongString);
            return finalRouteRemarksLongString;
        } else {
            System.out.println("No route remarks found.");
            return null;
        }
    }
}
