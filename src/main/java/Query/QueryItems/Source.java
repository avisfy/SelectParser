package Query.QueryItems;

import Query.Query;
import Query.QueryException;

public class Source {
    private Table singleTable = null;
    private Join joinedTable = null;
    private Query subQuery = null;

    public Source(Table table) {
        this.singleTable = table;
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
            if (singleTable != null) {
                resString =  "Source: " + singleTable.getString();
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
