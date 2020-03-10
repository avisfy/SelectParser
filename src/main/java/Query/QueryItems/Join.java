package Query.QueryItems;

import Query.QueryException;

public class Join extends QueryItem{
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
    private Table rTable = null;
    private JoinType join = null;
    private Column rTableCol = null;
    private Column lTableCol = null;

    //for implicit join
    public Join(JoinType type, Table table) throws QueryException {
        if (type != JoinType.IMPLICIT_JOIN){
            throw new QueryException(JOIN_EXCEPTION, "Attempt not implicitly join tables without columns");
        }
        rTable = table;
        join = type;
    }

    public Join(JoinType type, Table table, Column col1, Column col2) throws QueryException {
        if (type == JoinType.IMPLICIT_JOIN){
            throw new QueryException(JOIN_EXCEPTION, "Attempt implicitly join tables with columns");
        }
        rTable = table;
        join = type;
        rTableCol = col1;
        lTableCol = col2;
    }

    public String print(String pad) {
        String res = pad + "join: " + "\n";
        pad = pad + "\t";
        res = res + rTable.print(pad) + "\n";
        res = res + pad + "type: " + join.name();
        if (join != JoinType.IMPLICIT_JOIN) {
            res = res + "\n" + pad + "on: " + lTableCol.print(pad)+ "\n";
            res = res + pad + "   " + lTableCol.print(pad)+ "\n";
        }
        return res;
    }

}


