package Query.QueryItems;

import Query.QueryException;

public class Join {
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

    private Table rTable = null;
    private JoinType join = null;
    private ColumnItem rTableCol = null;
    private ColumnItem lTableCol = null;
    private static final String JOIN_EXCEPTION = "JOIN_EXCEPTION";

    //for implicit join
    public Join(JoinType type, Table table) throws QueryException {
        if (type != JoinType.IMPLICIT_JOIN){
            throw new QueryException(JOIN_EXCEPTION, "Attempt not implicitly join tables without columns");
        }
        rTable = table;
        join = type;
    }

    public Join(JoinType type, Table table, ColumnItem col1, ColumnItem col2) throws QueryException {
        if (type == JoinType.IMPLICIT_JOIN){
            throw new QueryException(JOIN_EXCEPTION, "Attempt implicitly join tables with columns");
        }
        rTable = table;
        join = type;
        rTableCol = col1;
        lTableCol = col2;
    }

    public String getString() {
        String joinStr;
        switch (join) {
            case INNER_JOIN:
                joinStr = "INNER JOIN";
                break;
            case LEFT_JOIN:
                joinStr = "LEFT JOIN";
                break;
            case RIGHT_JOIN:
                joinStr = "RIGHT JOIN";
                break;
            case FULL_JOIN:
                joinStr = "FULL JOIN";
                break;
            case IMPLICIT_JOIN:
                joinStr = "";
                return "Join: " + " " + rTable + " " + joinStr;
            default:
                return "";
        }
        return "Join: " + " " + rTable + " " + joinStr;
    }

}


