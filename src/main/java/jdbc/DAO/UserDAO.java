package jdbc.DAO;

import items.User;

public interface UserDAO {
	boolean queryUserEmailExist(String email);

	User queryUserByEmail(String email);
	boolean deleteUser(Integer uid);
	boolean insertUser(String email,String password);
	boolean updateUserPassword(String password,String email);

	Integer queryUidByEmail(String email);


    boolean insertTempUser(String verifyToken, String email, String password);

	User queryTempUserByToken(String token);

    boolean queryTempUserEmailExist(String email);

}