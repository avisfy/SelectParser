package SelectQuery.QueryItems;

import SelectQuery.QueryException;

public class Join extends QueryItem {
    public enum JoinType {
        INNER_JOIN(0),
        LEFT_JOIN(1),
        RIGHT_JOIN(2),
        FULL_JOIN(3),
        IMPLICIT_JOIN(4);

        private int joinType;

        JoinType(int join) {
            this.joinType = join;
        }
    }

    private static final String JOIN_EXCEPTION = "JOIN_EXCEPTION";
    private final Table rTable;
    private final JoinType join;
    private final Column lTableCol;
    private final Column rTableCol;

    //for implicit join
    public Join(JoinType type, Table table) throws QueryException {
        if (type != JoinType.IMPLICIT_JOIN) {
            throw new QueryException(JOIN_EXCEPTION, "Attempt not implicitly join tables without columns");
        }
        lTableCol = null;
        rTableCol = null;
        rTable = table;
        join = type;
    }

    //for not implicit join
    public Join(JoinType type, Table table, Column col1, Column col2) throws QueryException {
        if (type == JoinType.IMPLICIT_JOIN) {
            throw new QueryException(JOIN_EXCEPTION, "Attempt implicitly join tables with columns");
        }
        rTable = table;
        join = type;
        lTableCol = col1;
        rTableCol = col2;
    }

    public String print(String pad) {
        String res = pad + "join: " + "\n";
        pad = pad + "\t";
        res = res + rTable.print(pad) + "\n";
        res = res + pad + "type: " + join.name();
        if (join != JoinType.IMPLICIT_JOIN) {
            res = res + "\n" + pad + "on:\n" + lTableCol.print(pad + "\t") + "\n";
            res = res + rTableCol.print(pad + "\t");
        }
        return res;
    }

}


