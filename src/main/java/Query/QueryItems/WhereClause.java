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

    private ColumnItem column1;
    private OperatorType operator;
    private ColumnItem columnItem2 = null;
    //private Query subQuery = null; //for IN and NOT IN
    private Integer numberInt  = null;
    private Float numberFloat  = null;

    WhereClause(ColumnItem col1, OperatorType operator, ColumnItem col2) {
        column1 = col1;
        this.operator = operator;
        columnItem2 = col2;
    }

    WhereClause(ColumnItem col1, OperatorType operator, int number) {
        column1 = col1;
        this.operator = operator;
        columnItem2 = null;
        numberInt = number;
        numberFloat = null;
    }

    WhereClause(ColumnItem col1, OperatorType operator, float number) {
        column1 = col1;
        this.operator = operator;
        columnItem2 = null;
        numberInt = null;
        numberFloat = number;
    }

    public String getString() throws QueryException {
        if (columnItem2 != null ) {
            return "WhereClause: " + column1.getString() + " " + operator.name() + " " + columnItem2.getString();
        } else if (numberInt != null) {
            return "WhereClause: " + column1.getString() + " " + operator.name() + " " + numberInt;
        } else if (numberFloat != null) {
            return "WhereClause: " + column1.getString() + " " + operator.name() + " " + numberFloat;
        } else {
            return "";
        }
    }
}



