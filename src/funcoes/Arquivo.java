package funcoes;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

import classes.CorARGB;
import classes.ParteSprite;
import classes.Pastas;
import excecoes.TamanhoErradoException;

public class Arquivo {
	//Pega o nome de cada arquivo de uma pasta e coloca em um array
	public static String[] nomesArquivos(File[] array) {
		String[] saida = new String[array.length + 1];
		saida[0] = "[Vazio]";
		for (int i = 1; i < saida.length; i++) {
			saida[i] = array[i-1].getName();
		}
		return saida;
	}
	
	//Pega a imagem selecionada pelo comboBox
	public static int[][] selecionarImagem(File[] array, ParteSprite parte)
			throws TamanhoErradoException {
		int[][] matriz;
		try {
			matriz = lerImagem(array[parte.cmb.getSelectedIndex() - 1], new CorARGB(parte));
		} catch (ArrayIndexOutOfBoundsException e) {
			matriz = Imagem.gerarTransparencia();
		} catch (IOException e) {
			e.printStackTrace();
			matriz = Imagem.gerarTransparencia();
		}
		return matriz;
	}
	
	public static int[][] lerImagem(File arquivo, CorARGB cor) throws IOException, TamanhoErradoException {
		BufferedImage imagem = ImageIO.read(arquivo);	//Le o arquivo
		if (imagem.getWidth() != Imagem.LARGURA || imagem.getHeight() != Imagem.ALTURA) throw new TamanhoErradoException(arquivo.getName());
		return Imagem.bufferParaMatriz(imagem, cor);
	}

	//Salva o sprite gerado e exibe uma mensagem avisando
	public static String salvarSprite(String nomeArquivo, BufferedImage buffer) {
		String nome = Arquivo.nomeSprite(nomeArquivo);
		try {
			ImageIO.write(buffer, "PNG", new File("imagens\\sprites\\" + nome));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nome;
	}

	//Determina o nome com o qual o sprite ser� salvo
	@SuppressWarnings("resource")
	public static String nomeSprite(String nomeImagem) {
		Pastas.criar("sprites");
		try {
			new FileReader("imagens\\sprites\\" + nomeImagem + ".png");
			for (int i = 2; ; i++) {
				try {
					new FileReader("imagens\\sprites\\" + nomeImagem + '('+ i + ").png");
				} catch (FileNotFoundException e) {
					return nomeImagem + '(' + i + ").png";
				}
			}
		} catch (FileNotFoundException e) {
			return nomeImagem + ".png";
		}
	}
}
