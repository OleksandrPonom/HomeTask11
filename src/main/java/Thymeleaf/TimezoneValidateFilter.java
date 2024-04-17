package Thymeleaf;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.ZoneId;


@WebFilter(value = "/time")
public class TimezoneValidateFilter extends HttpFilter {
	@Override
	protected void doFilter(HttpServletRequest req,
							HttpServletResponse resp,
							FilterChain chain) throws IOException,
			ServletException {


		String timezone = req.getQueryString();
		if(req.getQueryString() == null) {
			timezone = "UTC";
		}
		try {
			ZoneId zoneId = ZoneId.of(timezone.substring(timezone.indexOf("=")+1));

			chain.doFilter(req, resp);
		} catch(DateTimeException e){
			resp.setStatus(400);

			resp.setContentType("text/html");
			resp.getWriter().write("Invalid timezone");
			resp.getWriter().close();

		}
	}
}
