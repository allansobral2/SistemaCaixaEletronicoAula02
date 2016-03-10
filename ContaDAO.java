package DAO;
import TO.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Connection;

import model.*;

import javax.swing.JOptionPane;

import java.sql.PreparedStatement;

import model.Conta;

public class ContaDAO extends MySQLConnect {

	// prepara a consulta sql
	private PreparedStatement 		 st;
	private Conta 			  		 contaModel;
	private static String 	  		 agencia;
	private static String 	  		 conta;
	private static ArrayList<Conta>  dados = new ArrayList<Conta>();

	public ContaDAO() { 
		super();
		st 		   = null;
		contaModel = new Conta();
		agencia    = "";
		conta 	   = "";
	}

	public void dadosConta(String agencia, String conta) {
		ContaDAO.agencia = agencia;
		ContaDAO.conta 	 = conta;
		String sql = "";
		sql += "SELECT conta, banco, agencia, nome, tipoPessoa, saldo ";
		sql += "FROM conta ";
		sql += "WHERE agencia = ? AND conta = ? ";
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, agencia);
			st.setString(2, conta);
			ResultSet resultSet = st.executeQuery();
			while (resultSet.next()) {
				contaModel.setConta(resultSet.getString("conta"));
				contaModel.setBanco(resultSet.getString("banco"));
				contaModel.setAgencia(resultSet.getString("agencia"));
				contaModel.setNome(resultSet.getString("nome"));
				contaModel.setTipoPessoa(resultSet.getString("tipoPessoa"));
				contaModel.setSaldo(resultSet.getDouble("saldo"));
				dados.add(contaModel);
				// Atualiza o nome do cliente na visualizacao 
				view.base.Frame frm = new view.base.Frame();
				frm.setNome(resultSet.getString("nome"));
			}
			st.close();
		} catch (SQLException e) {
			//ErrorMessage.setErrorMessage(e.getLocalizedMessage());
			//ErrorMessage.showErrorMessage();
			JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
		} catch (NullPointerException np){
			JOptionPane.showMessageDialog(null, np.getLocalizedMessage());
		}
		//closeConnection();
	}

	public static double consultaSaldo(ContaTo to) {
		double saldo = 0.0;
		for (int i=0; i<dados.size(); i++){
			if(to.getAgencia().equals(agencia) && to.getConta().equals(conta)){
				saldo = to.getSaldo();
			}
		}
		return saldo;
	}

	public boolean atualizarSaldo(ContaTo to, char op) {
		boolean sucesso = false;
		if (op == '+'){
			try {
				conn.setAutoCommit(false);
				String sql = "UPDATE conta SET saldo = saldo + ? WHERE agencia = ? AND conta = ?;";
				st   = conn.prepareStatement(sql);
				st.setDouble(1, to.getSaldo());
				st.setString(2, to.getAgencia());
				st.setString(3, to.getConta());
				st.executeUpdate();
				st.close();
				sucesso = true;
				conn.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (op == '-'){
			try {
				conn.setAutoCommit(false);
				String sql = "UPDATE conta SET saldo = saldo - ? WHERE agencia = ? AND conta = ?;";
				st   = conn.prepareStatement(sql);
				st.setDouble(1, to.getSaldo());
				st.setString(2, to.getAgencia());
				st.setString(3, to.getConta());
				st.executeUpdate();
				st.close();
				sucesso = true;
				conn.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// db.closeOperation();
		return sucesso;
	}
	public String consultarExtrato(ContaTo to)
	{ 	
		
		String msg = "";
		String sql = "SELECT * FROM MOVIMENTO where tipoOperacao = 1004";
		try(Connection conn = ConnectionFactory.obtemConexao(); 
				PreparedStatement stm = conn.prepareStatement(sql);){
				to.setBanco(to.getBanco());
				to.setConta(to.getConta());
				to.setSaldo(to.getSaldo());		
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("\n Não coi cossível Coletar o dado solicitado!");
		}
		return msg;
	}//fim do método
	
	public static String agenciaLogada() { return agencia; }

	public static String contaLogada() 	 { return conta;   }

}// fim da classe