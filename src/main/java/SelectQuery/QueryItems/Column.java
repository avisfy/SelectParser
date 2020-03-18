package SelectQuery.QueryItems;

import java.util.Objects;

public class Column extends QueryItem {
    private final Table source;
    private final String name;
    private final String alias;
    private final boolean isAll;

    //column with alias
    public Column(String name1, String name2, String alias) {
        if (!name2.isEmpty()) {
            source = new Table(name1);
            name = name2;
        } else {
            name = name1;
            source = null;
        }
        this.alias = alias;
        isAll = false;
    }

    //column without alias
    public Column(String name1, String name2) {
        if (!name2.isEmpty()) {
            source = new Table(name1);
            name = name2;
        } else {
            name = name1;
            source = null;
        }
        this.alias = null;
        isAll = false;
    }

    //all case ("*")
    public Column() {
        isAll = true;
        name = null;
        source = null;
        alias = null;
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
