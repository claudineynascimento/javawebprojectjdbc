package br.eti.claudiney.curso.web.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.eti.claudiney.curso.web.util.UtilConexao;

public final class OcorrenciaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public OcorrenciaServlet() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/*
		 * Recebe o nome da ocorrência
		 */

		String id = request.getParameter("id");
		String nomeOcorrencia = request.getParameter("nomeOcorrencia");

		if (nomeOcorrencia == null || nomeOcorrencia.trim().isEmpty()) {
			response.sendRedirect("formOcorrencia.jsp?mensagem="
					+ URLEncoder.encode("Nome da Ocorrência não informado", "ISO-8859-1"));
			return;
		}

		/*
		 * Grava a ocorrência no banco de dados
		 */
		Connection connection = null;
		try {
			
			Class.forName(UtilConexao.JDBC_DRIVER).newInstance();
			
			connection = DriverManager.getConnection(
					UtilConexao.JDBC_URL,
					UtilConexao.USUARIO,
					UtilConexao.SENHA);
			
		} catch (Exception e) {
			response.sendRedirect("formOcorrencia.jsp?mensagem="
					+ URLEncoder.encode("Erro ao conectar com o banco de dados: " + e.getMessage(), "ISO-8859-1"));
			return;
		}

		try {

			PreparedStatement st = null;
			
			if(id == null) {
				st = connection.prepareStatement("INSERT INTO TAB_OCR (NM_OCR) VALUES (?)");
				st.setString(1, nomeOcorrencia);
			} else {
				st = connection.prepareStatement("UPDATE TAB_OCR SET NM_OCR = ? WHERE ID = ?");
				st.setString(1, nomeOcorrencia);
				st.setInt(2, Integer.parseInt(id));
			}

			st.executeUpdate();

			st.close();

		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("formOcorrencia.jsp?mensagem=" + URLEncoder
					.encode("Erro ao gravar ocorrência no banco de dados: " + e.getMessage(), "ISO-8859-1"));
			return;
		} finally {
			try {
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/*
		 * Retorna para lista de ocorrencias
		 */
		response.sendRedirect("listaOcorrencia.jsp");

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String acao = request.getParameter("acao");
		
		if( "editar".equals(acao) ) {
			buscar(request, response);
		} else if("remover".equals(acao)) {
			excluir(request, response);
		} else {
			response.sendRedirect("listaOcorrencia.jsp?mensagem="
					+ URLEncoder.encode("Ação inválida", "ISO-8859-1"));
			return;
		}
		
	}
	
	private void buscar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String id = request.getParameter("id");
		
		/*
		 * Conecta com o banco de dados
		 */
		Connection connection = null;
		try {
			
			Class.forName(UtilConexao.JDBC_DRIVER).newInstance();
			
			connection = DriverManager.getConnection(
					UtilConexao.JDBC_URL,
					UtilConexao.USUARIO,
					UtilConexao.SENHA);
			
		} catch (Exception e) {
			response.sendRedirect("listaOcorrencia.jsp?mensagem="
					+ URLEncoder.encode("Erro ao conectar com o banco de dados: " + e.getMessage(), "ISO-8859-1"));
			return;
		}
		
		/*
		 * Busca ocorrência do banco de dados
		 */
		
		String nomeOcorrencia = null;
		
		try {
			
			PreparedStatement st = connection.prepareStatement("SELECT ID, NM_OCR FROM TAB_OCR WHERE ID = ?");
			
			st.setInt(1, Integer.parseInt(id) );
			
			ResultSet rs = st.executeQuery();
			
			if( rs.next() ) {
				nomeOcorrencia = rs.getString("NM_OCR").trim();
			}
			
			rs.close();
			st.close();
			
		} catch(Exception e) {
			response.sendRedirect("listaOcorrencia.jsp?mensagem="
					+ URLEncoder.encode("Erro ao recuperar ocorrência do banco de dados: " + e.getMessage(), "ISO-8859-1"));
			return;
		} finally {
			try {
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		response.sendRedirect("formOcorrencia.jsp?id="+id+"&nomeOcorrencia="+URLEncoder.encode(nomeOcorrencia, "ISO-8859-1"));
		
	}
	
	private void excluir(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String id = request.getParameter("id");
		
		/*
		 * Conecta com o banco de dados
		 */
		Connection connection = null;
		try {
			
			Class.forName(UtilConexao.JDBC_DRIVER).newInstance();
			
			connection = DriverManager.getConnection(
					UtilConexao.JDBC_URL,
					UtilConexao.USUARIO,
					UtilConexao.SENHA);
			
		} catch (Exception e) {
			response.sendRedirect("listaOcorrencia.jsp?mensagem="
					+ URLEncoder.encode("Erro ao conectar com o banco de dados: " + e.getMessage(), "ISO-8859-1"));
			return;
		}
		
		/*
		 * Busca ocorrência do banco de dados
		 */
		
		try {
			
			PreparedStatement st = connection.prepareStatement("DELETE FROM TAB_OCR WHERE ID = ?");
			
			st.setInt(1, Integer.parseInt(id) );
			st.executeUpdate();
			st.close();
			
		} catch(Exception e) {
			response.sendRedirect("listaOcorrencia.jsp?mensagem="
					+ URLEncoder.encode("Erro ao recuperar ocorrência do banco de dados: " + e.getMessage(), "ISO-8859-1"));
			return;
		} finally {
			try {
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		response.sendRedirect("listaOcorrencia.jsp");
		
	}

}
