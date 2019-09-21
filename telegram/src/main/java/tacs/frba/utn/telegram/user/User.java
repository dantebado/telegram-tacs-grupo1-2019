package tacs.frba.utn.telegram.user;

public class User {
	
	private String username;
	private String pass;
	private Boolean isAdmin;
	
	public User(String username, String pass, Boolean isAdmin) {
		this.username = username;
		this.pass = pass;
		this.isAdmin = isAdmin;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public Boolean getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

}
