package servicos;

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	private LocacaoService service;

	@Before
	public void setup() {
		service = new LocacaoService();
	}

    @Test
	public void deveAlugarFilme() throws Exception {
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		//cenário
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));
		
		//ação
		Locacao locacao = service.alugarFilme(usuario, filmes);

		//verificação
		Assert.assertTrue(locacao.getValor() == 5);
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
	}
	@Test(expected = FilmeSemEstoqueException.class)
	public void deveLancarExcecaoAoAlugarFilmeSemEstoque() throws Exception {
		//cenário
		Usuario usuario = new Usuario("Usuário 2");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));

		//ação
		service.alugarFilme(usuario, filmes);
	}

	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
		//cenario
		LocacaoService service = new LocacaoService();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

		//ação
		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), Is.is("Usuário vazio"));
		}
	}

	@Test
    public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2 , 4.0), new Filme("Filme 3", 2 , 4.0), new Filme("Filme 3", 2 , 4.0));

		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificação
		assertThat(resultado.getValor(), Is.is(11.0));
    }

	@Test
    public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(
			new Filme("Filme 1", 2 , 4.0), new Filme("Filme 3", 2 , 4.0), 
			new Filme("Filme 3", 2 , 4.0), new Filme("Filme 4", 2 , 4.0));

		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificação
		assertThat(resultado.getValor(), Is.is(13.0));
    }

	@Test
    public void devePagar25PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(
			new Filme("Filme 1", 2 , 4.0), new Filme("Filme 3", 2 , 4.0), 
			new Filme("Filme 3", 2 , 4.0), new Filme("Filme 4", 2 , 4.0), new Filme("Filme 5", 2 , 4.0));

		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificação
		assertThat(resultado.getValor(), Is.is(14.0));
    }

	@Test
    public void devePagar0PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(
			new Filme("Filme 1", 2 , 4.0), new Filme("Filme 3", 2 , 4.0), 
			new Filme("Filme 3", 2 , 4.0), new Filme("Filme 4", 2 , 4.0),
			new Filme("Filme 5", 2 , 4.0), new Filme("Filme 4", 2 , 4.0));

		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificação
		assertThat(resultado.getValor(), Is.is(14.0));
    }

	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date() , Calendar.SATURDAY));
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));

		//ação
		Locacao retorno = service.alugarFilme(usuario, filmes);

		//verificação
		boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		Assert.assertTrue(ehSegunda);
	}
}
