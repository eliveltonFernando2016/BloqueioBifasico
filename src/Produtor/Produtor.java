package Produtor;

import DAO.TransacaoDao;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Produtor extends Thread {
    private Thread t;
    private int numeroItens;
    private int numeroTransacoes;
    private int numeroAcessos;
    private static GerenciadorTransacao gerenciador;
    private boolean flag = true;

    public Produtor(int numeroItens, int numeroTransacoes, int numeroAcessos) throws SQLException {
        this.numeroItens = numeroItens;
        this.numeroTransacoes = numeroTransacoes;
        this.numeroAcessos = numeroAcessos;
    }

    public void run() {
        int ultimoIndice = 0;
        try {
            do {
                ultimoIndice = TransacaoDao.pegarUltimoIndice();
                gerenciador = new GerenciadorTransacao(numeroItens, numeroTransacoes, numeroAcessos, ultimoIndice);
                Schedule schedule = new Schedule(gerenciador.getListaTransacoes());
                TransacaoDao.gravarTransacoes(schedule);
                Thread.sleep( 3 * 1000 );
            } while(flag);
        }catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(Produtor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setFlag(boolean state) {
        this.flag = state;
    }

    public void start() {
	if (t == null) {
            t = new Thread (this);
            t.start ();
        }
    }
}
