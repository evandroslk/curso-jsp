package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.Usuario;

@WebFilter(urlPatterns = { "/principal/*" })
public class FilterAutenticacao implements Filter {


	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();

		String urlParaAutenticar = req.getServletPath();

		Usuario usuario = (Usuario) session.getAttribute("usuario");
		
		response.setCharacterEncoding("UTF-8");

		if (usuario == null && !urlParaAutenticar.equalsIgnoreCase("/principal/ServletLogin")) {
			RequestDispatcher redireciona = request.getRequestDispatcher("/index.jsp?url=" + urlParaAutenticar);
			request.setAttribute("msg", "Por favor realize o login!");
			redireciona.forward(request, response);
		} else {
			chain.doFilter(req, response);
		}

	}

	public void init(FilterConfig filterConfig) throws ServletException {

	}

}
