package SelectQuery.QueryItems;


public class FunctionColumn extends QueryItem {
    //aggregate functions
    public enum Functions {
        MIN, MAX, AVG, COUNT, SUM
    }

    private final Column column;
    private final Functions function;
    private final String alias;

    //FUNCT(column) AS alias
    public FunctionColumn(Functions type, Column column, String alias) {
        this.function = type;
        this.column = column;
        this.alias = alias;
    }

    //FUNCT(column)
    public FunctionColumn(Functions type, Column column) {
        this.function = type;
        this.column = column;
        alias = null;
    }

    public String print(String pad) {
        String res = pad + "funcol:\n";
        pad = pad + "\t";
        res = res + pad + "fun: " + function.name() + "\n" + column.print(pad);
        if (alias != null) {
            res = res + "\n" + pad + "alias: " + alias;
        }
        return res;
    }

}

