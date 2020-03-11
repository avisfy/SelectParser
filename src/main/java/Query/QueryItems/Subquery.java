package Query.QueryItems;

import Query.Query;
import Query.QueryException;

public class Subquery extends QueryItem {
    private Query subquery;
    private String alias = null;

    public Subquery(Query subquery, String alias) {
        this.subquery = subquery;
        this.alias = alias;
    }

    public Subquery(Query subquery) {
        this.subquery = subquery;
    }

    public String print(String pad) {
        String res = pad + "sub:\n";
        pad = pad + "\t";
        try {
            res = res + subquery.print(pad) + "\n";
            if (alias != null) {
                res = res + pad + "alias: " + alias + "\n";
            }
        } catch (QueryException e) {
            e.getError();
        }
        return res;
    }
}
