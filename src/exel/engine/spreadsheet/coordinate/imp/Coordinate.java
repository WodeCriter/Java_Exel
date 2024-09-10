package exel.engine.spreadsheet.coordinate.imp;

import java.util.List;

public class Coordinate
{
    private String row;
    private String col;

    public Coordinate(String row, String col)
    {
        this.row = row;
        this.col = col;
    }

    public Coordinate(String coordinate)
    {
        char[] charArray = coordinate.toCharArray();

        int separator = 0;
        while (Character.isLetter(charArray[separator]))
            separator++;
        //Now separator is the index where the digit sequence begins

        col = coordinate.substring(0, separator);
        row = coordinate.substring(separator);
    }

    public String getCol()
    {
        return col;
    }

    public String getRow()
    {
        return row;
    }

    public void setCol(String col)
    {
        this.col = col;
    }

    public void setRow(String row)
    {
        this.row = row;
    }
}
