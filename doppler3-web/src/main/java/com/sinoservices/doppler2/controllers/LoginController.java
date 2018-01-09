package com.sinoservices.doppler2.controllers;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({ "/${module-name}/view" })
public class LoginController {

	@RequestMapping({ "/dashboard" })
	public ModelAndView login(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		request.getSession().setAttribute("username", username);
		if (("admin".equals(username)) && ("123456".equals(password))) {
			return new ModelAndView("index.shtml");
		}

		Map<String, String> message = new HashMap<String, String>();
		message.put("message", "用户名或密码不正确！");
		return new ModelAndView("login.shtml", message);
	}

	@RequestMapping({ "/logout" })
	public ModelAndView logout() {
		return new ModelAndView("login.shtml");
	}
}