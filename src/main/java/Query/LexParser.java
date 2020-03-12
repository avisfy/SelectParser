package Query;

import Query.QueryItems.*;

import java.util.ArrayList;
import java.util.List;

public class LexParser {
    private ArrayList<String> lexemes;
    private StringBuilder buf;
    private int pos;
    private static final String END_LEXEMES_EXCEPTION = "LEXEMES_PARSER_EXCEPTION";
    private static final String LEXEMES_EXCEPTION = "LEXEMES_PARSER_EXCEPTION";

    public LexParser(String input) {
        pos = 0;
        buf = new StringBuilder();
        lexemes = new ArrayList<>();

        while (pos < input.length()) {
            if (!Character.isWhitespace(input.charAt(pos))) {
                if ((input.charAt(pos) == '(') || (input.charAt(pos) == ')') || (input.charAt(pos) == '\'') || (input.charAt(pos) == ',') || (input.charAt(pos) == '.') || (input.charAt(pos) == ';')) {
                    //if buffer contains lexemes, add it
                    addLexeme();
                    lexemes.add(String.valueOf(input.charAt(pos)));
                } //parse operators like =, <=, <>, ...
                else if ((input.charAt(pos) == '<') || (input.charAt(pos) == '>') || (input.charAt(pos) == '=') || (input.charAt(pos) == '!')) {
                    addLexeme();
                    buf.append(input.charAt(pos));
                    pos++;
                    //if operator includes 2 characters
                    if ((input.charAt(pos) == '<') || (input.charAt(pos) == '>') || (input.charAt(pos) == '=')) {
                        buf.append(input.charAt(pos));
                    } else {
                        pos--;
                        addLexeme();
                    }
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

    public void parseQuery(Query q) throws QueryException {
        String lexeme;
        try {
            while (!((lexeme = nextLexeme()).equals(")") || lexeme.equals(";"))) {
                switch (lexeme) {
                    case "select":
                        q.setSelectItems(parseSelect());
                        break;
                    case "from":
                        q.setFromSources(parseFrom());
                        break;
                    case "where":
                        q.setWhereClauses(parseWhere());
                        break;
                    case "group":
                        if (nextLexeme().equals("by")) {
                            q.setGroupByColumns(parseGroupBy());
                        }
                        break;
                    case "order":
                        if (nextLexeme().equals("by")) {
                            q.setSortColumns(parseOrderBy());
                        }
                        break;
                    case "limit":
                        q.setLimit(parseLim());
                        break;
                    case "offset":
                        q.setOffset(parseLim());
                        break;
                }
            }
        } catch (QueryException e) {
            if (!e.getCode().equals(END_LEXEMES_EXCEPTION)) {
                throw e;
            }
        }
    }

    private ArrayList<QueryItem> parseSelect() throws QueryException {
        ArrayList<QueryItem> selectItems = null;
        try {
            String lexeme;
            QueryItem column;
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
                    case "'":
                        column = parsePrim();
                        break;
                    default:
                        //case primitive number
                        if (Character.isDigit(lexeme.charAt(0))) {
                            column = parsePrim();
                        } else {
                            column = parseColumn();
                        }
                }
                if (column != null) {
                    selectItems.add(column);
                }
            } while ((nextLexeme()).equals(","));
        } catch (QueryException e) {
            if (!e.getCode().equals(END_LEXEMES_EXCEPTION))
                throw e;
        }
        pos--;
        return selectItems;
    }


    private List<QueryItem> parseFrom() throws QueryException {
        List<QueryItem> sources = null;
        try {
            String lexeme1 = nextLexeme();
            QueryItem table;
            if (lexeme1.equals("(")) {
                table = parseSubq();
            } else {
                pos--;
                table = parseTable();
            }
            sources = new ArrayList<>();
            sources.add(table);
            lexeme1 = nextLexeme();
            while (lexeme1.equals("inner") || lexeme1.equals("left") || lexeme1.equals("right") || lexeme1.equals("full") || lexeme1.equals(",")) {
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
                            pos--;
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
            if (!e.getCode().equals(END_LEXEMES_EXCEPTION))
                throw e;
        }
        return sources;
    }

    private List<WhereClause> parseWhere() throws QueryException {
        List<WhereClause> where = null;
        WhereClause whereItem = null;
        try {
            String lexeme;
            Column operand1 = null;
            QueryItem operand2 = null;
            WhereClause.OperatorType operator;
            where = new ArrayList<>();
            //no AND or OR lexeme after where clause means end of where;
            boolean isEnd = false;
            do {
                operator = null;
                whereItem = null;
                nextLexeme();
                operand1 = parseColumn();
                lexeme = nextLexeme();
                switch (lexeme) {
                    case "=":
                        operator = WhereClause.OperatorType.EQUAL;
                        break;
                    case ">":
                        operator = WhereClause.OperatorType.MORE;
                        break;
                    case ">=":
                        operator = WhereClause.OperatorType.MORE_OR_EQUAL;
                        break;
                    case "<":
                        operator = WhereClause.OperatorType.LESS;
                        break;
                    case "<=":
                        operator = WhereClause.OperatorType.LESS_OR_EQUAL;
                        break;
                    case "!=":
                    case "<>":
                        operator = WhereClause.OperatorType.NOT_EQUAL;
                        break;
                    case "in":
                        operator = WhereClause.OperatorType.IN;
                        break;
                    case "not":
                        if (nextLexeme().equals("in")) {
                            operator = WhereClause.OperatorType.NOT_IN;
                        } else throw new QueryException(LEXEMES_EXCEPTION, "Error operator");
                        break;
                    case "like":
                        operator = WhereClause.OperatorType.LIKE;
                        break;
                }
                lexeme = nextLexeme();
                if (Character.isDigit(lexeme.charAt(0)) || lexeme.equals("'")) {
                    operand2 = parsePrim();
                } else if (lexeme.equals("(")) {
                    operand2 = parseSubq();
                } else {
                    operand2 = parseColumn();
                }
                if ((operator != null) && (operand2 != null)) {
                    whereItem = new WhereClause(operand1, operator, operand2);
                    lexeme = nextLexeme();
                    if (lexeme.equals("and")) {
                        whereItem.setNext(WhereClause.ConnectionType.AND);
                    } else if (lexeme.equals("or")) {
                        whereItem.setNext(WhereClause.ConnectionType.OR);
                    } else {
                        isEnd = true;
                    }
                    where.add(whereItem);
                }
            } while (!isEnd);
        } catch (QueryException e) {
            if (!e.getCode().equals(END_LEXEMES_EXCEPTION)) {
                throw e;
            }
            //if end of query but last where clause not saved
            if (whereItem != null) {
                where.add(whereItem);
            }
        }
        pos--;
        return where;
    }

    private List<Column> parseGroupBy() throws QueryException {
        ArrayList<Column> group = null;
        try {
            Column column = null;
            group = new ArrayList<>();
            do {
                nextLexeme();
                column = parseColumn();
                if (column != null) {
                    group.add(column);
                }
            } while (nextLexeme().equals(","));
        } catch (QueryException e) {
            if (!e.getCode().equals(END_LEXEMES_EXCEPTION))
                throw e;
        }
        pos--;
        return group;
    }

    private List<Sort> parseOrderBy() throws QueryException {
        ArrayList<Sort> orderBy = null;
        Sort sort = null;
        try {
            String lexeme;
            Column column = null;
            orderBy = new ArrayList<>();
            do {
                sort = null;
                nextLexeme();
                column = parseColumn();
                sort = new Sort(column);
                lexeme = nextLexeme();
                if (lexeme.equals("asc")) {
                    sort.setAsc();
                } else if (lexeme.equals("desc")) {
                    sort.setDesc();
                } else {
                    pos--;
                }
                orderBy.add(sort);
            } while (nextLexeme().equals(","));
        } catch (QueryException e) {
            if (!e.getCode().equals(END_LEXEMES_EXCEPTION)) {
                throw e;
            }
            if (sort != null) {
                orderBy.add(sort);
            }
        }
        pos--;
        return orderBy;
    }

    private Integer parseLim() throws QueryException {
        Integer lim = null;
        try {
            String lexeme = nextLexeme();
            lim = Integer.valueOf(lexeme);
        } catch (QueryException e) {
            throw e;
        } catch (NumberFormatException e) {
            throw new QueryException(LEXEMES_EXCEPTION, "Error parsing limit or offset number: wrong number format");
        }
        return lim;
    }

    private Join parseJoin(Join.JoinType type) throws QueryException {
        Join join = null;
        Table joinedTable = null;
        boolean isBrackets = true;
        try {
            if (type == Join.JoinType.IMPLICIT_JOIN) {
                joinedTable = parseTable();
                join = new Join(type, joinedTable);
                return join;
            }
            //skip "join"
            if (!nextLexeme().equals("join")) {
                throw new QueryException(LEXEMES_EXCEPTION, "JOIN not found");
            }
            joinedTable = parseTable();
            //skip "on"
            if (!nextLexeme().equals("on")) {
                throw new QueryException(LEXEMES_EXCEPTION, "ON not found");
            }
            //if join condition not in brackets
            if (!nextLexeme().equals("(")) {
                pos--;
                isBrackets = false;
            }
            nextLexeme();
            Column column1 = parseColumn();
            if (column1 == null) {
                throw new QueryException(LEXEMES_EXCEPTION, "join column not found");
            }
            //skip "="
            if (!nextLexeme().equals("=")) {
                throw new QueryException(LEXEMES_EXCEPTION, "join column not found");
            }
            nextLexeme();
            Column column2 = parseColumn();
            if (column2 == null) {
                throw new QueryException(LEXEMES_EXCEPTION, "join column not found");
            }
            join = new Join(type, joinedTable, column1, column2);
            //if join condition in brackets, skip ")"
            if (isBrackets)
                nextLexeme();
        } catch (QueryException e) {
            if (!e.getCode().equals(END_LEXEMES_EXCEPTION))
                throw e;
        }
        return join;
    }


    private Subquery parseSubq() throws QueryException {
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
                    lexeme = nextLexeme();
                    subQuery = new Subquery(q, lexeme);
                    nextLexeme();
                } else {
                    pos--;
                    subQuery = new Subquery(q);
                }
            }
        } catch (QueryException e) {
            if (!e.getCode().equals(END_LEXEMES_EXCEPTION))
                throw e;
        }
        return subQuery;
    }

