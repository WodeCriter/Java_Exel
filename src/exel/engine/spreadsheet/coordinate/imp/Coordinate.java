package exel.engine.spreadsheet.coordinate.imp;

import java.util.List;
import java.util.Objects;

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

    @Override
    public String toString()
    {
        return col + row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Coordinate that = (Coordinate) o;

        if (!Objects.equals(row, that.row))
            return false;
        return Objects.equals(col, that.col);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
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
