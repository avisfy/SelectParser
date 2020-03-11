package Query.QueryItems;


public class FunctionColumn extends QueryItem {
    //aggregate functions
    public enum Functions {
        MIN, MAX, AVG, COUNT, SUM
    }

    private Column column;
    private Functions function = null;
    private String alias;

    //FUNCT(column)
    public FunctionColumn(Functions type, Column column, String alias) {
        this.function = type;
        this.column = column;
        this.alias = alias;
    }

    public FunctionColumn(Functions type, Column column) {
        this.function = type;
        this.column = column;
    }

    public String print(String pad) {
        String res = pad + "col:\n";
        pad = pad + "\t";
        res = res + pad + "fun: " + function.name() + "\n" + column.print(pad);
        if (alias != null) {
            res = res + "\n" + pad + "alias: " + alias;
        }
        return res;
    }

}