    private Table parseTable() throws QueryException {
        Table table = null;
        String lexeme1 = "";
        String lexeme2 = "";
        try {
            lexeme1 = nextLexeme();
            lexeme2 = nextLexeme();
            if (lexeme2.equals("as")) {
                lexeme2 = nextLexeme();
                table = new Table(lexeme1, lexeme2);
            } else {
                pos--;
                table = new Table(lexeme1);
            }
        } catch (QueryException e) {
            if (!e.getCode().equals(END_LEXEMES_EXCEPTION)) {
                throw e;
            }
            if (!lexeme1.isEmpty()) {
                table = new Table(lexeme1);
            }
        }
        return table;
    }


    private Column parseColumn() throws QueryException {
        Column column = null;
        String str1 = "";
        String str2 = "";
        String str3 = "";
        pos--;
        try {
            str1 = nextLexeme();
            str2 = nextLexeme();
            str3 = "";
            if (str2.equals(".")) {
                //means that str1 - table name, str2 - column name
                str2 = nextLexeme();
            } else {
                str2 = "";
                pos--;
            }
            str3 = nextLexeme();
            if (str3.equals("as")) {
                //means column with alias
                str3 = nextLexeme();
                column = new Column(str1, str2, str3);
            } else {
                pos--;
                column = new Column(str1, str2);
            }
        } catch (QueryException e) {
            if (!e.getCode().equals(END_LEXEMES_EXCEPTION)) {
                throw e;
            }
            if (str3.isEmpty()) {
                column = new Column(str1, str2);
            } else {
                column = new Column(str1, str2, str3);
            }
        }
        return column;
    }

