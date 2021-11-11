package servicos;

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.servicos.LocacaoService;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

    private LocacaoService service;

    @Parameter
    public  List<Filme> filmes;
    @Parameter(value = 1)
    public Double valorLocacao;

    @Before
    public void setup() {
        service = new LocacaoService();
    }

    private static Filme filme1 = new Filme("Filme 1", 2, 4.0);
    private static Filme filme2 = new Filme("Filme 2", 2, 4.0);
    private static Filme filme3 = new Filme("Filme 3", 2, 4.0);
    private static Filme filme4 = new Filme("Filme 4", 2, 4.0);
    private static Filme filme5 = new Filme("Filme 5", 2, 4.0);
    private static Filme filme6 = new Filme("Filme 6", 2, 4.0);

    @Parameters(name = "{2}")
    public static Collection<Object[]> getParametros() {
        return Arrays.asList(new Object[][]{
            {Arrays.asList(filme1, filme2, filme3), 11.00},
            {Arrays.asList(filme1, filme2, filme3, filme4), 13.00},
            {Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14.00},
            {Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14.00}
        });
    }
    
    @Test
    public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
		Usuario usuario = new Usuario("Usuario 1");
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificação
		assertThat(resultado.getValor(), Is.is(valorLocacao));
    }
}
