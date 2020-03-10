package Query.QueryItems;

public class Sort {
    static final int ASC = 1;
    static final int DESC = -1;
    Column column;
    int sortType;

    Sort(Column col, int type) {
        this.column = col;
        this.sortType = type;
    }

    public String print(String pad) {
        if (sortType == 1) {
            return "sort: " + column.print(pad) + "ascending";
        } else if (sortType == -1) {
            return "sort: " + column.print(pad) + "descending";
        } else
            return "";
    }
}
