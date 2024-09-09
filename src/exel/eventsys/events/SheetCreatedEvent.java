package exel.eventsys.events;

public class SheetCreatedEvent {
    private String sheetName;
    private int cellHeight;
    private int cellWidth;
    private int numOfCols;
    private int numOfRows;

    public SheetCreatedEvent(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getSheetName() {
        return sheetName;
    }
}
