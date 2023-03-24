package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAOLogin;
import dao.DAOUsuario;
import model.Usuario;

@WebServlet(urlPatterns = { "/ServletLogin", "/principal/ServletLogin" })
public class ServletLogin extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private DAOLogin daoLogin = new DAOLogin();
	private DAOUsuario daoUsuario = new DAOUsuario();

	public ServletLogin() {

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		String url = request.getParameter("url");

		try {

			if (login != null && !login.isEmpty() && senha != null && !senha.isEmpty()) {
				Usuario usuario = new Usuario();
				usuario.setLogin(login);
				usuario.setSenha(senha);

				if (daoLogin.validarAutenticacao(usuario)) {
					
					usuario = daoUsuario.consultaUsuarioLogado(login);

					request.getSession().setAttribute("usuarioLogado", usuario.getLogin());
					request.getSession().setAttribute("perfil", usuario.getPerfil());

					if (url == null || url.equals("null")) {
						url = "principal/principal.jsp";
					}
					RequestDispatcher redirecionar = request.getRequestDispatcher(url);
					redirecionar.forward(request, response);
				} else {
					RequestDispatcher redirecionar = request.getRequestDispatcher("index.jsp");
					request.setAttribute("msg", "Informe o login e senha corretamente!");
					redirecionar.forward(request, response);
				}

			} else {
				RequestDispatcher redirecionar = request.getRequestDispatcher("index.jsp");
				request.setAttribute("msg", "Informe o login e senha corretamente!");
				redirecionar.forward(request, response);
			}

		} catch (Exception e) {
			e.printStackTrace();
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}
	}

}
