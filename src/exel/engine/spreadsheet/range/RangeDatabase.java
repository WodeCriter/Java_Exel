package exel.engine.spreadsheet.range;

import org.w3c.dom.ranges.RangeException;

import java.util.HashMap;
import java.util.Map;

public class RangeDatabase
{
    private final Map<String, Range> ranges;

    public RangeDatabase()
    {
        ranges = new HashMap<>();
    }

    public void addRange(String rangeName, Range range)
    {
        if (isRangeInDatabase(rangeName))
        {
            try {removeRange(rangeName);}
            catch (Exception e) {throw new RuntimeException("Cannot replace range \"" + rangeName + "\" as it is currently used.");}
        }
        ranges.put(rangeName, range);
    }

    public boolean isRangeInDatabase(String rangeName)
    {
        return ranges.containsKey(rangeName);
    }

    public void removeRange(String rangeName)
    {
        if (getRange(rangeName).isRangeUsed())
            throw new RuntimeException("Cannot remove range \"" + rangeName + "\" as it is currently used.");
        ranges.remove(rangeName);
    }

    public Range getRange(String rangeName)
    {
        if (!isRangeInDatabase(rangeName))
            throw new RuntimeException("The range \"" + rangeName + "\" does not exist.");
        return ranges.get(rangeName);
    }

    public Range getRangeAndCountUse(String rangeName)
    {
        Range range = getRange(rangeName);
        range.countUseOfRange();
        return range;
    }
}
