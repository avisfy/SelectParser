package Query.QueryItems;

public class Primitive extends QueryItem {
    private Object primObj;
    private String alias = null;

    public Primitive(Object primObj) {
        this.primObj = primObj;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String print(String pad) {
        if (alias != null) {
            return pad + "prim:\n" + pad + "\tvalue: " + primObj.toString() + "\n" + pad + "\talias: " + alias;
        } else {
            return pad + "prim:\n" + pad + "\tvalue: " + primObj.toString();
        }
    }


}
