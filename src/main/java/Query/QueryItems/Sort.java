package Query.QueryItems;

public class Sort {
    private static final int ASC = 1;
    private static final int DESC = -1;
    Column column;
    int sortType;

    public Sort(Column col) {
        this.column = col;
        sortType = ASC;  //default
    }

    public void setAsc() {
        this.sortType = ASC;
    }

    public void setDesc() {
        this.sortType = DESC;
    }

    public String print(String pad) {
        pad = pad + "\t";
        if (sortType == 1) {
            return column.print(pad) + "\n" + pad + "ascending";
        } else if (sortType == -1) {
            return column.print(pad) + "\n" + pad + "descending";
        } else
            return "";
    }
}
