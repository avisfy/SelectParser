package SelectQuery.QueryItems;


public class Table extends QueryItem {
    private final String name;
    private final String alias;

    //table_name
    public Table(String name) {
        this.name = name;
        alias = null;
    }

    //table_name AS alias
    public Table(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public String print(String pad) {
        String res = pad + "tab: " + name;
        pad = pad + "\t";
        if (alias == null) {
            return res;
        } else {
            return res + "\n" + pad + "alias: " + alias;
        }
    }
}
