package com.demo.app.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.boot.json.JsonParseException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.entity.Condition;
import com.demo.service.ConditionService;
import com.demo.service.UserDetailsImpl;
import com.fasterxml.jackson.databind.JsonMappingException;

@Controller
@RequestMapping("/condition")
public class ConditionController {

	private final ConditionService conditionService;

	public ConditionController(ConditionService conditionService) {
		this.conditionService = conditionService;
	}

	// コンディション日付検索一覧画面
	@GetMapping
	public String condition(Condition condition, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		String user_name = userDetails.getUsername();
		// model.addAttribute("user_name", user_name);

		// Conditionのリストを取得する
		List<Condition> list = conditionService.findAll(user_name);

		for (int i = 0; i < list.size(); i++) {
			//List<String> memoList = new ArrayList<String>();
			System.out.println(list.get(i));
			// String memo =
			// memoList.add(condition.getMemo());
		}

		model.addAttribute("list", list);
		// model.addAttribute("title", user_name + "さんのコンディションデータ");

		return "app/condition_data";
	}

	// 登録画面
	@GetMapping("/insert")
	public String form(Condition condition, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		String user_name = userDetails.getUsername();
		LocalDate date = LocalDate.now();
		condition.setDay(date);
		model.addAttribute("user_name", user_name);
		model.addAttribute("day", date);
		return "app/condition_register";
	}

	// コンディションデータを一件挿入
	@PostMapping("/insert")
	public String insert(
			@Valid @ModelAttribute Condition condition,
			BindingResult result,
			Model model) {

		if (!result.hasErrors()) {
			conditionService.insert(condition);
			return "redirect:/condition/graph";
		} else {
			// エラーの場合
			// List<String> errorList = new ArrayList<String>();
			// for (ObjectError error : result.getAllErrors()) {
			// System.out.println(error.getDefaultMessage());
			// errorList.add(error.getDefaultMessage());
			// }
			model.addAttribute("condition", condition);
			return "app/condition_register";
		}
	}

	// 詳細表示 削除、更新できる画面
	// 一件コンディションデータを取得し、フォーム内に表示
	@GetMapping("/{id}")
	public String showUpdate(
			Condition condition,
			@PathVariable int id,
			Model model) {

		// System.out.println(condition.getId());

		// Conditionを取得(Optionalでラップ)
		Optional<Condition> conditionOpt = conditionService.getCondition(id);

		// Conditionがnullでなければ中身を取り出し
		if (conditionOpt.isPresent()) {
			condition = conditionOpt.get();
		}

		model.addAttribute("condition", condition);
		model.addAttribute("id", id);

		return "app/condition_details";
	}

	// タスクidを取得し、一件のデータ更新
	@PostMapping("/update")
	public String update(
			@Valid @ModelAttribute Condition condition,
			BindingResult result,
			@RequestParam("id") int id,
			Model model,
			RedirectAttributes redirectAttributes) {

		// System.out.println(result);

		if (!result.hasErrors()) {

			// 更新処理、フラッシュスコープの使用、リダイレクト（個々の編集ページ）
			conditionService.update(condition);
			redirectAttributes.addFlashAttribute("complete", "更新しました！");
			return "redirect:/condition/" + id;
		} else {
			// エラーの場合
			model.addAttribute("condition", condition);
			model.addAttribute("id", id);
			return "app/condition_details";
		}
	}

	// タスクidを取得し、一件のデータ削除
	@PostMapping("/delete")
	public String delete(
			@RequestParam("id") int id,
			Model model) {

		// タスクを一件削除しリダイレクト
		conditionService.deleteById(id);
		return "redirect:/";
	}

	@GetMapping("/search")
	public String dateSearch(Condition condition, Model model, @ModelAttribute("date_from") String date_from,
			@ModelAttribute("date_to") String date_to,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		String user_name = userDetails.getUsername();

		List<Condition> list = conditionService.findByDate(date_from, date_to, user_name);
		// System.out.println(condition.getUser_name());

		model.addAttribute("title", user_name + "さんのコンディションデータ");
		model.addAttribute("list", list);

		return "app/condition_data";
	}

	@GetMapping("/graph")
	public String graph(Model model, @ModelAttribute("graph_from") String graph_from,
			@ModelAttribute("graph_to") String graph_to,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		String user_name = userDetails.getUsername();

		// 縦軸mentalデータを取ってきてリストに詰める
		List<Condition> mentalList = conditionService.findMental(graph_from, graph_to, user_name);

		// object型の配列を作る
		Object[] mentalArray = new Object[mentalList.size()];
		// object型の配列に中身data型のオブジェクトをobject型に変換して詰める
		for (int i = 0; i < mentalArray.length; i++) {
			mentalArray[i] = mentalList.get(i).toObject();
			// System.out.println(mentalArray[i]);
		}

		// Object[] mentalArray = mentalList.toArray(new Object[mentalList.size()]);
		// Object[] DayArray = dayList.toArray(new Object[dayList.size()]);
		// System.out.println(DayArray.getClass());

		// 横軸dayデータを取ってきてリストに詰める
		List<Condition> dayList = conditionService.findDay(graph_from, graph_to, user_name);
		// string型の配列を作る
		String[] dayArray = new String[dayList.size()];
		// string型の配列に中身data型のオブジェクトをstring型に変換して詰める
		for (int i = 0; i < dayArray.length; i++) {
			dayArray[i] = dayList.get(i).toString();
		}
		// String[] dayArray = Arrays.asList(DayArray).toArray(new
		// String[DayArray.length]);

		model.addAttribute("mental", mentalArray);
		model.addAttribute("day", dayArray);

		return "app/graph";
	}

	@GetMapping("/photos")
	public String photos(Model model) {
		return "app/photos";
	}

	@GetMapping("/memo")
	@ResponseBody
	public Condition memo(String day, @AuthenticationPrincipal UserDetailsImpl userDetails)
			throws JsonParseException, JsonMappingException, IOException {
		String user_name = userDetails.getUsername();
		System.out.println(day);
		Condition memo = conditionService.findMemo(day, user_name);
		return memo;
	}
}
