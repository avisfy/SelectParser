package Query.QueryItems;

import Query.Query;

public class ColumnItem {
    private enum ItemTypes {
        ALL,                    // *
        COLUMN,                 //single column
        COLUMN_IN_FUNCTION,     //column in aggregate functions
        COLUMN_FROM_SUBQUERY    //columns as result of subquery
    }

    //aggregate functions
    public enum Functions {
        MIN, MAX, AVG, COUNT, SUM
    }

    private final ItemTypes type;
    private String name = ""; //not null if type == COLUMN, COLUMN_IN_FUNCTION
    private Source source = null; //not null if type == COLUMN, COLUMN_IN_FUNCTION, COLUMN_FROM_SUBQUERY
    private String alias = "";
    private Functions function = null;

    //FUNCT(table_name.column)
    public ColumnItem(Functions type, String name1, String name2, String alias) {
        this.function = type;
        if (!name1.isEmpty()) {
            this.source = new Source(name1);
            this.name = name2;
        } else {
            this.name = name1;
        }
        if (!alias.isEmpty()){
            this.alias = alias;
        }
        this.type = ItemTypes.COLUMN_IN_FUNCTION;
    }

    //subquery
    public ColumnItem(Query subQuery) {
        this.source = new Source(subQuery);
        this.type = ItemTypes.COLUMN_FROM_SUBQUERY;
    }

    //All (*)
    public ColumnItem() {
        this.type = ItemTypes.ALL;
    }

    //pure column
    public ColumnItem(String name1, String name2, String alias) {
        if (!name1.isEmpty()) {
            this.source = new Source(name1);
            this.name = name2;
        } else {
            this.name = name1;
        }
        if (!alias.isEmpty()){
            this.alias = alias;
        }
        this.type = ItemTypes.COLUMN;
    }


    public String getString() {
        String resStr = "";
        if (type == ItemTypes.ALL) {
            resStr = "*";
        } else if (type == ItemTypes.COLUMN) {
            if (source != null) {
                resStr = source.getString();
            }
            resStr = resStr + " "  + name;
            resStr = resStr + " " + alias;
        } else if(type == ItemTypes.COLUMN_IN_FUNCTION) {
            resStr = function.name() + "(";
            if (source != null) {
                resStr = resStr + source.getString() + " ";
            }
            resStr = resStr + name;
            resStr = resStr + ")" + " " + alias;
        } else if(type == ItemTypes.COLUMN_FROM_SUBQUERY) {
            resStr = source.getString();
        }
        return resStr;
    }

}

