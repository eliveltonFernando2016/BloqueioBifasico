package DAO;

import Model.Operacao;
import Produtor.Schedule;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TransacaoDao {
    private static MinhaConexao minhaConexao;

    public TransacaoDao() {
        minhaConexao = new MinhaConexao();
        minhaConexao.getConnection();
    }

    public static void gravarTransacoes(Schedule schedule) throws SQLException {
        Operacao operacao = null;

        Connection conn = minhaConexao.getConnection();
        String sql = "INSERT INTO schedule(indiceTransacao, operacao, itemDado, timestampj, flag) VALUES (?, ?, ?, ?, 0)";
        PreparedStatement stmt = null;
        
        while(!schedule.getScheduleInList().isEmpty()) {
                operacao = schedule.getScheduleInList().removeFirst();
                try {
                    stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, operacao.getIndice());
                    stmt.setString(2, operacao.getAcesso().texto);
                    stmt.setString(3, operacao.getDado().getNome());
                    stmt.setString(4, new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println("Erro na insercao da transacao");
                    e.printStackTrace();
                }
        }
        try {
            minhaConexao.releaseAll(stmt, conn);
        } catch (SQLException e) {
            System.out.println("Erro ao encerrar conexão");
            e.printStackTrace();
        } finally{
            minhaConexao.release(conn);
        }
    }

    public static int pegarUltimoIndice() throws SQLException {
        int ultimoIndice = 0;
        minhaConexao = new MinhaConexao();
        Connection conn = minhaConexao.getConnection();
        String sql = "SELECT MAX(indiceTransacao) FROM schedule";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            ultimoIndice = rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Erro na consulta ao ultimo Indice");
            e.printStackTrace();
        } finally{
            minhaConexao.release(conn);
        }
        return ultimoIndice;
    }
}