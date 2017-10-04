package com.mkyong.helloworld.web;

import com.google.common.collect.ImmutableList;
import com.mkyong.helloworld.repository.ProjectModuleRepository;
import com.mkyong.helloworld.service.HelloWorldService;
import com.mkyong.helloworld.util.AppVersion;
import com.mkyong.helloworld.util.DatabaseModule;
import com.mkyong.helloworld.util.ProjectModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.Optional;

@Controller
public class WelcomeController {

	private final Logger logger = LoggerFactory.getLogger(WelcomeController.class);
	private final HelloWorldService helloWorldService;

	@Autowired
	private ProjectModuleRepository projectModuleRepository;

	@Autowired
	public WelcomeController(HelloWorldService helloWorldService) {
		this.helloWorldService = helloWorldService;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Map<String, Object> model) {

		logger.debug("index() is executed!");

		model.put("title", helloWorldService.getTitle(""));
		model.put("msg", helloWorldService.getDesc());
		model.put("appVersion", AppVersion.getInstance().getShortVersion());
		model.put("commitYear", AppVersion.getInstance().getCommitTime().getYear());
		model.put("moduleNames", createModuleNames());
		
		return "index";
	}

	@RequestMapping(value = "/hello/{name:.+}", method = RequestMethod.GET)
	public ModelAndView hello(@PathVariable("name") String name) {

		logger.debug("hello() is executed - $name {}", name);

		ModelAndView model = new ModelAndView();
		model.setViewName("index");
		
		model.addObject("title", helloWorldService.getTitle(name));
		model.addObject("msg", helloWorldService.getDesc());
		
		return model;

	}

	private String createModuleNames() {
		return String.join(", ", projectModuleRepository.getProjectModules().stream()
				.map(this::createModuleName)
				.collect(ImmutableList.toImmutableList()));
	}

	private String createModuleName(ProjectModule pm) {
		Optional<DatabaseModule> dbModule = projectModuleRepository.getDatabaseModule(pm);
		return dbModule
				.map(databaseModule -> pm.getModuleName() + "(" + databaseModule.getDatabaseType().name() + ")")
				.orElseGet(pm::getModuleName);
	}

}
