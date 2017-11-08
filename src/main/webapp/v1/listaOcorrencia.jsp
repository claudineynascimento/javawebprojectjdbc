<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="br.eti.claudiney.curso.web.util.UtilConexao"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%
Connection connection = null;
try {
	Class.forName(UtilConexao.JDBC_DRIVER).newInstance();
	connection = DriverManager.getConnection(UtilConexao.JDBC_URL, UtilConexao.USUARIO, UtilConexao.SENHA);
} catch(Exception e) {
	e.printStackTrace();
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Lista de Tipo de Ocorrências</title>
</head>
<body>
<h3>Lista de Tipo de Ocorrências</h3>
<%
String mensagem = request.getParameter("mensagem");
if(mensagem != null) { %>
<p><%=mensagem%></p>
<%	} %>
<%	if(connection == null) { %>
<p>Não foi possível conectar com o banco de dados. Tente novamente efetuando o refresh dessa página</p>
<%	} else { %>
<table border="1" cellpadding="5" cellspacing="0">
	<thead>
		<tr>
			<th>Código</th>
			<th>Descrição</th>
			<th>Ação</th>
		</tr>
	</thead>
	<tbody> <%
	try {
	
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery("SELECT ID, NM_OCR FROM TAB_OCR");
		while(rs.next()) { 
			Integer id = rs.getInt("ID"); %>
		<tr>
			<td><%= id %></td>
			<td><%= rs.getString("NM_OCR") %></td>
			<td>
				<input type="button" value="Remover" onclick="location.href='./Ocorrencia?acao=remover&id=<%=id%>'">
				<input type="button" value="Editar" onclick="location.href='./Ocorrencia?acao=editar&id=<%=id%>'">
			</td>
		</tr> <%
 		}
		rs.close();
		st.close();
	} catch(Exception e) { %>
<%	} finally { connection.close(); } %>
	</tbody>
</table>
<%	} %>
<br/>
<input type="button" onclick="location.href='formOcorrencia.jsp'" value="Incluir Ocorrência">
</body>
</html>