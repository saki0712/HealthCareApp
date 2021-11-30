package com.demo.service;

import java.util.List;
import java.util.Optional;

import com.demo.entity.Condition;

public interface ConditionService {

	List<Condition> findAll(String user_name);
	
	Optional<Condition> getCondition(int id);
	
	void insert(Condition condition);
	void update(Condition condition);
	void deleteById(int id);
	
	List<Condition> findByDate(String date_from, String date_to, String user_name);
	
	//mentalとdayを取得
	List<Condition> findMental(String graph_from, String graph_to, String user_name);
	List<Condition> findDay(String graph_from, String graph_to, String user_name);
	
}
