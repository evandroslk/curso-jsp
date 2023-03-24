package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.DAOUsuario;
import model.Usuario;

@MultipartConfig
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
			} else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("downloadFoto")) {
				String idUser = request.getParameter("id");
				
				Usuario usuario = daoUsuario.consultaUsuarioID(idUser, super.getUserLogado(request));
				if (usuario.getFotouser() != null && !usuario.getFotouser().isEmpty()) {
					response.setHeader("Content-Disposition", "attachment;filename=arquivo." + usuario.getExtensaofotouser());
					response.getOutputStream().write(Base64.decodeBase64(usuario.getFotouser().split("\\,")[1]));
				}
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
			String cep = request.getParameter("cep");
			String logradouro = request.getParameter("logradouro");
			String bairro = request.getParameter("bairro");
			String localidade = request.getParameter("localidade");
			String uf = request.getParameter("uf");
			String numero = request.getParameter("numero");

			Usuario usuario = new Usuario();

			usuario.setId(id != null && !id.isEmpty() ? Long.parseLong(id) : null);
			usuario.setNome(nome);
			usuario.setEmail(email);
			usuario.setLogin(login);
			usuario.setSenha(senha);
			usuario.setPerfil(perfil);
			usuario.setSexo(sexo);
			usuario.setCep(cep);
			usuario.setLogradouro(logradouro);
			usuario.setBairro(bairro);
			usuario.setLocalidade(localidade);
			usuario.setUf(uf);
			usuario.setNumero(numero);

			if (ServletFileUpload.isMultipartContent(request)) {
				Part part = request.getPart("filefoto");

				if (part != null && part.getSize() > 0) {
					byte[] foto = IOUtils.toByteArray(part.getInputStream());
					String imagemBase64 = "data:image/" + part.getContentType().split("\\/")[1] + ";base64," + Base64.encodeBase64String(foto);
					usuario.setFotouser(imagemBase64);
					usuario.setExtensaofotouser(part.getContentType().split("\\/")[1]);
				}
			}

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
