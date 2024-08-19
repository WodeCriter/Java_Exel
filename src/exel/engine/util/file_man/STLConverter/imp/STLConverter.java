package exel.engine.util.file_man.STLConverter.imp;

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

        //we need to initialize the layout first

        //create stlCell list from a list of cells
        for (Cell cell : sheet.getCells()) {
            STLCell stlCell = new STLCell();

            // Extracting coordinate data
            String coordinate = cell.getCoordinate();
            String[] parts = coordinate.split(","); // Assuming the format "row,column"
            int row = Integer.parseInt(parts[0].trim());
            String column = parts[1].trim();

            // Set properties from Cell to STLCell
            stlCell.setSTLOriginalValue(cell.getOriginalValue());
            stlCell.setRow(row);
            stlCell.setColumn(column);

            // Add the constructed STLCell to the list of STLCells
            stlCells.getSTLCell().add(stlCell);
        }
        stlSheet.setName(sheet.getName());
        stlSheet.setSTLCells(stlCells);
        return stlSheet;
    }

    // Convert from STL class to project class
    public static Sheet fromSTLSheet(STLSheet stlSheet) {
        Sheet sheet = new SheetImp();

        for (STLCell stlCell : stlSheet.getCells().getSTLCell()) {
            CellImp cell = new CellImp();
            // Set properties from STLCell to CellImp
            cell.setContent(stlCell.getContent());
            cell.setX(stlCell.getPositionX());
            cell.setY(stlCell.getPositionY());
            sheet.addCell(cell);
        }

        return sheet;
    }
