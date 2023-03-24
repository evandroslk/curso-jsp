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

	public Usuario gravarUsuario(Usuario usuario, Long userLogado) throws Exception {

		if (usuario.isNovo()) {
			String sql = "INSERT INTO usuario(login, senha, nome, email, usuario_id, perfil, sexo, useradmin) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, usuario.getLogin());
			preparedStatement.setString(2, usuario.getSenha());
			preparedStatement.setString(3, usuario.getNome());
			preparedStatement.setString(4, usuario.getEmail());
			preparedStatement.setLong(5, userLogado);
			preparedStatement.setString(6, usuario.getPerfil());
			preparedStatement.setString(7, usuario.getSexo());
			preparedStatement.setBoolean(8, false);

			preparedStatement.execute();

			connection.commit();
		} else {
			String sql = "UPDATE usuario SET login=?, senha=?, nome=?, email=?, perfil=?, sexo=? WHERE id = " + usuario.getId() + ";";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, usuario.getLogin());
			preparedStatement.setString(2, usuario.getSenha());
			preparedStatement.setString(3, usuario.getNome());
			preparedStatement.setString(4, usuario.getEmail());
			preparedStatement.setString(5, usuario.getPerfil());
			preparedStatement.setString(6, usuario.getSexo());

			preparedStatement.execute();

			connection.commit();
		}

		return this.consultaUsuario(usuario.getLogin(), userLogado);

	}

	public Usuario consultaUsuarioID(String id, Long userLogado) throws Exception {
		Usuario usuario = new Usuario();

		String sql = "select * from usuario where id = ? and useradmin is false and usuario_id = ?";

		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, Long.parseLong(id));
		statement.setLong(2, userLogado);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) {
			usuario.setId(resultado.getLong("id"));
			usuario.setEmail(resultado.getString("email"));
			usuario.setLogin(resultado.getString("login"));
			usuario.setSenha(resultado.getString("senha"));
			usuario.setNome(resultado.getString("nome"));
			usuario.setPerfil(resultado.getString("perfil"));
			usuario.setSexo(resultado.getString("sexo"));
		}

		return usuario;
	}

	public List<Usuario> consultaUsuarioList(String nome, Long userLogado) throws Exception {
		List<Usuario> list = new ArrayList<Usuario>();

		String sql = "select * from usuario where nome like ? and useradmin is false and usuario_id = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, "%" + nome + "%");
		statement.setLong(2, userLogado);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) {
			Usuario usuario = new Usuario();

			usuario.setEmail(resultado.getString("email"));
			usuario.setId(resultado.getLong("id"));
			usuario.setLogin(resultado.getString("login"));
			usuario.setNome(resultado.getString("nome"));
			usuario.setSenha(resultado.getString("senha"));
			usuario.setPerfil(resultado.getString("perfil"));
			usuario.setSexo(resultado.getString("sexo"));

			list.add(usuario);
		}

		return list;
	}

	public Usuario consultaUsuario(String login, Long userLogado) throws Exception {
		Usuario usuario = new Usuario();
		
		String sql = "select * from usuario where login = '" + login + "' and useradmin is false and usuario_id = " + userLogado;

		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) {
			usuario.setId(resultado.getLong("id"));
			usuario.setEmail(resultado.getString("email"));
			usuario.setLogin(resultado.getString("login"));
			usuario.setSenha(resultado.getString("senha"));
			usuario.setNome(resultado.getString("nome"));
			usuario.setUseradmin(resultado.getBoolean("useradmin"));
			usuario.setPerfil(resultado.getString("perfil"));
			usuario.setSexo(resultado.getString("sexo"));
		}

		return usuario;
	}

	public boolean validarLogin(String login) throws Exception {
		String sql = "select count(1) > 0 as existe from usuario where login = '" + login + "'";

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

	public Usuario consultaUsuarioLogado(String login) throws Exception {
		Usuario usuario = new Usuario();
		
		String sql = "select * from usuario where login = '" + login + "'";

		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) {
			usuario.setId(resultado.getLong("id"));
			usuario.setEmail(resultado.getString("email"));
			usuario.setLogin(resultado.getString("login"));
			usuario.setSenha(resultado.getString("senha"));
			usuario.setNome(resultado.getString("nome"));
			usuario.setUseradmin(resultado.getBoolean("useradmin"));
			usuario.setPerfil(resultado.getString("perfil"));
			usuario.setSexo(resultado.getString("sexo"));
		}

		return usuario;
	}

}
