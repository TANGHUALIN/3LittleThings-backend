package jdbc.DAO;

import items.User;
import jdbc.DBUtils;

import java.util.List;
import java.util.Map;

public class UserDAOImpl implements UserDAO {
    @Override
    public boolean queryUserEmailExist(String email) {
        String sql = "select * from user where email=?";
        List<List<Object>> table = DBUtils.queryList(sql, email);
        return !table.isEmpty();
    }

    @Override
    public User queryUserByEmail(String email) {
        String sql = "select uid,hashed_password from user where email=?";
        List<Map<String, Object>> table = DBUtils.queryMap(sql, email);

        if (table != null && !table.isEmpty()) {
            Map<String, Object> row = table.get(0);
            Integer uid = (Integer) row.get("uid");
            String hashedPassword = (String) row.get("hashed_password");
            return new User(uid, email, hashedPassword);
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteUser(Integer uid) {
        String sql = "delete * from user where uid=?";
        return DBUtils.update(sql, uid) != -1;
    }

    @Override
    public boolean insertUser(String email, String password) {
        String sql = "insert into user(email,hashed_password) values(?,?)";
        return DBUtils.update(sql, email, password) != -1;
    }

    @Override
    public boolean updateUserPassword(String password, String email) {
        String sql = "update user set hashed_password=? where email=?";
        return DBUtils.update(sql, password, email) != -1;
    }

    @Override
    public Integer queryUidByEmail(String email) {
        String sql = "select uid from user where email=?";
        List<Map<String, Object>> table = DBUtils.queryMap(sql, email);
        if (!table.isEmpty()) {
            Map<String, Object> row = table.get(0);
            if (row.containsKey("uid")) {
                return (Integer) row.get("uid");
            }
        }
        return -1;
    }

    @Override
    public boolean insertTempUser(String verifyToken, String email, String password) {
        String sql = "insert into temp_user(temp_token,email,hashed_password) values(?,?,?)";
        return DBUtils.update(sql, verifyToken, email, password) != -1;
    }

    @Override
    public User queryTempUserByToken(String token) {
        String sql = "select email,hashed_password from temp_user where temp_token=?";
        List<Map<String, Object>> table = DBUtils.queryMap(sql, token);
        if (table != null && !table.isEmpty()) {
            Map<String, Object> row = table.get(0);
            String email = (String) row.get("email");
            String hashedPassword = (String) row.get("hashed_password");
            if (deleteTempUser(token)) {
                System.out.println("delete temp user with token successfully ");
                return new User(token, email, hashedPassword);
            } else {
                System.out.println("Failed to delete temp user with token: " + token);
                return null;
            }
        }
        return null;
    }

    public boolean queryTempUserEmailExist(String email) {
        String sql = "select * from temp_user where email=?";
        List<List<Object>> table = DBUtils.queryList(sql, email);
        return !(table == null || table.isEmpty());
    }

    private Boolean deleteTempUser(String token) {
        String sql = "delete from temp_user where temp_token=?";
        return DBUtils.update(sql, token) != -1;
    }


}
