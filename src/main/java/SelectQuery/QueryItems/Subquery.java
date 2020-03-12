package SelectQuery.QueryItems;

import SelectQuery.SelectQuery;
import SelectQuery.QueryException;

public class Subquery extends QueryItem {
    private SelectQuery subquery;
    private String alias = null;

    public Subquery(SelectQuery subquery, String alias) {
        this.subquery = subquery;
        this.alias = alias;
    }

    public Subquery(SelectQuery subquery) {
        this.subquery = subquery;
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
