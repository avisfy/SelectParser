package Query;

import Query.QueryItems.*;

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
                    System.out.println("Select");
                    lexeme = nextLexeme();
                }
                System.out.println(pos);
                if (lexeme.equals("from")) {
                    q.setFromSources(parseFrom());
                    System.out.println("From");
                    lexeme = nextLexeme();
                }
            }
        } catch (QueryException e) {
            e.getError();
            return;
        }
    }

    private ArrayList<QueryItem> parseSelect() {
        ArrayList<QueryItem> selectItems = null;
        try {
            String lexeme;
            QueryItem column = null;
            FunctionColumn.Functions fun;
            selectItems = new ArrayList<>();
            do {
                lexeme = nextLexeme();
                switch (lexeme) {
                    case "*":
                        column = new Column();
                        selectItems.add(column);
                        return selectItems;
                    case "max":
                        fun = FunctionColumn.Functions.MAX;
                        column = parseAggregateFunct(fun);
                        break;
                    case "min":
                        fun = FunctionColumn.Functions.MIN;
                        column = parseAggregateFunct(fun);
                        break;
                    case "avg":
                        fun = FunctionColumn.Functions.AVG;
                        column = parseAggregateFunct(fun);
                        break;
                    case "sum":
                        fun = FunctionColumn.Functions.SUM;
                        column = parseAggregateFunct(fun);
                        break;
                    case "count":
                        fun = FunctionColumn.Functions.COUNT;
                        column = parseAggregateFunct(fun);
                        break;
                    //subquery
                    case "(":
                        column = parseSubq();
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
        pos--;
        return selectItems;
    }



    private List<QueryItem> parseFrom() {
        List<QueryItem> sources = null;
        try {
            String lexeme1 = nextLexeme();
            QueryItem table;
            if (lexeme1.equals("(")) {
                table = parseSubq();
            } else  {
                pos--;
                table = parseTable();
            }
            sources = new ArrayList<>();
            sources.add(table);
            lexeme1 = nextLexeme();
            while(lexeme1.equals("inner") || lexeme1.equals("left") || lexeme1.equals("right") || lexeme1.equals("full") || lexeme1.equals(",")){
                table = null;
                Join.JoinType type;
                switch (lexeme1) {
                    case "inner":
                        type = Join.JoinType.INNER_JOIN;
                        table = parseJoin(type);
                        break;
                    case "left":
                        type = Join.JoinType.LEFT_JOIN;
                        table = parseJoin(type);
                        break;
                    case "right":
                        type = Join.JoinType.RIGHT_JOIN;
                        table = parseJoin(type);
                        break;
                    case "full":
                        type = Join.JoinType.FULL_JOIN;
                        table = parseJoin(type);
                        break;
                    case ",":
                        type = Join.JoinType.IMPLICIT_JOIN;
                        if (nextLexeme().equals("(")) {
                            table = parseSubq();
                        } else {
                            table = parseJoin(type);
                        }
                        break;
                }
                if (table != null) {
                    sources.add(table);
                }
                lexeme1 = nextLexeme();
            }
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
            Column column1 = parseColumn(nextLexeme());
            //skip "="
            nextLexeme();
            Column column2 = parseColumn(nextLexeme());
            join =  new Join(type, joinedTable, column1, column2);
        } catch (QueryException e) {
            e.getError();
        }
        return join;
    }

    private Subquery parseSubq() {
        Query q;
        Subquery subQuery = null;
        try {
            String lexeme = nextLexeme();
            if (lexeme.equals("select")) {
                pos--;
                q = new Query();
                parseQuery(q);
                lexeme = nextLexeme();
                if (lexeme.equals("as")) {
                    //alias
                    lexeme =  nextLexeme();
                    subQuery = new Subquery(q, lexeme);
                    nextLexeme();
                } else {
                    pos--;
                    subQuery = new Subquery(q);
                }
            }
        } catch (QueryException e) {
            e.getError();
            subQuery = null;
        }
        return subQuery;
    }

    private Table parseTable() {
        Table table = null;
        try {
            String lexeme1 = nextLexeme();
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
            table = null;
        }
        return table;
    }


    private Column parseColumn(String lexeme) {
        Column column = null;
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
                column = new Column(str1, str2, str3);
            } else {
                pos--;
                column = new Column(str1, str2);
            }
        } catch (QueryException e) {
            e.getError();
            column =  null;
        }
        return column;
    }

    //column in aggregate function
    private FunctionColumn parseAggregateFunct(FunctionColumn.Functions fun) {
        FunctionColumn column = null;
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
            Column col =  new Column(str1, str2);
            String str3 = nextLexeme();
            //alias
            if (str3.equals("as")) {
                //have alias
                str3 = nextLexeme();
                column = new FunctionColumn(fun, col, str3);
            } else {
                pos--;
                column = new FunctionColumn(fun, col);
            }
        } catch (QueryException e) {
            e.getError();
            column = null;
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
