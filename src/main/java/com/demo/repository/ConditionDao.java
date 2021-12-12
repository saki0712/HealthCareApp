package com.demo.repository;

import java.util.List;
import java.util.Optional;

import com.demo.entity.Condition;

public interface ConditionDao {
	
	List<Condition> findAll(String user_name);
	
	Optional<Condition> findById(int id);
	
	void insert(Condition condition);
	
	//更新した数が返ってくる
	int update(Condition condition);
	int deleteById(int id);

	List<Condition> findByDate(String date_from, String date_to, String user_name);

	List<Condition> findMental(String graph_from, String graph_to, String user_name);

	List<Condition> findDay(String graph_from, String graph_to, String user_name);

	Condition findMemo(String day, String user_name);
	
}
