package exel.eventsys.events;

import exel.engine.spreadsheet.api.ReadOnlySheet;

public class SheetDisplayRefactorEvent {
    private final ReadOnlySheet sheet;

    public SheetDisplayRefactorEvent(ReadOnlySheet sheet) {
        this.sheet = sheet;
    }

    public ReadOnlySheet getSheet() {
        return sheet;
    }
}
