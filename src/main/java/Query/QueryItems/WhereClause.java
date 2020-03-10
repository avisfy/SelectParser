package Query.QueryItems;

import Query.QueryException;

public class WhereClause {
    enum OperatorType {
        EQUAL("="),
        NOT_EQUAL("<>"),
        MORE(">"),
        LESS("<"),
        MORE_OR_EQUAL(">="),
        LESS_OR_EQUAL("<=");

        private String operator;

        OperatorType(String operator) {
            this.operator = operator;
        }
    }

    private Column column1;
    private OperatorType operator;
    private QueryItem item = null;

    WhereClause(Column col1, OperatorType operator, QueryItem col2) {
        column1 = col1;
        this.operator = operator;
        item = col2;
    }

    public String getString() throws QueryException {
        return "whereClause: " + column1.print(" ") + " " + operator.name() + " " + item.print(" ");
    }
}



