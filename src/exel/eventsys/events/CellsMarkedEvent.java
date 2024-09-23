package exel.eventsys.events;

import java.util.List;

public class CellsMarkedEvent
{
    private List<String> cellsMarkedCords;

    public CellsMarkedEvent(List<String> cellsMarkedCords)
    {
        this.cellsMarkedCords = cellsMarkedCords;
    }

    public List<String> getCellsMarkedCords()
    {
        return cellsMarkedCords;
    }
}
