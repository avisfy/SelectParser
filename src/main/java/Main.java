import Query.Query;
import Query.QueryException;

import java.util.Scanner;

/*
SELECT name, city FROM users
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Enter select query. \";\" - end of query.");
        System.out.print("> ");
        Scanner in = new Scanner(System.in);
        String input = "";
        String line = in.nextLine();
        while (!line.isEmpty()) {
            input = input + line;
            if (line.indexOf(";") == -1) {
                input = input + " ";
                line = in.nextLine();
            } else
                break;
        }

        try {
            Query q = new Query(input);
            System.out.println(q.print());
        } catch (QueryException e) {
            System.out.println(e.getError());
        }

    }

}
