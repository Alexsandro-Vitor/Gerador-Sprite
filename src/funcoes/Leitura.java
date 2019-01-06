package funcoes;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import classes.CorARGB;
import classes.Dimensoes;
import classes.ParteSprite;
import excecoes.TamanhoErradoException;

public class Leitura {
	
	//Pega o nome de cada arquivo de uma pasta e coloca em um array
	public static String[] nomesArquivos(File[] array) {
		String[] saida = new String[array.length + 1];
		if (array.length > 0) saida[0] = "[None]";
		else saida[0] = "[Empty]";
		for (int i = 1; i < saida.length; i++) {
			saida[i] = array[i-1].getName();
		}
		return saida;
	}
	
	//Pega a imagem selecionada pelo comboBox
	public static int[][] selecionarImagem(File[] array, ParteSprite parte) throws TamanhoErradoException {
		int[][] matriz;
		try {
			matriz = lerImagem(array[parte.getCmb().getSelectedIndex() - 1], parte.getCor());
		} catch (ArrayIndexOutOfBoundsException e) {
			matriz = Imagem.gerarTransparencia();
		} catch (IOException e) {
			e.printStackTrace();
			matriz = Imagem.gerarTransparencia();
		}
		return matriz;
	}

	public static int[][] lerImagem(File arquivo, CorARGB cor) throws IOException, TamanhoErradoException {
		BufferedImage buffer = ImageIO.read(arquivo);	//Le o arquivo
		if (buffer.getWidth() != Dimensoes.LARGURA || buffer.getHeight() != Dimensoes.ALTURA)
			throw new TamanhoErradoException(arquivo.getName(), Dimensoes.LARGURA, Dimensoes.ALTURA);
		return bufferParaMatriz(buffer, cor);
	}

	private static int[][] bufferParaMatriz(BufferedImage imagem, CorARGB cor) {
		int[] pixels = imagem.getRGB(0, 0, Dimensoes.LARGURA, Dimensoes.ALTURA, null, 0, Dimensoes.LARGURA);	//Transformação da imagem em array de pixels
		return arrayParaMatriz(pixels, cor);
	}

	private static int[][] arrayParaMatriz(int[] array, CorARGB cor) {
		int[][] matriz = new int[Dimensoes.LARGURA][Dimensoes.ALTURA];
		for (int coluna = 0; coluna < Dimensoes.LARGURA; coluna++) {
			for (int linha = 0; linha < Dimensoes.ALTURA; linha++) {
				CorARGB original = new CorARGB((cor.getAlpha() << 24) + (array[Dimensoes.LARGURA * linha + coluna] & 0xFFFFFF));
				if ((array[Dimensoes.LARGURA * linha + coluna] & 0xFF000000) != 0)
					matriz[coluna][linha] = Imagem.filtrarCor(original, cor);
			}
		}
		return matriz;
	}
}