    //column in aggregate function
    private FunctionColumn parseAggregateFunct(FunctionColumn.Functions fun) throws QueryException {
        FunctionColumn column = null;
        String str1 = "";
        String str2 = "";
        String str3 = "";
        Column col = null;
        try {
            nextLexeme();
            str1 = nextLexeme();
            str2 = nextLexeme();
            if (str2.equals(".")) {
                //means that str1 - table; str2 - column
                str2 = nextLexeme();
                nextLexeme();
            } else {
                //means that str1 - column
                str2 = "";
            }
            col = new Column(str1, str2);
            str3 = nextLexeme();
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
            if (!e.getCode().equals(END_LEXEMES_EXCEPTION)) {
                throw e;
            }
            if (str3.isEmpty()) {
                column = new FunctionColumn(fun, col);
            } else {
                column = new FunctionColumn(fun, col, str3);
            }
        }
        return column;
    }

    private Primitive parsePrim() throws QueryException {
        String lexeme = "";
        Primitive prim = null;
        pos--;
        try {
            lexeme = nextLexeme();
            //case string primitive
            if (lexeme.equals("'")) {
                lexeme = nextLexeme();
                prim = new Primitive(lexeme);
                //skip '
                lexeme = "";
                nextLexeme();
            } else {
                //case float primitive
                if (nextLexeme().equals(".")) {
                    Double number = Double.valueOf(lexeme + "." + nextLexeme());
                    prim = new Primitive(number);
                } else {
                    pos--;
                    Integer number = Integer.valueOf(lexeme);
                    prim = new Primitive(number);
                }
            }
            lexeme = nextLexeme();
            if (lexeme.equals("as")) {
                lexeme = nextLexeme();
                prim.setAlias(lexeme);
            } else {
                pos--;
            }
        } catch (QueryException e) {
            if (e.getCode().equals(END_LEXEMES_EXCEPTION)) {
                throw e;
            }
            if (!lexeme.isEmpty()) {
                Integer number = Integer.valueOf(lexeme);
                prim = new Primitive(number);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new QueryException(LEXEMES_EXCEPTION, "Error parsing primitive: wrong number format");
        }
        return prim;
    }

    private String nextLexeme() throws QueryException {
        if (pos < lexemes.size()) {
            return lexemes.get(pos++);
        } else {
            throw new QueryException(END_LEXEMES_EXCEPTION, "End of lexemes array");
        }
    }


    public void printLexemes() {
        for (String lexeme : lexemes) {
            System.out.println(lexeme);
        }
        System.out.println();
    }


    private void addLexeme() {
        if (buf.length() != 0) {
            lexemes.add(buf.toString());
            buf.delete(0, buf.length());
        }
    }
}
