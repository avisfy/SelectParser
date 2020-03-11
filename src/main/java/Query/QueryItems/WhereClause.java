package Query.QueryItems;

public class WhereClause extends QueryItem{
    public enum OperatorType {
        EQUAL("="),
        NOT_EQUAL("<>"),
        MORE(">"),
        LESS("<"),
        MORE_OR_EQUAL(">="),
        LESS_OR_EQUAL("<="),
        IN("IN"),
        NOT_IN("NOT IN"),
        LIKE("LIKE");

        private String operator;

        OperatorType(String operator) {
            this.operator = operator;
        }
    }

    private Column column;
    private OperatorType operator;
    private QueryItem item;

    public WhereClause(Column col1, OperatorType operator, QueryItem col2) {
        column = col1;
        this.operator = operator;
        item = col2;
    }

    public String print(String pad) {
        String newpad =  pad + "\t";
        return newpad + "col1: " + column.print(newpad) + " \n" + newpad + "operator: " + operator.name() + newpad + "col1: " + item.print(newpad);
    }
}



