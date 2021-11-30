package com.demo.entity;

import java.sql.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Condition {

	private int id;
	private String user_name;
	private Date day;
	
	@Digits(integer = 1, fraction = 0)
	@NotNull (message = "内容を入力してください。")
	private int condition;
	
	@Digits(integer = 1, fraction = 0)
	@NotNull (message = "内容を入力してください。")
	private int mental;
	
	@Digits(integer = 1, fraction = 0)
	@NotNull (message = "内容を入力してください。")
	private int achievement;
	
	@Size(min = 1, max = 200, message = "200文字以内で入力してください。")
	private String memo;
	
    //HTMLファイルでの分岐処理
    private boolean newCondition;
    
	public Condition(int id, String user_name, Date day, int condition, int mental, int achievement, String memo,
			boolean newCondition) {
		this.id = id;
		this.setUser_name(user_name);
		this.day = day;
		this.condition = condition;
		this.mental = mental;
		this.achievement = achievement;
		this.memo = memo;
		this.newCondition = newCondition;
	}
	
	//コンディション登録
	public Condition(String user_name, Date day, int condition, int mental, int achievement, String memo) {
		this.user_name = user_name;
		this.day = day;
		this.condition = condition;
		this.mental = mental;
		this.achievement = achievement;
		this.memo = memo;
	}
	
	//グラフ機能
	public Condition(int mental) {
		this.mental = mental;
	}
	public Condition(Date day) {
		this.day = day;
	}
	
	
	public Condition() {
	}

	public int getCondition() {
		return condition;
	}
	public void setCondition(int condition) {
		this.condition = condition;
	}
	
	public int getMental() {
		return mental;
	}
	public void setMental(int mental) {
		this.mental = mental;
	}
	
	
	
	public int getAchievement() {
		return achievement;
	}
	public void setAchievement(int achievement) {
		this.achievement = achievement;
	}
	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public Date getDay() {
		return day;
	}
	public void setDay(Date day) {
		this.day = day;
	}
	
	//グラフ画面にて日付けをStringで表示
	@Override
	public String toString() {
		return "日付:" + day;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public boolean isNewCondition() {
		return newCondition;
	}
	public void setNewCondition(boolean newCondition) {
		this.newCondition = newCondition;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public Object toObject() {
		return mental;
	}
	
}
