package servicos;

import java.util.Date;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

    @Test
	public void test() throws Exception {
		//cenário
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);
		
		//ação
		Locacao locacao = service.alugarFilme(usuario, filme);

		//verificação
		Assert.assertTrue(locacao.getValor() == 5);
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
	}
	@Test(expected = Exception.class)
	public void testLocacao_filmeSemEstoque_Elegante() throws Exception {

		//cenário
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuário 2");
		Filme filme = new Filme("Filme 2", 0, 5.0);

		//ação
		service.alugarFilme(usuario, filme);
	}

	@Test
	public void testLocacao_filmeSemEstoque_Robusta(){

		//cenário
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuário 3");
		Filme filme = new Filme("Filme 3", 2, 5.0);

		//ação
		try {
			service.alugarFilme(usuario, filme);
		} catch (Exception e) {
			Assert.assertThat(e.getMessage(), Is.is("Filme sem estoque"));
		}
	}
}
