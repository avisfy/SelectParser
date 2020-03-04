package Query.QueryItems;

import Query.Query;
import Query.QueryException;

public class Join {
    enum JoinType {
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

    private String rTable = null;
    private String lTable = null;
    private JoinType join = null;
    private ColumnItem rTableCol = null;
    private ColumnItem lTableCol = null;
    private Query subQuery = null;
    private static final String JOIN_EXCEPTION = "JOIN_EXCEPTION";

    public Join(String leftTable, JoinType join, String rightTable, ColumnItem leftTableCol, ColumnItem rightTableCol) {
        lTable = leftTable;
        rTable = rightTable;
        this.join = join;
        if (join != JoinType.IMPLICIT_JOIN) {
            lTableCol = leftTableCol;
            rTableCol = rightTableCol;
        }
    }

    public Join(String leftTable, JoinType join, String rightTable) throws QueryException {
        if (join == JoinType.IMPLICIT_JOIN) {
            lTable = leftTable;
            rTable = rightTable;
            this.join = join;
        } else {
            throw new QueryException(JOIN_EXCEPTION, "Not implicit join need columns connection");
        }
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
                return "Join: " + lTable + " " + " " + rTable + " " + joinStr;
            default:
                return "";
        }
        return "Join: " + lTable + " " + " " + rTable + " " + joinStr;
    }

}


