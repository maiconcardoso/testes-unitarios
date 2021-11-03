package servicos;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
	}

    @Test
	public void test() throws Exception {
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
	public void testLocacao_filmeSemEstoque_Elegante() throws Exception {
		//cenário
		Usuario usuario = new Usuario("Usuário 2");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));

		//ação
		service.alugarFilme(usuario, filmes);
	}

	@Test
	public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException {
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
}
