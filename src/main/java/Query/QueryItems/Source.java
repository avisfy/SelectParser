package Query.QueryItems;

import Query.Query;
import Query.QueryException;

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
        String resString = "";
        try {
            if (table != null) {
                resString =  "Source: " + table;
            } else if (joinedTable != null) {
                resString =  "Source: " + joinedTable.getString();
            } else if (subQuery != null) {
                resString =  "Source: " + subQuery.getString();
            }
        } catch (QueryException e) {
            e.getError();
            resString = "";
        }
        return resString;
    }
}
