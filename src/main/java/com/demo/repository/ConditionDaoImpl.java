package com.demo.repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.demo.entity.Condition;

@Repository
public class ConditionDaoImpl implements ConditionDao {
	
	private final JdbcTemplate jdbcTemplate;

	public ConditionDaoImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public List<Condition> findAll(String user_name) {
		
		String sql = "SELECT * FROM `condition` WHERE `condition`.user_name LIKE \"" + user_name + "\" ORDER BY `day` ASC;";
		
		//コンディションデータをMapのListで取得
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);
		
		//return用の空のListを用意
		List<Condition> list = new ArrayList<Condition>();
		
		//データをConditionにまとめる
		for(Map<String, Object> result : resultList) {
			Condition condition = new Condition();
			condition.setId((int)result.get("id"));
			condition.setUser_name((String)result.get("user_name"));
			condition.setDay(((Date) result.get("day")).toLocalDate());
			//condition.setCondition((int)result.get("condition"));
			condition.setMental((int)result.get("mental"));
			//condition.setAchievement((int)result.get("achievement"));
			condition.setMemo((String)result.get("memo"));
			
			list.add(condition);
		}	
		return list;
	}
	
	@Override
	public void insert(Condition condition) {
		jdbcTemplate.update("INSERT INTO `condition` (`user_name`, `day`, `mental`, `memo`) VALUES(?, ?, ?, ?)"
				+" ON DUPLICATE KEY UPDATE"
			    +" `day` = VALUES(`day`)"
			    //+", `condition` = VALUES(`condition`)"
			    +", `mental` = VALUES(`mental`)"
			    //+", `achievement` = VALUES(`achievement`)"
			    +", `memo` = VALUES(`memo`);"
,
				condition.getUser_name(), condition.getDay(), condition.getMental(), condition.getMemo());		
	}

	@Override
	public int update(Condition condition) {
		return jdbcTemplate.update("UPDATE `condition` SET day = ?, mental = ?, memo = ? WHERE id = ?;",
				condition.getDay(), condition.getMental(), condition.getMemo(), condition.getId());		
	}

	@Override
	public int deleteById(int id) {
		return jdbcTemplate.update("DELETE FROM `condition` WHERE id = ?;", id);
	}

	@Override
	public Optional<Condition> findById(int id) {
		String sql = "SELECT id, user_name, day, mental, memo "
				+ "FROM `condition` "
				+ "WHERE id = ?;";

		//コンディションを一件取得
		Map<String, Object> result = jdbcTemplate.queryForMap(sql,id);

		Condition condition = new Condition();
		condition.setId((int)result.get("id"));
		condition.setUser_name((String)result.get("user_name"));
		condition.setDay(((Date) result.get("day")).toLocalDate());
		//condition.setCondition((int)result.get("condition"));
		condition.setMental((int)result.get("mental"));
		//condition.setAchievement((int)result.get("achievement"));
		condition.setMemo((String) result.get("memo"));

		//conditionをOptionalでラップする
		Optional<Condition> conditionOpt = Optional.ofNullable(condition);	
		
		return conditionOpt;
	}

	@Override
	public List<Condition> findByDate(String date_from, String date_to, String user_name){
		String sql  = "SELECT * FROM `condition` WHERE day BETWEEN \"" + date_from + "\" AND \"" + date_to + "\" AND `condition`.user_name LIKE \"" + user_name + "\" ORDER BY `day` ASC;";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);
//		System.out.println(sql);
		List<Condition> list = new ArrayList<Condition>();
		for(Map<String, Object> result : resultList) {
			Condition condition = new Condition();
			condition.setId((int)result.get("id"));
			condition.setUser_name((String)result.get("user_name"));
//			System.out.println(condition.getUser_name());
			condition.setDay(((Date) result.get("day")).toLocalDate());
			//condition.setCondition((int)result.get("condition"));
			condition.setMental((int)result.get("mental"));
			//condition.setAchievement((int)result.get("achievement"));
			condition.setMemo((String)result.get("memo"));
			
			list.add(condition);
		}	
		return list;
	}

	@Override
	public List<Condition> findMental(String graph_from, String graph_to, String user_name) {
		String sql  = "SELECT mental FROM `condition` WHERE day BETWEEN \"" + graph_from + "\" AND \"" + graph_to + "\" AND `condition`.user_name LIKE \"" + user_name + "\" ORDER BY `day` ASC;";
		RowMapper<Condition> rowMapper = new BeanPropertyRowMapper<Condition>(Condition.class);
        List<Condition> MentalList = jdbcTemplate.query(sql, rowMapper);
		return MentalList;
	}

	@Override
	public List<Condition> findDay(String graph_from, String graph_to, String user_name) {
		String sql  = "SELECT day FROM `condition` WHERE day BETWEEN \"" + graph_from + "\" AND \"" + graph_to + "\" AND `condition`.user_name LIKE \"" + user_name + "\" ORDER BY `day` ASC;";
		RowMapper<Condition> rowMapper = new BeanPropertyRowMapper<Condition>(Condition.class);
        List<Condition> DayList = jdbcTemplate.query(sql, rowMapper);
		return DayList;
	}

	@Override
	public Condition findMemo(String day, String user_name) {
		String sql  = "SELECT memo FROM `condition` WHERE day = ? AND `condition`.user_name LIKE ?;";
		RowMapper<Condition> rowMapper = new BeanPropertyRowMapper<Condition>(Condition.class);
		Condition memo = jdbcTemplate.queryForObject(sql, rowMapper, day, user_name);
		return memo;
	}




}
