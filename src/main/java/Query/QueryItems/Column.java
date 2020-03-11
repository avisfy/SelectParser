package Query.QueryItems;

public class Column extends QueryItem {
    private Table source = null;
    private String name = null;
    private String alias = null;
    private boolean isAll = false;

    //column with alias
    public Column(String name1, String name2, String alias) {
        if (!name2.isEmpty()) {
            this.source = new Table(name1);
            this.name = name2;
        } else {
            this.name = name1;
        }
        this.alias = alias;
        isAll = false;
    }

    //column without alias
    public Column(String name1, String name2) {
        if (!name2.isEmpty()) {
            this.source = new Table(name1);
            this.name = name2;
        } else {
            this.name = name1;
        }
        isAll = false;
    }

    //all case ("*")
    public Column() {
        isAll = true;
    }

    public String print(String pad) {
        String res = pad + "col:\n";
        pad = pad + "\t";
        if (isAll)
            return res + pad + "*";
        if (source != null) {
            res = res + source.print(pad) + "\n";
        }
        res = res + pad + "name: " + name;
        if (alias != null) {
            res = res + "\n" + pad + "alias: " + alias;
        }
        return res;
    }
}
