package servicos;

import java.util.Date;

import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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
		System.out.println("Before");
	}

	@After
	public void tearDown() {
		System.out.println("After");
	}

    @Test
	public void test() throws Exception {
		//cenário
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);
		
		//ação
		Locacao locacao = service.alugarFilme(usuario, filme);

		//verificação
		Assert.assertTrue(locacao.getValor() == 5);
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
	}
	@Test(expected = FilmeSemEstoqueException.class)
	public void testLocacao_filmeSemEstoque_Elegante() throws Exception {
		//cenário
		Usuario usuario = new Usuario("Usuário 2");
		Filme filme = new Filme("Filme 2", 0, 5.0);

		//ação
		service.alugarFilme(usuario, filme);
	}

	@Test
	public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException {
		//cenario
		LocacaoService service = new LocacaoService();
		Filme filme = new Filme("Filme 3", 1, 4.0);

		//ação
		try {
			service.alugarFilme(null, filme);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), Is.is("Usuário vazio"));
		}
	}
}
