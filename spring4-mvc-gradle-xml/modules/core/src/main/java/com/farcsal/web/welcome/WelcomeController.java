package com.farcsal.web.welcome;

import com.google.common.collect.ImmutableList;
import com.farcsal.logic.service.module.AppVersion;
import com.farcsal.logic.service.module.Package;
import com.farcsal.logic.service.module.PackageService;
import com.farcsal.web.util.locale.DateTimeFormatterFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletContext;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@Controller
public class WelcomeController {

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private DateTimeFormatterFactory dateTimeFormatterFactory;

	@Autowired
	private PackageService packageService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Map<String, Object> model) {
		log.debug("index() is executed!");
		final DateTimeFormatter dateTimeFormatter = dateTimeFormatterFactory.createLocalizedMediumDateTimeFormatter();
		final Package pkg = packageService.getPackage();
		model.put("packageName", pkg.getName());
		model.put("appVersion", AppVersion.getInstance().getShortVersion());
		model.put("revision", AppVersion.getInstance().getRevision());
		model.put("commitYear", AppVersion.getInstance().getCommitTime().getYear());
		model.put("commitTime", dateTimeFormatter.format(AppVersion.getInstance().getCommitTime()));
		model.put("buildTime", dateTimeFormatter.format(AppVersion.getInstance().getBuildTime()));
		model.put("moduleNames", createModuleNames(pkg));
		model.put("flavorNames", createFlavorNames(pkg));
		model.put("supportedDatabase", createSupportedDatabase(pkg));
		model.put("serverInfo", servletContext.getServerInfo());
		model.put("osName", createOs());
		return "index";
	}

	private String createModuleNames(Package pkg) {
		return String.join(", ", pkg.getProjectModules().stream()
				.map(Package.ProjectModule::getName)
				.collect(ImmutableList.toImmutableList()));
	}

	private String createFlavorNames(Package pkg) {
		String names = String.join(", ", pkg.getFlavorModules().stream()
				.map(Package.FlavorModule::getName)
				.collect(ImmutableList.toImmutableList()));
		return names.isEmpty() ? "-" : names;
	}

	private String createSupportedDatabase(Package pkg) {
		return pkg.getDatabaseModule()
				.map(Package.DatabaseModule::getUpperCamelDatabaseTypeName)
				.orElse("-");
	}

	private String createOs() {
		return System.getProperty("os.name");
	}

}
