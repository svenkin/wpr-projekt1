package model;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	private String firstName;
	private String lastName;
	private Gender gender;
	private String nickName;
	private String password;
	private String accessChapterId;
	private String accessSectionId;
	
	public User() {
		//
	}

	public User(
			String firstName,
			String lastName,
			Gender gender,
			String nickName,
			String password,
			String accessChapterId,
			String accessSectionId)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.nickName = nickName;
		this.password = password;
		this.accessChapterId = accessChapterId;
		this.accessSectionId = accessSectionId;
	}



	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAccessChapterId() {
		return accessChapterId;
	}

	public void setAccessChapterId(String accessChapterId) {
		this.accessChapterId = accessChapterId;
	}

	public String getAccessSectionId() {
		return accessSectionId;
	}

	public void setAccessSectionId(String accessSectionId) {
		this.accessSectionId = accessSectionId;
	}
}
