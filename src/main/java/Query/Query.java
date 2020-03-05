package Query;

import Query.QueryItems.*;


import java.util.List;

public class Query {
    private List<ColumnItem> selectItems = null;
    private List<Source> fromSources  = null;
    private List<WhereClause> whereClauses = null;
    private List<ColumnItem> groupByColumns = null;
    private List<Sort> sortColumns = null;
    private Integer limit = null;
    private Integer offset = null;
    private LexParser parser;
    private static final String QUERY_EXCEPTION = "QUERY_EXCEPTION";

    public Query(String strQuery) throws QueryException {
        strQuery = strQuery.toLowerCase();
        parser = new LexParser(strQuery);
        parser.printLexemes();
        parser.parseQuery(this);
    }

    public Query() {}

    public void setSelectItems(List<ColumnItem> selectItems) {
        if (!selectItems.isEmpty()) {
            this.selectItems = selectItems;
        }
    }

    public void setFromSources(List<Source> fromSources) {
        this.fromSources = fromSources;
    }

    public void setWhereClauses(List<WhereClause> whereClauses) {
        this.whereClauses = whereClauses;
    }

    public void setGroupByColumns(List<ColumnItem> groupByColumns) {
        this.groupByColumns = groupByColumns;
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


    public String getString() throws QueryException{
        StringBuffer strResult;
        if (selectItems != null) {
            strResult = new StringBuffer("SELECT:\n");
            for (ColumnItem columnItem : selectItems) {
                strResult.append(columnItem.getString() + " ");
            }
            strResult.append("\n");
        } else {
            throw new QueryException(QUERY_EXCEPTION, "Select statement not found");
        }
        if (fromSources != null) {
            strResult.append("FROM:\n");
            for (Source source: fromSources) {
                strResult.append(source.getString() + " ");
            }
            strResult.append("\n");
        } //else {
            //throw new QueryException(QUERY_EXCEPTION, "From statement not found");
        //}
        if (whereClauses != null) {
            strResult.append("WHERE:\n");
            for (WhereClause where: whereClauses) {
                strResult.append(where.getString() + " ");
            }
            strResult.append("\n");
        }
        if (groupByColumns != null) {
            strResult.append("GROUP BY:\n");
            for (ColumnItem group: groupByColumns) {
                strResult.append(group.getString() + " ");
            }
            strResult.append("\n");
        }
        if (sortColumns != null) {
            strResult.append("ORDER BY:\n");
            for (Sort sort: sortColumns) {
                strResult.append(sort.getString() + " ");
            }
            strResult.append("\n");
        }
        if (limit != null) {
            strResult.append("LIMIT:\n");
            strResult.append(limit.toString());
            strResult.append("\n");
        }
        if (offset != null) {
            strResult.append("LIMIT:\n");
            strResult.append(offset.toString());
            strResult.append("\n");
        }
        return strResult.toString();
    }
}