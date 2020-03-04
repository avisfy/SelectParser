package Query.QueryItems;

public class Sort {
    static final int ASC = 1;
    static final int DESC = -1;
    ColumnItem column;
    int sortType;

    Sort(ColumnItem col, int type) {
        this.column = col;
        this.sortType = type;
    }

    public String getString() {
        if (sortType == 1) {
            return "Sort: " + column.getString() + "ascending";
        } else if (sortType == -1) {
            return "Sort: " + column.getString() + "descending";
        } else
            return "";
    }
}
