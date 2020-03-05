package Query;

import Query.QueryItems.ColumnItem;
import Query.QueryItems.Join;
import Query.QueryItems.Source;
import Query.QueryItems.Table;

import java.util.ArrayList;
import java.util.List;

public class LexParser {
    private String input;
    private ArrayList<String> lexemes;
    private StringBuilder buf;
    private int pos;
    private static final String LEXEMES_EXCEPTION = "LEXEMES_PARSER_EXCEPTION";

    public LexParser(String strInput) {
        input = strInput;
        pos = 0;
        buf = new StringBuilder("");
        lexemes = new ArrayList<>();

        while ((pos < input.length()) && (input.charAt(pos) != ';')) {
            if (!Character.isWhitespace(input.charAt(pos))) {
                if ((input.charAt(pos) == '(') || (input.charAt(pos) == ')') || (input.charAt(pos) == ',') || (input.charAt(pos) == '.')) {
                    //if buffer contains lexemes, add it
                    addLexeme();
                    lexemes.add(String.valueOf(input.charAt(pos)));
                } else {
                    buf.append(input.charAt(pos));
                }
            } else {
                addLexeme();
            }
            pos++;
        }
        //after end of cycle buffer contains not added lexeme, don't lose it
        addLexeme();
        pos = 0;
    }


    public void parseQuery(Query q) {
        String lexeme;
        try {
            while (!((lexeme = nextLexeme()).equals(";") || lexeme.equals(")"))) {
                if (lexeme.equals("select")) {
                    q.setSelectItems(parseSelect());
                }
                if (lexeme.equals("from")) {
                    q.setFromSources(parseFrom());
                    System.out.println("From");
                }
                nextLexeme();
            }
        } catch (QueryException e) {
            e.getError();
            return;
        }
    }

    //return true if end of lexemes array
    private ArrayList<ColumnItem> parseSelect() {
        ArrayList<ColumnItem> selectItems = null;
        try {
            String lexeme;
            ColumnItem column = null;
            ColumnItem.Functions fun;
            selectItems = new ArrayList<>();
            do {
                lexeme = nextLexeme();
                switch (lexeme) {
                    case "*":
                        column = new ColumnItem();
                        selectItems.add(column);
                        return selectItems;
                    case "max":
                        //"("
                        fun = ColumnItem.Functions.MAX;
                        column = parseAggregateFunct(fun);
                        break;
                    case "min":
                        //"("
                        fun = ColumnItem.Functions.MIN;
                        column = parseAggregateFunct(fun);
                        break;
                    case "avg":
                        //"("
                        fun = ColumnItem.Functions.AVG;
                        column = parseAggregateFunct(fun);
                        break;
                    case "sum":
                        //"("
                        fun = ColumnItem.Functions.SUM;
                        column = parseAggregateFunct(fun);
                        break;
                    case "count":
                        //"("
                        fun = ColumnItem.Functions.COUNT;
                        column = parseAggregateFunct(fun);
                        break;
                    //subquery
                    case "(":
                        lexeme = nextLexeme();
                        if (lexeme.equals("select")) {
                            pos--;
                            Query subQuery = new Query();
                            parseQuery(subQuery);
                            column = new ColumnItem(subQuery);
                            nextLexeme();
                        } else {
                            column = null;
                        }
                        break;
                    default:
                        column = parseColumn(lexeme);
                }
                if (column != null) {
                    selectItems.add(column);
                }
            } while ((lexeme = nextLexeme()).equals(","));
        } catch (QueryException e) {
            e.getError();
        }
        return selectItems;
    }


