import java.io.FileInputStream;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.*;
import org.jetbrains.annotations.Nullable;

public class RouteFinder {
    private static final int DOMESTIC_SHEET_INDEX = 1;     // 国内線シートのインデックス
    private static final int ORIGIN_COLUMN_INDEX = 1;      // 出発空港の列インデックス
    private static final int DESTINATION_COLUMN_INDEX = 2; // 到着空港の列インデックス
    private static final int ROUTE_COLUMN_INDEX = 5;       // ルートの列インデックス
    private static final int REMARKS_COLUMN_INDEX = 4;     // 備考の列インデックス
    private static final String REGEX_E = "only for pre-coordination flight";
    private static final String REGEX = REGEX_E + "$";
    private String xlsxFilePath;
    private String originICAO;
    private String destICAO;
    private FileInputStream fileInputStream;
    private Workbook workbook;
    private Sheet domesticSheet;
    private StringBuilder routes;
    private StringBuilder remarks;
    private String formattedRoutes;
    private String formattedRemarks;

    public RouteFinder(String xlsxFilePath, String originICAO, String destICAO) {
        this.xlsxFilePath = xlsxFilePath;
        this.originICAO = originICAO;
        this.destICAO = destICAO;
    }
    public void open() throws Exception {
        if (this.xlsxFilePath != null && !this.xlsxFilePath.isEmpty()) {
            this.fileInputStream = new FileInputStream(this.xlsxFilePath);
            this.workbook = WorkbookFactory.create(this.fileInputStream);
            this.domesticSheet = this.workbook.getSheetAt(DOMESTIC_SHEET_INDEX);
        } else {
            throw new IllegalStateException("XLSX file path is null or empty.");
        }
    }
    public void close() throws Exception {
        if (this.workbook != null) {
            this.workbook.close();
        }
        if (this.fileInputStream != null) {
            this.fileInputStream.close();
        }
    }
    @Nullable
    public String findRoute() throws Exception {
        if (this.domesticSheet == null) {
            throw new IllegalStateException("Domestic sheet is not loaded.");
        }
        if (this.originICAO == null || this.destICAO == null || this.originICAO.isEmpty() || this.destICAO.isEmpty()) {
            throw new IllegalStateException("Origin or destination ICAO code is null or empty.");
        }
        this.routes = new StringBuilder();
        for (Row row : this.domesticSheet) {
            Cell originCell = row.getCell(ORIGIN_COLUMN_INDEX);
            Cell destCell = row.getCell(DESTINATION_COLUMN_INDEX);
            if (originCell != null && destCell != null) { // 出発空港と到着空港のセルが存在する場合
                String origin = originCell.getStringCellValue().trim(); // 出発空港を取得
                String dest = destCell.getStringCellValue().trim(); // 到着空港を取得
                if (origin.equalsIgnoreCase(this.originICAO) && dest.equalsIgnoreCase(this.destICAO)) { // ICAOコードが一致する場合
                    Cell routeCell = row.getCell(ROUTE_COLUMN_INDEX);
                    if (routeCell != null) { // ルートのセルが存在する場合
                        String route = routeCell.getStringCellValue().trim(); // ルートを取得
                        if (!route.isEmpty()) { // ルートが空でない場合
                            this.routes.append(route).append("\n");
                            this.formattedRoutes = this.routes.toString();
                        }
                    }
                }
            }
        }
        if (this.formattedRoutes != null && !this.formattedRoutes.isEmpty()) {
            return this.formattedRoutes;
        } else {
            return null;
        }
    }
    @Nullable
    public String findRouteRemarks() throws Exception {
        if (this.domesticSheet == null) {
            throw new IllegalStateException("Domestic sheet is not loaded.");
        }
        if (this.originICAO == null || this.destICAO == null || this.originICAO.isEmpty() || this.destICAO.isEmpty()) {
            throw new IllegalStateException("Origin or destination ICAO code is null or empty.");
        }
        this.remarks = new StringBuilder();
        for (Row row : this.domesticSheet) {
            Cell originCell = row.getCell(ORIGIN_COLUMN_INDEX);
            Cell destCell = row.getCell(DESTINATION_COLUMN_INDEX);
            if (originCell != null && destCell != null) {
                String origin = originCell.getStringCellValue().trim();
                String dest = destCell.getStringCellValue().trim();
                if (origin.equalsIgnoreCase(this.originICAO) && dest.equalsIgnoreCase(this.destICAO)) {
                    Cell remarksCell = row.getCell(REMARKS_COLUMN_INDEX);
                    String remarks = remarksCell.getStringCellValue().trim();
                    boolean isExactMatch = Pattern.matches(REGEX_E, remarks);
                    boolean isPartialMatch = Pattern.matches(".*" + REGEX, remarks);
                    boolean isEndWithString = remarks.endsWith(REGEX_E);
                    if (!remarks.isEmpty()) {
                        if (isExactMatch) {
                            this.remarks.append("\n");
                        } else if (isPartialMatch || isEndWithString) {
                            String modifiedRemarks = remarks.substring(0, remarks.length() - REGEX_E.length());
                            this.remarks.append(modifiedRemarks.trim()).append("\n");
                        } else {
                            this.remarks.append(remarks).append("\n");
                        }
                    } else {
                        this.remarks.append("\n");
                    }
                    this.formattedRemarks = this.remarks.toString();
                }
            }
        }
        if (this.formattedRemarks != null && !this.formattedRemarks.isEmpty()) {
            return this.formattedRemarks;
        } else {
            return null;
        }
    }
}
