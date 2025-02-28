package queries;

public class CustomerQueries {

    public static String fetchUserByCols(String searchVal) {
        StringBuilder sb = new StringBuilder(" select * from customer c");
        sb.append("where c.email like %");
        sb.append(searchVal);
        sb.append("%");
        return sb.toString();
    }
}
