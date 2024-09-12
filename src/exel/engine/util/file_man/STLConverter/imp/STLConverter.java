package exel.engine.util.file_man.STLConverter.imp;

import exel.engine.spreadsheet.coordinate.imp.Coordinate;
import exel.engine.spreadsheet.imp.SheetImp;
import exel.engine.util.jaxb.classes.*;
import exel.engine.spreadsheet.cell.api.Cell;
import exel.engine.spreadsheet.api.Sheet;

public class STLConverter {

    // Convert from project class to STL class
    public static STLSheet toSTLSheet(Sheet sheet) {
        STLSheet stlSheet = new STLSheet();
        STLCells stlCells = new STLCells();
        STLSize stlSize = new STLSize();
        STLLayout stlLayout = new STLLayout();

        //set the size of the sheet
        stlSize.setColumnWidthUnits(sheet.getCellWidth());
        stlSize.setRowsHeightUnits(sheet.getCellHeight());

        //initialize the layout
        stlLayout.setSTLSize(stlSize);
        stlLayout.setRows(sheet.getNumOfRows());
        stlLayout.setColumns(sheet.getNumOfCols());
        stlSheet.setSTLLayout(stlLayout);
        stlSheet.setName(sheet.getName());

        //create stlCell list from a list of cells
        for (Cell cell : sheet.getCells()) {
            STLCell stlCell = new STLCell();

            // Extracting coordinate data
            Coordinate coordinate = cell.getCoordinate();

            // Separate the column letters and row numbers based on the index found
            String column = coordinate.getCol(); // Column letters
            int row = coordinate.getRow(); // Row numbers

            // Set properties from Cell to STLCell
            stlCell.setSTLOriginalValue(cell.getOriginalValue());
            stlCell.setRow(row);
            stlCell.setColumn(column);

            // Add the constructed STLCell to the list of STLCells
            stlCells.getSTLCell().add(stlCell);
        }
        stlSheet.setSTLCells(stlCells);
        return stlSheet;
    }

    // Convert from STL class to project class
    public static Sheet fromSTLSheet(STLSheet stlSheet) {
        // Derive values from STLSheet
        int cellHeight = stlSheet.getSTLLayout().getSTLSize().getRowsHeightUnits();
        int cellWidth = stlSheet.getSTLLayout().getSTLSize().getColumnWidthUnits();  // Default or derived value
        int numOfCols = stlSheet.getSTLLayout().getColumns();
        int numOfRows = stlSheet.getSTLLayout().getRows();
        String sheetName = stlSheet.getName();

        // Create a new SheetImp instance with derived or default values
        SheetImp sheet = new SheetImp(cellHeight, cellWidth, numOfCols, numOfRows, sheetName);

        // Loop through each STLCell in the STLCells of the STLSheet and set them inside the sheet object
        for (STLCell stlCell : stlSheet.getSTLCells().getSTLCell()) {
            // get values for each
            String coordinate = stlCell.getColumn() + stlCell.getRow();
            String originalVal = stlCell.getSTLOriginalValue();
            // Add cell to the sheet
            sheet.setCell(coordinate, originalVal);
        }
        sheet.rebase();
        return sheet;
    }
}
