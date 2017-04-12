package haibo.yan.io.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class TestController {
	@RequestMapping(value = "/testing")
	public String goTest(Model model) {
		model.addAttribute("test", "My Jesus");
		return "Test";
	}
}
