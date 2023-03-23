package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import connection.SingleConnectionBanco;
import model.Usuario;

public class DAOLogin {

	private Connection connection;

	public DAOLogin() {
		connection = SingleConnectionBanco.getConnection();
	}

	public boolean validarAutenticacao(Usuario usuario) throws Exception {

		String sql = "select * from usuario where upper(login) = upper(?) and upper(senha) = upper(?) ";

		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setString(1, usuario.getLogin());
		statement.setString(2, usuario.getSenha());

		ResultSet resultSet = statement.executeQuery();

		if (resultSet.next()) {
			return true;
		}

		return false;

	}

}
