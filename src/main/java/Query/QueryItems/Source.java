package Query.QueryItems;


import Query.Query;

public class Source {
    private String table = null;
    private Join joinedTable = null;
    private Query subQuery = null;
    private String alias = null;

    public Source(String table) {
        this.table = table;
    }

    public Source(Query subQuery) {
        this.subQuery = subQuery;
    }

    public Source(Join jTable) {
        joinedTable = jTable;
    }



    public String getString() {
        if (table != null) {
            return "Source: " + table;
        } else if (joinedTable != null) {
            return "Source: " + joinedTable.getString();
        } else if (subQuery != null) {
            return "Source: " + joinedTable.getString();
        } else {
            return "";
        }
    }
}
