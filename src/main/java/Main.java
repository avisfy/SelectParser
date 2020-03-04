import Query.Query;
import Query.QueryException;

import java.util.Scanner;

/*
SELECT name, city FROM users
 */
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String input = "";
        String line = in.nextLine();
        while (!line.isEmpty()) {
            input = input + " " + line;
            line = in.nextLine();
        }
        try {
            Query q = new Query(input);
            System.out.println(q.getString());
        } catch (QueryException e) {
            System.out.println(e.getError());
        }

    }

}
