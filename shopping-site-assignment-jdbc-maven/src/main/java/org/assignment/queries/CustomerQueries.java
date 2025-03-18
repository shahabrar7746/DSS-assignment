package org.assignment.queries;

public class CustomerQueries {
    private static final String SELECT_BASE_QUERY = " SELECT * FROM customer ";
    private static final String INSERT_BASE_QUERY = " INSERT INTO customer ";
    private static final String UPDATE_BASE_QUERY = " UPDATE customer SET ";
    private static final String DELETE_BASE_QUERY = " DELETE FROM customer ";
    private static final String EQUALS = " = ";
    private static final String PLACEHOLDER = " ? ";

    public static String getAllCustomerQuery() {
        return SELECT_BASE_QUERY;
    }

    public static String getCustomerOrAdminByColumn(String columns[], String operation) {
        StringBuilder builder = new StringBuilder(SELECT_BASE_QUERY);
        builder.append(Clauses.WHERE);
        builder.append(columns[0]);
        builder.append(EQUALS).append(PLACEHOLDER);
        for (int i = 1; i < columns.length; i++) {
            builder.append(" ").append(operation).append(" ");
            builder.append(columns[i]);
            builder.append(EQUALS).append(PLACEHOLDER);
        }
        return builder.toString();
    }

    public static String addCustomerQuery(String columns[]) {
        StringBuilder builder = new StringBuilder(INSERT_BASE_QUERY);
        builder.append("(");
        builder.append(columns[0]);
        for (int i = 1; i < columns.length; i++) {
            builder.append(",");
            builder.append(columns[i]);
        }
        builder.append(" ) ");
        builder.append(" VALUES( ?");
        for (int i = 1; i < columns.length; i++) {
            builder.append(",?");
        }
        builder.append(" ) ");
        return builder.toString();
    }

    public static String updateCustomerQuery(String columns[], String customer_id) {
        StringBuilder builder = new StringBuilder(UPDATE_BASE_QUERY);
        builder.append(columns[0]);
        builder.append(EQUALS).append(PLACEHOLDER);
        for (int i = 1; i < columns.length; i++) {
            builder.append(",");
            builder.append(columns[i]);
            builder.append(EQUALS).append(PLACEHOLDER);
        }
        builder.append(" WHERE ");
        builder.append(customer_id);
        builder.append(EQUALS).append(PLACEHOLDER);
        return builder.toString();
    }

    public static String deleteCustomerQuery(String column) {
        StringBuilder builder = new StringBuilder(DELETE_BASE_QUERY);
        builder.append(" WHERE ");
        builder.append(column);
        builder.append(EQUALS).append(PLACEHOLDER);
        return builder.toString();

    }

}
