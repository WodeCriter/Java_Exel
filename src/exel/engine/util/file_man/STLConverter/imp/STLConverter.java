package exel.engine.util.file_man.STLConverter.imp;

import exel.engine.effectivevalue.api.EffectiveValue;
import exel.engine.spreadsheet.cell.imp.CellImp;
import exel.engine.spreadsheet.imp.SheetImp;
import exel.engine.util.jaxb.classes.*;
import exel.engine.spreadsheet.cell.api.Cell;
import exel.engine.spreadsheet.api.Sheet;

import java.util.ArrayList;
import java.util.List;

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
            String coordinate = cell.getCoordinate();
            int i = 0;
            // Find where the letters stop and the numbers start
            while (i < coordinate.length() && Character.isLetter(coordinate.charAt(i))) {
                i++;
            }

            // Separate the column letters and row numbers based on the index found
            String column = coordinate.substring(0, i).trim(); // Column letters
            int row = Integer.parseInt(coordinate.substring(i).trim()); // Row numbers

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
