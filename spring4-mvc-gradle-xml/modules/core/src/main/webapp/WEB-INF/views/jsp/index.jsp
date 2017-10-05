<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="${pageContext.response.locale}">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<meta name="description" content="<spring:message code="welcome.app.description" />">
	<meta name="author" content="<spring:message code="welcome.app.author" />">
	<title><spring:message code="welcome.app.name" /></title>
	<!-- Favicon -->
	<spring:url value="/resources/core/favicon.ico" var="faviconIco" />
    <link rel="icon" href="${faviconIco}">
	<!-- Bootstrap core CSS -->
	<spring:url value="/resources/core/css/bootstrap.min.css" var="bootstrapCss" />
	<link href="${bootstrapCss}" rel="stylesheet" />
	<!-- Custom styles for this template -->
	<spring:url value="/resources/core/css/narrow-jumbotron.css" var="jumbotronCss" />
	<link href="${jumbotronCss}" rel="stylesheet" />
</head>
<body>
<div class="container">
	<div class="header clearfix">
		<nav></nav>
		<h3 class="text-muted"><spring:message code="welcome.app.name" /></h3>
	</div>
	<div class="jumbotron">
		<h1 class="display-3"><spring:message code="welcome.header.it_works" /></h1>
	</div>
	<div class="row marketing">
		<div class="col-lg-6">
			<h4><spring:message code="welcome.title.package" /></h4>
			<p>${packageName}</p>

			<h4><spring:message code="welcome.title.version" /></h4>
			<p>${appVersion}</p>

			<h4><spring:message code="welcome.title.revision" /></h4>
			<p>${revision}</p>

			<h4><spring:message code="welcome.title.modules" /></h4>
			<p>${moduleNames}</p>
		</div>
		<div class="col-lg-6">
			<h4><spring:message code="welcome.title.supported_database" /></h4>
			<p>${supportedDatabase}</p>

			<h4><spring:message code="welcome.title.commit_time" /></h4>
			<p>${commitTime}</p>

			<h4><spring:message code="welcome.title.build_time" /></h4>
			<p>${buildTime}</p>

			<h4><spring:message code="welcome.title.server" /></h4>
			<p>${serverInfo} ${osName}</p>
		</div>
	</div>
	<footer class="footer">
		<p><spring:message code="welcome.footer.copyright" /> ${commitYear}</p>
	</footer>
</div> <!-- /container -->

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<spring:url value="/resources/core/js/ie10-viewport-bug-workaround.js" var="ie10workaroundJs" />
<script src="${ie10workaroundJs}"></script>

</body>
</html>
