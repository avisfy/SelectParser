package SelectQuery.QueryItems;

import SelectQuery.SelectQuery;
import SelectQuery.QueryException;

public class Subquery extends QueryItem {
    private final SelectQuery subquery;
    private final String alias;

    //(SELECT ... subquery)  AS alias
    public Subquery(SelectQuery subquery, String alias) {
        this.subquery = subquery;
        this.alias = alias;
    }

    //(SELECT ... subquery)
    public Subquery(SelectQuery subquery) {
        this.subquery = subquery;
        alias = null;
    }

    public String print(String pad) {
        String res = pad + "sub:\n";
        pad = pad + "\t";
        try {
            res = res + subquery.print(pad);
            if (alias != null) {
                res = res + "\n" + pad + "alias: " + alias;
            }
        } catch (QueryException e) {
            e.getError();
        }
        return res;
    }
}
