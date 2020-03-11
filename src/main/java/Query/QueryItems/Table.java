package Query.QueryItems;


public class Table extends QueryItem {
    private String name;
    private String alias = null;

    public Table(String name) {
        this.name = name;
    }

    public Table(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public String print(String pad) {
        String res = pad + "table:\n";
        pad = pad + "\t";
        if (alias == null) {
            return res + pad + "name: " + name;
        } else {
            return res + pad + "name: " + name + "\n" + pad + "alias: " + alias;
        }
    }
}
