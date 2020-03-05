package Query.QueryItems;

import Query.Query;

public class Table {
    private String name;
    private String alias =  "";
    private Query subQuery = null;

    public Table(String name) {
        this.name = name;
    }

    public Table(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public Table(Query q) {
        this.subQuery = q;
    }

    public String getString() {
        return "Table: " + name + alias;
    }
}
