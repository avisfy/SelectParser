package Query.QueryItems;

public class Primitive extends QueryItem {
    private Object primObj;

    public Primitive(Object primObj) {
        this.primObj = primObj;
    }

    public String print(String pad) {
        return pad + "prim: " + primObj.toString();
    }
}
