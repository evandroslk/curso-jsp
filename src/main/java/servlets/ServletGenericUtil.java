package servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import dao.DAOUsuario;

public class ServletGenericUtil extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private DAOUsuario daoUsuario = new DAOUsuario();

	public Long getUserLogado(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		String usuarioLogado = (String) session.getAttribute("usuarioLogado");
		return daoUsuario.consultaUsuarioLogado(usuarioLogado).getId();
	}

}
