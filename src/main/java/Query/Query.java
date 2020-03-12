package Query;

import Query.QueryItems.*;


import java.util.List;

public class Query {
    private List<QueryItem> selectItems = null;
    private List<QueryItem> fromSources = null;
    private List<WhereClause> whereClauses = null;
    private List<Column> groupByColumns = null;
    private List<Sort> sortColumns = null;
    private Integer limit = null;
    private Integer offset = null;
    private LexParser parser;
    private static final String QUERY_EXCEPTION = "QUERY_EXCEPTION";

    public Query(String strQuery) throws QueryException {
        strQuery = strQuery.toLowerCase();
        parser = new LexParser(strQuery);
        //parser.printLexemes();
        parser.parseQuery(this);
    }

    public Query() {
    }

    public void setSelectItems(List<QueryItem> selectItems) {
        if (!selectItems.isEmpty()) {
            this.selectItems = selectItems;
        }
    }

    public void setFromSources(List<QueryItem> fromSources) {
        if (!fromSources.isEmpty()) {
            this.fromSources = fromSources;
        }
    }

    public void setWhereClauses(List<WhereClause> whereClauses) {
        if (!whereClauses.isEmpty()) {
            this.whereClauses = whereClauses;
        }
    }

    public void setGroupByColumns(List<Column> groupByColumns) {
        if (!groupByColumns.isEmpty()) {
            this.groupByColumns = groupByColumns;
        }
    }

    public void setSortColumns(List<Sort> sortColumns) {
        this.sortColumns = sortColumns;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }


    public String print(String pad) throws QueryException {
        StringBuffer strResult;
        String newPad = pad + "\t";
        if (selectItems != null) {
            strResult = new StringBuffer(pad + "SELECT:\n");
            for (QueryItem columnItem : selectItems) {
                strResult.append(columnItem.print(newPad) + "\n");
            }
        } else {
            throw new QueryException(QUERY_EXCEPTION, "Select statement not found");
        }
        if (fromSources != null) {
            strResult.append(pad + "FROM:\n");
            for (QueryItem source : fromSources) {
                strResult.append(source.print(newPad) + "\n");
            }
        } else {
        throw new QueryException(QUERY_EXCEPTION, "From statement not found");
        }
        if (whereClauses != null) {
            strResult.append(pad + "WHERE:\n");
            for (WhereClause where : whereClauses) {
                strResult.append(where.print(newPad) + "\n");
            }
        }
        if (groupByColumns != null) {
            strResult.append(pad + "GROUP BY:\n");
            for (Column group : groupByColumns) {
                strResult.append(group.print(newPad) + "\n");
            }
        }
        if (sortColumns != null) {
            strResult.append(pad + "ORDER BY:\n");
            for (Sort sort : sortColumns) {
                strResult.append(sort.print(newPad) + "\n");
            }
        }
        if (limit != null) {
            strResult.append(pad + "LIMIT:\n");
            strResult.append(newPad + limit.toString());
        }
        if (offset != null) {
            strResult.append(pad + "LIMIT:\n");
            strResult.append(newPad + offset.toString());
        }
        return strResult.toString();
    }
}