package exel.engine.spreadsheet.range;

import java.util.HashMap;
import java.util.Map;

public class RangeDatabase
{
    private static final Map<String, Range> ranges = new HashMap<String, Range>();

    public static void addRange(String rangeName, Range range)
    {
        ranges.put(rangeName, range);
    }

    public static boolean isRangeInDatabase(String rangeName)
    {
        return ranges.containsKey(rangeName);
    }

    public static void removeRange(String rangeName)
    {
        //todo: Handle situation where range is currently used.
        ranges.remove(rangeName);
    }

    public static Range getRange(String rangeName)
    {
        return ranges.get(rangeName);
    }
}
