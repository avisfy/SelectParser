package SelectQuery.QueryItems;

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
        String newPad = pad + "\t";
        String res = pad + "col:\n";
        if (isAll)
            return res + newPad + "*";
        if (source != null) {
            res = res + source.print(newPad) + "\n";
        }
        res = res + newPad + "colname: " + name;
        if (alias != null) {
            res = res + "\n" + newPad + "alias: " + alias;
        }
        return res;
    }
}
