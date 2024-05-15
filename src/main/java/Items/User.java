package items;

public class User {
	private Integer uid;
	private String email;
	private String hashedPassword;

	private String tempToken;

	public User(Integer uid, String email, String hashedPassword) {
		this.uid = uid;
		this.email = email;
		this.hashedPassword = hashedPassword;
	}

	public User(String tempToken, String email, String hashedPassword) {
		this.email = email;
		this.hashedPassword = hashedPassword;
		this.tempToken = tempToken;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public String getTempToken() {
		return tempToken;
	}

	public void setTempToken(String tempToken) {
		this.tempToken = tempToken;
	}
}
