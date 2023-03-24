package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.DAOUsuario;
import model.Usuario;

@WebServlet("/ServletUsuarioController")
public class ServletUsuarioController extends ServletGenericUtil {

	private static final long serialVersionUID = 1L;

	private DAOUsuario daoUsuario = new DAOUsuario();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String acao = request.getParameter("acao");

			if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("deletar")) {
				String idUser = request.getParameter("id");
				daoUsuario.deletarUser(idUser);

				List<Usuario> usuarios = daoUsuario.consultaUsuarioList("", super.getUserLogado(request));
				request.setAttribute("usuarios", usuarios);
				request.setAttribute("msg", "Excluído com sucesso");
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
			} else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("deletarajax")) {
				String idUser = request.getParameter("id");
				daoUsuario.deletarUser(idUser);
				response.getWriter().write("Excluído com sucesso");
			} else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarUserAjax")) {
				String nomeBusca = request.getParameter("nomeBusca");

				List<Usuario> dadosJsonUser = daoUsuario.consultaUsuarioList(nomeBusca, super.getUserLogado(request));

				ObjectMapper mapper = new ObjectMapper();

				String json = mapper.writeValueAsString(dadosJsonUser);

				response.getWriter().write(json);
			} else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarEditar")) {
				String id = request.getParameter("id");

				Usuario usuario = daoUsuario.consultaUsuarioID(id, super.getUserLogado(request));

				List<Usuario> usuarios = daoUsuario.consultaUsuarioList("", super.getUserLogado(request));
				request.setAttribute("usuarios", usuarios);
				request.setAttribute("msg", "Usuário em edição");
				request.setAttribute("usuario", usuario);
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
			} else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("listarUser")) {
				List<Usuario> usuarios = daoUsuario.consultaUsuarioList("", super.getUserLogado(request));
				request.setAttribute("msg", "Usuários carregados");
				request.setAttribute("usuarios", usuarios);
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
			}

			else {
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {

			request.setCharacterEncoding("UTF-8");
			String msg;

			String id = request.getParameter("id");
			String nome = request.getParameter("nome");
			String email = request.getParameter("email");
			String login = request.getParameter("login");
			String senha = request.getParameter("senha");
			String perfil = request.getParameter("perfil");
			String sexo = request.getParameter("sexo");

			Usuario usuario = new Usuario();

			usuario.setId(id != null && !id.isEmpty() ? Long.parseLong(id) : null);
			usuario.setNome(nome);
			usuario.setEmail(email);
			usuario.setLogin(login);
			usuario.setSenha(senha);
			usuario.setPerfil(perfil);
			usuario.setSexo(sexo);

			if (daoUsuario.validarLogin(usuario.getLogin()) && usuario.getId() == null) {
				msg = "Já existe usuário com o mesmo login, informe outro login";
			} else {
				if (usuario.isNovo()) {
					msg = "Gravado com sucesso!";
				} else {
					msg = "Atualizado com sucesso!";
				}
				usuario = daoUsuario.gravarUsuario(usuario, super.getUserLogado(request));
			}

			List<Usuario> usuarios = daoUsuario.consultaUsuarioList("", super.getUserLogado(request));

			request.setAttribute("msg", msg);
			request.setAttribute("usuarios", usuarios);
			request.setAttribute("usuario", usuario);
			request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}

	}

}
