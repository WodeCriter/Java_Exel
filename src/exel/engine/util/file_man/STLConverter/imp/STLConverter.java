package exel.engine.util.file_man.STLConverter.imp;

import exel.engine.spreadsheet.coordinate.Coordinate;
import exel.engine.spreadsheet.imp.SheetImp;
import exel.engine.spreadsheet.range.Range;
import exel.engine.util.jaxb.classes.*;
import exel.engine.spreadsheet.cell.api.Cell;
import exel.engine.spreadsheet.api.Sheet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class STLConverter {

    // Convert from project class to STL class
    /**
     * Converts a project Sheet object to an STLSheet object.
     *
     * @param sheet the Sheet to convert
     * @return the converted STLSheet
     */
    public static STLSheet toSTLSheet(Sheet sheet) {
        STLSheet stlSheet = new STLSheet();
        stlSheet.setName(sheet.getName());

        // Initialize and set up the layout
        STLLayout stlLayout = createSTLLayout(sheet);
        stlSheet.setSTLLayout(stlLayout);

        // Convert and set cells
        STLCells stlCells = convertCells(sheet.getCells());
        stlSheet.setSTLCells(stlCells);

        return stlSheet;
    }

    /**
     * Creates and configures the STLLayout based on the Sheet.
     *
     * @param sheet the source Sheet
     * @return the configured STLLayout
     */
    private static STLLayout createSTLLayout(Sheet sheet) {
        STLSize stlSize = new STLSize();
        stlSize.setColumnWidthUnits(sheet.getCellWidth());
        stlSize.setRowsHeightUnits(sheet.getCellHeight());

        STLLayout stlLayout = new STLLayout();
        stlLayout.setSTLSize(stlSize);
        stlLayout.setRows(sheet.getNumOfRows());
        stlLayout.setColumns(sheet.getNumOfCols());

        return stlLayout;
    }

    /**
     * Converts a list of Cell objects to STLCells.
     *
     * @param cells the list of Cells to convert
     * @return the STLCells containing converted STLCell objects
     */
    private static STLCells convertCells(List<Cell> cells) {
        STLCells stlCells = new STLCells();
        List<STLCell> convertedCells = cells.stream()
                .map(STLConverter::convertCell)
                .collect(Collectors.toList());
        stlCells.getSTLCell().addAll(convertedCells);
        return stlCells;
    }

    /**
     * Converts a single Cell to an STLCell.
     *
     * @param cell the Cell to convert
     * @return the converted STLCell
     */
    private static STLCell convertCell(Cell cell) {
        STLCell stlCell = new STLCell();
        Coordinate coordinate = cell.getCoordinate();

        stlCell.setColumn(coordinate.getCol());
        stlCell.setRow(coordinate.getRow());
        stlCell.setSTLOriginalValue(cell.getOriginalValue());

        return stlCell;
    }

    // Convert from STL class to project class
    public static Sheet fromSTLSheet(STLSheet stlSheet) {
        // Derive values from STLSheet
        int cellHeight = stlSheet.getSTLLayout().getSTLSize().getRowsHeightUnits();
        int cellWidth = stlSheet.getSTLLayout().getSTLSize().getColumnWidthUnits();  // Default or derived value
        int numOfCols = stlSheet.getSTLLayout().getColumns();
        int numOfRows = stlSheet.getSTLLayout().getRows();
        String sheetName = stlSheet.getName();
        List<STLRange> stlRanges = stlSheet.getSTLRanges().getSTLRange();

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

        for (STLRange stlRange : stlRanges) {
            STLBoundaries bounds = stlRange.getSTLBoundaries();
            Range range = new Range(new Coordinate(bounds.getFrom()), new Coordinate(bounds.getTo()), sheet );
            sheet.addRange(stlRange.getName(),range );
        }
        sheet.rebase();
        return sheet;
    }
}
