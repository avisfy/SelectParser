package SelectQuery.QueryItems;

public class Clause extends QueryItem {
    public enum OperatorType {
        EQUAL,
        NOT_EQUAL,
        MORE,
        LESS,
        MORE_OR_EQUAL,
        LESS_OR_EQUAL,
        IN,
        NOT_IN,
        LIKE;
    }

    //connection type for next WHERE clause
    public enum ConnectionType {
        AND,
        OR
    }

    private final QueryItem column;
    private final OperatorType operator;
    private final QueryItem item;
    private ConnectionType next = null;

    public Clause(QueryItem col1, OperatorType operator, QueryItem col2) {
        column = col1;
        this.operator = operator;
        item = col2;
    }

    public void setNext(ConnectionType next) {
        this.next = next;
    }

    public String print(String pad) {
        String newpad = pad + "\t";
        String res = column.print(newpad) + " \n" + newpad + "operator: " + operator.name() + "\n" + item.print(newpad);
        if (next != null) {
            res = res + "\n" + pad + next.name();
        }
        return res;
    }
}