    private List<Source> parseFrom() {
        List<Source> sources = null;
        try {
            Table table1 = parseTable();
            sources = new ArrayList<Source>();
            sources.add(new Source(table1));
            String lexeme1;
            do{
                lexeme1 = nextLexeme();
                Join.JoinType type;
                switch (lexeme1) {
                    case "inner":
                        type = Join.JoinType.INNER_JOIN;
                        parseJoin(type);
                        break;
                    case "left":
                        type = Join.JoinType.LEFT_JOIN;
                        parseJoin(type);
                        break;
                    case "right":
                        type = Join.JoinType.RIGHT_JOIN;
                        parseJoin(type);
                        break;
                    case "full":
                        type = Join.JoinType.FULL_JOIN;
                        parseJoin(type);
                        break;
                    case ",":
                        type = Join.JoinType.IMPLICIT_JOIN;
                        parseJoin(type);
                        break;
                }
            } while(!(lexeme1 = nextLexeme()).equals("where"));
            pos--;
        } catch (QueryException e) {
            e.getError();
        }
        return sources;
    }


    private Join parseJoin(Join.JoinType type) {
        Join join = null;
        Table joinedTable = null;
        try {
            if (type == Join.JoinType.IMPLICIT_JOIN) {
                joinedTable = parseTable();
                join = new Join(type, joinedTable);
                return join;
            }
            //skip "join"
            nextLexeme();
            joinedTable = parseTable();
            //skip "on"
            nextLexeme();
            ColumnItem column1 = parseColumn(nextLexeme());
            //skip "="
            nextLexeme();
            ColumnItem column2 = parseColumn(nextLexeme());
            join =  new Join(type, joinedTable, column1, column2);
        } catch (QueryException e) {
            e.getError();
        }
        return join;
    }


    private Table parseTable() {
        Table table = null;
        try {
            String lexeme1 = nextLexeme();
            if (lexeme1.equals("(")) {
                if (nextLexeme().equals("select")) {
                    pos--;
                    Query subQuery = new Query();
                    parseQuery(subQuery);
                    table =  new Table(subQuery);
                    return table;
                }
            }
            String lexeme2 = nextLexeme();
            if (lexeme2.equals("as")) {
                lexeme2 = nextLexeme();
                table =  new Table( lexeme1, lexeme2);
            } else {
                pos--;
                table =  new Table( lexeme1);
            }
        } catch (QueryException e) {
            e.getError();
        }
        return table;
    }


    private ColumnItem parseColumn(String lexeme) {
        ColumnItem column = null;
        try {
            String str1 = lexeme;
            String str2 = nextLexeme();
            if (str2.equals(".")) {
                //means that str1 - table name, str2 - column name
                str2 = nextLexeme();
            } else {
                str2 = "";
                pos--;
            }
            String str3 = nextLexeme();
            if (str3.equals("as")) {
                //means column with alias
                str3 = nextLexeme();

            } else {
                pos--;
                str3 = "";
            }
            column = new ColumnItem(str1, str2, str3);
        } catch (QueryException e) {
            e.getError();
        }
        return column;
    }

    //column in aggregate function
    private ColumnItem parseAggregateFunct(ColumnItem.Functions fun) {
        ColumnItem column = null;
        try {
            nextLexeme();
            String str1 = nextLexeme();
            String str2 = nextLexeme();
            if (str2.equals(".")) {
                //means that str1 - table; str2 - column
                str2 = nextLexeme();
                nextLexeme();
            } else {
                //means that str1 - column
                str2 = "";
            }
            String str3 = nextLexeme();
            //alias
            if (str3.equals("as")) {
                //have alias
                str3 = nextLexeme();
            } else {
                str3 = "";
                pos--;
            }
            column = new ColumnItem(fun, str1, str2, str3);
        } catch (QueryException e) {
            e.getError();
        }
        return column;
    }


    private String nextLexeme() throws QueryException {
        if (pos < lexemes.size()) {
            return lexemes.get(pos++);
        } else {
            throw new QueryException(LEXEMES_EXCEPTION, "End of lexemes array");
        }
    }


    public void printLexemes() {
        for (String lexeme : lexemes) {
            System.out.println(lexeme);
        }
    }


    private void addLexeme() {
        if (buf.length() != 0) {
            lexemes.add(buf.toString());
            buf.delete(0, buf.length());
        }
    }
}
