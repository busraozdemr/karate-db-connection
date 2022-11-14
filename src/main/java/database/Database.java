package database;

import com.intuit.karate.Logger;
import oracle.jdbc.driver.OracleDriver;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.impl.DSL;
import org.jooq.tools.json.JSONArray;
import org.jooq.tools.json.JSONObject;

import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Database {
    private final String jdbcUrl;
    private final String userName;
    private final String password;
    Logger logger;


    public Database(String[] dbInfo) {
        this.jdbcUrl = dbInfo[0];
        this.userName = dbInfo[1];
        this.password = dbInfo[2];
        logger = new Logger(Database.class);
    }

    private Connection getDbConnection() throws SQLException {
        DriverManager.registerDriver(new OracleDriver());
        return DriverManager.getConnection(jdbcUrl, userName, password);
    }

    public String runQueryAndGetJsonArray(String query) {
        try {
            Statement statement = getDbConnection().createStatement();
            statement.setQueryTimeout(10);
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData md = resultSet.getMetaData();
            int numCols = md.getColumnCount();
            List<String> colNames = IntStream.range(0, numCols)
                    .mapToObj(i -> {
                        try {
                            return md.getColumnName(i + 1);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            return "?";
                        }

                    }).collect(Collectors.toList());
            List json = DSL.using(getDbConnection())
                    .fetch(resultSet).map(new RecordMapper() {
                        @Override
                        public JSONObject map(Record r) {
                            JSONObject object = new JSONObject();
                            colNames.forEach(cn -> object.put(cn, r.get(cn)));
                            return object;
                        }
                    });
            return new JSONArray(json).toString();
        } catch (SQLException e) {
            logger.error(e.getSQLState() + "-" + e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return "Fail";
    }
    public String insertUpdateDeleteQuery(String query){
        try{
            String[] queries = query.split(";");
            Statement statement= getDbConnection().createStatement();
            statement.setQueryTimeout(10);
            for(String s:queries){
                statement.addBatch(s);
            }
            statement.executeBatch();
        }catch (SQLException e){
            logger.error(e.getSQLState() + "-" + e.getMessage());
        }

        return "Fail";

    }
}
