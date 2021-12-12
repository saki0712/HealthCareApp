package com.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.demo.entity.Condition;
import com.demo.repository.ConditionDao;

@Service
public class ConditionServiceImpl implements ConditionService {
	
	private final ConditionDao dao;
	
	public ConditionServiceImpl(ConditionDao dao) {
		this.dao = dao;
	}

	@Override
	public List<Condition> findAll(String user_name){
		return dao.findAll(user_name);
	}
	
	@Override
	public void insert(Condition condition) {
		dao.insert(condition);
	}

	@Override
	public void update(Condition condition) {
		//Conditionを更新　idが無ければ例外発生
		if(dao.update(condition) == 0) {
			throw new ConditionNotFoundException("更新するコンディションデータが存在しません");
		}
	}

	@Override
	public void deleteById(int id) {
		
		//Conditionを更新 idがなければ例外発生
		if(dao.deleteById(id) == 0) {
			throw new ConditionNotFoundException("削除するコンディションデータが存在しません");
		}
	}

	@Override
	public Optional<Condition> getCondition(int id) {
		//Optional<Condition>一件を取得 idが無ければ例外発生
		try {
			return dao.findById(id);		
		}catch (EmptyResultDataAccessException e) {
			//例外を投げて自作したConditionNotFoundExceptionで処理させる
			throw new ConditionNotFoundException("指定されたコンディションデータが存在しません");
		}
	}

	@Override
	public List<Condition> findByDate(String date_from, String date_to, String user_name){
		
		return dao.findByDate(date_from, date_to, user_name);
	}

	@Override
	public List<Condition> findMental(String graph_from, String graph_to, String user_name) {
		return dao.findMental(graph_from, graph_to, user_name);
	}

	@Override
	public List<Condition> findDay(String graph_from, String graph_to, String user_name) {
		return dao.findDay(graph_from, graph_to, user_name);
	}

	@Override
	public Condition findMemo(String day, String user_name) {
		return dao.findMemo(day, user_name);
	}
	
}
