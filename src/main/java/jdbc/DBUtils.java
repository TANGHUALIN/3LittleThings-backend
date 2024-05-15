package jdbc;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.*;


public class DBUtils {
    private static DataSource dataSource = null;

    static {
        //外部のファイルを読み込む、/ はsrc
        InputStream asStream = DBUtils.class.getResourceAsStream("/db.properties");
        //Propertiesオブジェクトを通してStreamを解釈
        Properties properties = new Properties();
        try {
            properties.load(asStream);
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConn() {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void close(ResultSet resultSet, Statement statement, Connection conn) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static int update(String sql, Object... params) {
        Connection conn = getConn();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            setParam(ps, params);
            System.out.println(ps);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(null, ps, conn);
        }
        return -1;
    }

    public static List<List<Object>> queryList(String sql, Object... params) {
        Connection conn = getConn();
        PreparedStatement ps = null;
        ResultSet set = null;
        try {
            ps = conn.prepareStatement(sql);
            setParam(ps, params);
            set = ps.executeQuery();
            ResultSetMetaData metaData = set.getMetaData();
            ArrayList<List<Object>> table = new ArrayList<>();
            while (set.next()) {
                ArrayList<Object> row = new ArrayList<>();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    row.add(set.getObject(i));
                }
                table.add(row);
            }
            return table;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(set, ps, conn);
        }
        return null;
    }


    public static List<Map<String, Object>> queryMap(String sql, Object... params) {
        Connection conn = getConn();
        ResultSet set = null;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            setParam(ps, params);
            set = ps.executeQuery();
            ResultSetMetaData metaData = set.getMetaData();
            ArrayList<Map<String, Object>> tableWithColumnName = new ArrayList<>();
            while (set.next()) {
                HashMap<String, Object> columnNameAndValue = new HashMap<>();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    columnNameAndValue.put(metaData.getColumnName(i), set.getObject(i));
                }
                tableWithColumnName.add(columnNameAndValue);
            }
            return tableWithColumnName;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(set, ps, conn);
        }
        return null;
    }

    public static int insertAndGetPrimaryKey(String sql, Object... params) {
        Connection conn = getConn();
        PreparedStatement ps = null;
        ResultSet set = null;
        try {
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setParam(ps, params);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                set = ps.getGeneratedKeys();
                if (set.next()) {
                    int pkValue = set.getInt(1);
                    System.out.println(pkValue);
                    return pkValue;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(set, ps, conn);
        }
        return -1;
    }


    public static void setParam(PreparedStatement ps, Object... params) {
        try {
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
