package Thymeleaf;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JavaxServletWebApplication;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/time")
public class ThymeleafServlet extends HttpServlet {

	private TemplateEngine engine;

	@Override
	public void init() throws ServletException {
		engine = new TemplateEngine();

		JavaxServletWebApplication jswa =
				JavaxServletWebApplication.buildApplication(this.getServletContext());

		WebApplicationTemplateResolver resolver =
				new WebApplicationTemplateResolver(jswa);

		resolver.setPrefix("/WEB-INF/templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode("HTML5");
		resolver.setOrder(engine.getTemplateResolvers().size());
		resolver.setCacheable(false);
		engine.addTemplateResolver(resolver);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html");

		resp.addCookie(new Cookie("lastTimezone", "timezone=UTC"));

		String parameter = null;
		Cookie[] cookies = req.getCookies();
		for (Cookie cookie : cookies){
			parameter = cookie.getValue();
		}

		if(req.getQueryString() != null) {
			resp.addCookie(new Cookie("lastTimezone", req.getQueryString()));
		}

		String param = ZonedDateTime.now(ZoneId.of(
					parameter.substring(
						parameter.indexOf("=")+1)))
				.format(DateTimeFormatter.ofPattern(
							"dd-MM-yyyy HH:mm:ss v"));

		Context data = new Context(req.getLocale(), Map.of("queryParams", param));

		engine.process("test", data, resp.getWriter());
		resp.getWriter().close();
	}
}
