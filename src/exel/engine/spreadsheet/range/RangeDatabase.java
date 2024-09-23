package exel.engine.spreadsheet.range;

import org.w3c.dom.ranges.RangeException;

import java.util.HashMap;
import java.util.Map;

public class RangeDatabase
{
    private static final Map<String, Range> ranges = new HashMap<String, Range>();

    public static void addRange(String rangeName, Range range)
    {
        if (isRangeInDatabase(rangeName))
        {
            try {removeRange(rangeName);}
            catch (Exception e) {throw new RuntimeException("Cannot replace range \"" + rangeName + "\" as it is currently used.");}
        }
        ranges.put(rangeName, range);
    }

    public static boolean isRangeInDatabase(String rangeName)
    {
        return ranges.containsKey(rangeName);
    }

    public static void removeRange(String rangeName)
    {
        if (getRange(rangeName).isRangeUsed())
            throw new RuntimeException("Cannot remove range \"" + rangeName + "\" as it is currently used.");
        ranges.remove(rangeName);
    }

    public static Range getRange(String rangeName)
    {
        if (!isRangeInDatabase(rangeName))
            throw new RuntimeException("The range \"" + rangeName + "\" does not exist.");
        return ranges.get(rangeName);
    }

    public static Range getRangeAndCountUse(String rangeName)
    {
        Range range = getRange(rangeName);
        range.countUseOfRange();
        return range;
    }
}
