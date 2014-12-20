package com.capstone.data;

import java.util.Date;

public class Patient {
	private String pid; 
	private String name; 
	private String gender;
	private Date date; 
	private String email; 
	private String phone; 
	private String address;
	private String zip;
	
	public Patient(){}
	
	public Patient(String pid, String name, String gender, Date date,
			String email, String phone, String address, String zip) {
		super();
		this.pid = pid;
		this.name = name;
		this.gender = gender;
		this.date = date;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.zip = zip;
	}
	public String getPid() {
		return pid;
	}
	public String getName() {
		return name;
	}
	public String getGender() {
		return gender;
	}
	public Date getDate() {
		return date;
	}
	public String getEmail() {
		return email;
	}
	public String getPhone() {
		return phone;
	}
	public String getAddress() {
		return address;
	}
	public String getZip() {
		return zip;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}

	@Override
	public String toString() {
		return "Patient [pid=" + pid + ", name=" + name + ", gender=" + gender
				+ ", date=" + date + ", email=" + email + ", phone=" + phone
				+ ", address=" + address + ", zip=" + zip + "]";
	}
	
	
}
