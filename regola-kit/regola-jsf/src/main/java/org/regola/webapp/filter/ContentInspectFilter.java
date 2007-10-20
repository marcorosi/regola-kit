package org.regola.webapp.filter;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ContentInspectFilter implements Filter {

	private Object filterConfig;

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		ResponseWrapper wrapper = new ResponseWrapper(
				(HttpServletResponse) response);

		PrintWriter out = response.getWriter();
		
		try {
			chain.doFilter(request, wrapper);
		} finally {
			
			String content = wrapper.toString();
			if(wrapper.getContentType()!=null && wrapper.getContentType().indexOf("text/html")!=-1) {
				content=removeTag(content);
			}
			//PrintWriter out = response.getWriter();
			out.write(content);
			out.flush();
		}

	}
	
	private static String replace(String pattern, String html)
	{
		Pattern pat = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pat.matcher(html);
		return matcher.replaceAll("");		
	}
	
	public String removeTag(String html)
	{
		html = replace("<noscript>.*noscript>", html);
		
		return html;
	}

	public void init(FilterConfig arg0) throws ServletException {
		this.filterConfig = filterConfig;

	}

	public class ResponseWrapper extends HttpServletResponseWrapper {
		private CharArrayWriter output;

		public String toString() {
			return output.toString();
		}

		public ResponseWrapper(HttpServletResponse response) {
			super(response);
			output = new CharArrayWriter();
		}

		public PrintWriter getWriter() {
			return new PrintWriter(output);
		}
	}

}
