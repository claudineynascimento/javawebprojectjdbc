<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Cadastro de Tipo de Ocorrência</title>
</head>
<body>
<h3>Cadastro de Tipo de Ocorrência</h3>

<%
String id = request.getParameter("id");
String nomeOcorrencia = request.getParameter("nomeOcorrencia");
String mensagem = request.getParameter("mensagem");
if(mensagem != null) { %>
<p><%=mensagem%></p>
<% } %>

<form name="formOcorrencia" action="./Ocorrencia" method="POST">
<%	if(id != null) { %> <input type="hidden" name="id" value="<%=id%>"> <% } %>
	<label>Nome do Tipo de Ocorrência:</label><br/>
<%	if(nomeOcorrencia != null) { %>
	<input type="text" name="nomeOcorrencia" value="<%=nomeOcorrencia%>">
<%	} else { %>
	<input type="text" name="nomeOcorrencia">
<%	} %>
	<br/> <br/>
	<input type="submit" value="Salvar">
	<input type="button" onclick="location.href='./listaOcorrencia.jsp'" value="Voltar">
</form>

<br/>

</body>
</html>