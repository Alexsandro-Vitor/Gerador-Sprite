package funcoes;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Imagem {
	public static int[][] lerImagem(File arquivo, int alfa) throws IOException {
		BufferedImage imagem = ImageIO.read(arquivo);							//Le o arquivo
		return bufferParaMatriz(imagem, alfa);
	}
	
	public static int[][] bufferParaMatriz(BufferedImage imagem, int alfa) {
		int largura = imagem.getWidth(), altura = imagem.getHeight();			//Dimensoes da imagem
		int[] pixels = imagem.getRGB(0, 0, largura, altura, null, 0, largura);	//Transforma��o da imagem em array de pixels
		return arrayParaMatriz(pixels, largura, altura, alfa);
	}
	
	private static int[][] arrayParaMatriz(int[] array, int largura, int altura, int alfa) {
		int[][] matriz = new int[largura][altura];
		for (int coluna = 0; coluna < largura; coluna++) {
			for (int linha = 0; linha < altura; linha++) {
				matriz[coluna][linha] = array[largura * linha + coluna];
				if (alfa(matriz[coluna][linha]) > 0) 
					matriz[coluna][linha] = alfa * 16777216 + (Integer.remainderUnsigned(matriz[coluna][linha], 16777216));
				else matriz[coluna][linha] = 0;
			}
		}
		return matriz;
	}
	
	public static void produzirPng(int[][] pixels, File arquivo) throws IOException {
		BufferedImage imagem = matrizParaBuffer(pixels);
		ImageIO.write(imagem, "PNG", arquivo);
	}
	
	public static BufferedImage matrizParaBuffer(int[][] matriz) {
		int largura = matriz.length, altura = matriz[0].length;
		//TYPE_INT_ARGB: Gera a imagem com um alpha (opacidade) e cores RGB
		BufferedImage imagem = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
		imagem.setRGB(0, 0, largura, altura, matrizParaArray(matriz), 0, largura);
		return imagem;
	}
	
	private static int[] matrizParaArray(int[][] matriz) {
		int largura = matriz.length, altura = matriz[0].length;
		int[] array = new int[largura * altura];
		for (int col = 0; col < largura; col++) {
			for (int lin = 0; lin < altura; lin++) {
				array[largura * lin + col] = matriz[col][lin];
			}
		}
		return array;
	}
	
	public static int[][] sobreporImagem(int[][] acima, int[][] abaixo) {
		int largura = acima.length, altura = acima[0].length;
		int[][] saida = new int[largura][altura];
		for (int coluna = 0; coluna < largura; coluna++) {
			for (int linha = 0; linha < altura; linha++) {
				saida[coluna][linha] = sobreporCor(acima[coluna][linha], abaixo[coluna][linha]);//transparente(acima[coluna][linha]) ? abaixo[coluna][linha] : acima[coluna][linha];
			}
		}
		return saida;
	}
	
	private static boolean transparente(int argb) {
		//Checa se argb possui alguma opacidade (se os 8 bits mais significativos de argb sao diferentes de 0)
		return alfa(argb) == 0;
	}
	
	private static int sobreporCor(int acima, int abaixo) {
		int alfa = 255;
		byte red = (byte)((alfa(acima) * red(acima) + (255 - alfa(acima)) * red(abaixo)) / 255);
		byte green = (byte)((alfa(acima) * green(acima) + (255 - alfa(acima)) * green(abaixo)) / 255);
		byte blue = (byte)((alfa(acima) * blue(acima) + (255 - alfa(acima)) * blue(abaixo)) / 255);
		return ((alfa * 256 + red) * 256 + green) * 256 + blue;
	}
	
	private static int alfa(int argb) {
		return Integer.divideUnsigned(argb, 16777216);
	}
	
	private static int red(int argb) {
		return Integer.remainderUnsigned(Integer.divideUnsigned(argb, 65536), 256);
	}
	
	private static int green(int argb) {
		return Integer.remainderUnsigned(Integer.divideUnsigned(argb, 256), 256);
	}
	
	private static int blue(int argb) {
		return Integer.remainderUnsigned(argb, 256);
	}
	
	public static int[][] gerarTransparencia() {
		int[][] saida = new int[96][128];
		for (int i = 0; i < saida.length; i++) {
			for (int j = 0; j < saida[0].length; j++) {
				saida[i][j] = 0;
			}
		}
		return saida;
	}
}
