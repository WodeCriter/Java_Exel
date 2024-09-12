package exel.engine.spreadsheet.coordinate.imp;

import java.util.List;
import java.util.Objects;

public class Coordinate
{
    private String col;
    private int row;

    public Coordinate(String col, int row)
    {
        this.col = col;
        this.row = row;
    }

    public Coordinate(String coordinate)
    {
        if (!isStringACellCoordinate(coordinate))
            throw new IllegalArgumentException("The given input is not a valid cell coordinate");

        char[] charArray = coordinate.toCharArray();
        int separator = 0;
        while (Character.isLetter(charArray[separator]))
            separator++;
        //Now separator is the index where the digit sequence begins

        col = coordinate.substring(0, separator);
        row = Integer.parseInt(coordinate.substring(separator));
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

    public static Boolean isStringACellCoordinate(String input)
    {
        if (input == null || input.isEmpty()) {
            return false;
        }

        input = input.trim();
        int length = input.length();
        int i = 0;

        // Check for the presence of at least one letter at the start
        while (i < length && Character.isLetter(input.charAt(i))) {
            i++;
        }

        // There should be at least one letter and one digit
        if (i == 0 || i == length) {
            return false;
        }

        // Check that the rest of the string is digits
        while (i < length && Character.isDigit(input.charAt(i))) {
            i++;
        }

        // If we've parsed through all characters, it's a valid coordinate
        return i == length;
    }

    public String getCol()
    {
        return col;
    }

    public int getRow()
    {
        return row;
    }

    public void setCol(String col)
    {
        this.col = col;
    }

    public void setRow(int row)
    {
        this.row = row;
    }
}
