package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
* Classe responsável por incluir no banco de dados os dados do movimento efetuado
*/


public class MovimentoDAO extends MySQLConnect {
	private PreparedStatement st;

	public MovimentoDAO() {
		super();
		st = null;
	}// fim do construtor

	// Método responsável por inserir dados no banco de dados
	public boolean incluirMovimento(String agencia, String conta, String tipoMovimento, String debitoCredito,
									String data, String hora, String descricao, double valor) {
		boolean sucesso = false;
		try {
			int idConta = -1;
			String sql = "SELECT idConta FROM conta WHERE agencia = ? AND conta = ?";
			st = conn.prepareStatement(sql);
			st.setString(1, agencia);
			st.setString(2, conta);
			ResultSet resultSet = st.executeQuery();
			while (resultSet.next()) {
				idConta = resultSet.getInt("idConta");
			}
			st.close();
			conn.setAutoCommit(false);
			sql = "INSERT INTO movimento (idConta, tipoMovimento, debitoCredito, data, hora, descricao, valor) "; 
			sql += "VALUES(?, ?, ?, ?, ?, ?, ?);";
			st = conn.prepareStatement(sql);
			st.setInt(1, idConta);
			st.setString(2, tipoMovimento);
			st.setString(3, debitoCredito);
			st.setString(4, data);
			st.setString(5, hora);
			st.setString(6, descricao);
			st.setDouble(7, valor);
			st.executeUpdate();
			sucesso = true;
			st.close();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// db.closeConnection();
		return sucesso;
	}// fim do método

	// Método para consulta das operações realizadas
	public String consultarOperacao() {
		ResultSet rs;
		String conteudo = "";
		String retorno = "Lista vazia !";
		try {
			String sql = "SELECT * FROM MOVIMENTO ORDER BY idMov";
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
			while (rs.next()) {
				conteudo += "Codigo   : " + rs.getInt("idCodigo") + "\n";
				conteudo += "Operacao : " + rs.getInt("tipoOperacao") + "\n";
				conteudo += "Data     : " + rs.getString("dataMov") + "\n";
				conteudo += "Hora     : " + rs.getString("horaMov") + "\n";
				conteudo += "Valor    : " + rs.getDouble("valor") + "\n\n";
				retorno = conteudo;
			}
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// db.closeConnection();
		return retorno;

	}// fim do método

}// fim da classe