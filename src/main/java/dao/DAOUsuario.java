package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import connection.SingleConnectionBanco;
import model.Usuario;

public class DAOUsuario {

	private Connection connection;

	public DAOUsuario() {
		connection = SingleConnectionBanco.getConnection();
	}

	public Usuario gravarUsuario(Usuario usuario) throws Exception {

		if (usuario.isNovo()) {
			String sql = "INSERT INTO usuario(login, senha, nome, email) VALUES (?, ?, ?, ?)";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, usuario.getLogin());
			preparedStatement.setString(2, usuario.getSenha());
			preparedStatement.setString(3, usuario.getNome());
			preparedStatement.setString(4, usuario.getEmail());

			preparedStatement.execute();

			connection.commit();
		} else {
			String sql = "UPDATE usuario SET login=?, senha=?, nome=?, email=? WHERE id = " + usuario.getId() + ";";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, usuario.getLogin());
			preparedStatement.setString(2, usuario.getSenha());
			preparedStatement.setString(3, usuario.getNome());
			preparedStatement.setString(4, usuario.getEmail());

			preparedStatement.execute();

			connection.commit();
		}

		return this.consultaUsuario(usuario.getLogin());

	}

	public Usuario consultaUsuarioID(String id) throws Exception {
		Usuario usuario = new Usuario();

		String sql = "select * from usuario where id = ? ";

		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, Long.parseLong(id));

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) {
			usuario.setId(resultado.getLong("id"));
			usuario.setEmail(resultado.getString("email"));
			usuario.setLogin(resultado.getString("login"));
			usuario.setSenha(resultado.getString("senha"));
			usuario.setNome(resultado.getString("nome"));
		}

		return usuario;
	}

	public List<Usuario> consultaUsuarioList(String nome) throws Exception {
		List<Usuario> list = new ArrayList<Usuario>();

		String sql = "select * from usuario where nome like ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, "%" + nome + "%");

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) {
			Usuario usuario = new Usuario();

			usuario.setEmail(resultado.getString("email"));
			usuario.setId(resultado.getLong("id"));
			usuario.setLogin(resultado.getString("login"));
			usuario.setNome(resultado.getString("nome"));
			usuario.setSenha(resultado.getString("senha"));

			list.add(usuario);
		}

		return list;
	}

	public Usuario consultaUsuario(String login) throws Exception {
		Usuario usuario = new Usuario();
		
		String sql = "select * from usuario where upper(login) = upper('" + login + "')";

		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) {
			usuario.setId(resultado.getLong("id"));
			usuario.setEmail(resultado.getString("email"));
			usuario.setLogin(resultado.getString("login"));
			usuario.setSenha(resultado.getString("senha"));
			usuario.setNome(resultado.getString("nome"));
		}

		return usuario;
	}

	public boolean validarLogin(String login) throws Exception {
		String sql = "select count(1) > 0 as existe from usuario where upper(login) = upper('" + login + "');";

		PreparedStatement preparedStatement = connection.prepareStatement(sql);

		ResultSet resultado = preparedStatement.executeQuery();

		resultado.next();

		return resultado.getBoolean("existe");
	}

	public void deletarUser(String idUser) throws Exception {
		String sql = "DELETE FROM usuario WHERE id = ?;";

		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setLong(1, Long.parseLong(idUser));

		statement.executeUpdate();

		connection.commit();
	}

}
