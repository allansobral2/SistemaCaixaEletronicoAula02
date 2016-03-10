package DAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	static{
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	//Obtém conexão com o banco de dados
	public static Connection obtemConexao() throws SQLException{
		return DriverManager
				
	.getConnection("jdbc:mysql://localhost/tutorial?user=alunos&password=alunos");
	}
	
	
}// fim da classe de conexão 
