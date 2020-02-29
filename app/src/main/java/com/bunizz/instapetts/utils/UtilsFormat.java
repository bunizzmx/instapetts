package com.bunizz.instapetts.utils;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;


/**
 * Clase que contiene diferentes métodos utilizados para las conversiones del
 * protocolo
 * 
 * @author sgcweb
 * 
 */
public class UtilsFormat {

	// Debug
	private static final String TAG = "UtilsFormat";

	/**
	 * Limpia un archivo existente con datos
	 * 
	 * @param path
	 * @return
	 */
	public static boolean clearFile(String path) {
		try {
			File file = new File(path);
			// si el archivo no existe, se crea
			if (!file.exists()) {
				file.createNewFile();
			}
			// se escribe vacio el archivo
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(new String());
			bw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * Obtiene los datos binarios de un hexadecimal
	 * 
	 * @param strHex
	 * @return
	 */
	public static String hexToBin(String strHex) {
		try {
			return new BigInteger(strHex, 16).toString(2);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Convierte un valor entero a su representación hexadecimal en un arreglo
	 * de bytes
	 * 
	 * @param valor
	 *            dato tipo entero que sera convertido a hexadecimal
	 * @param size
	 *            tamaño del dato
	 * @return
	 */
	public static byte[] intToHex(int valor, int size) {
		try {
			byte[] hex = initArray(size);
			int pos = size - 1;
			do {
				if ((valor % 16) < 10) {
					hex[pos--] = (byte) ((valor % 16) + 48);
				} else
					hex[pos--] = (byte) ((valor % 16) + 55);
			} while ((valor /= 16) != 0 && pos >= 0);
			return hex;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] initArray(int size) {
		byte[] array = new byte[size];
		for (int i = 0; i < size; i++) {
			array[i] = (byte) '0';
		}
		return array;
	}

	/**
	 * Convierte un valor byte hexadecimal a entero
	 * 
	 * @param data
	 *            arrreglo de bytes hexadecimal que se convertira a entero
	 * @return el byte hexadecimal
	 */
	public static int byteHexToInt(byte[] data) {
		try {
			return new BigInteger(new String(data), 16).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Obtiene un subarreglo de bytes contenido en un arreglo de bytes
	 * 
	 * @param array
	 *            arreglo de bytes
	 * @param index
	 *            la posicion donde comienza
	 * @param count
	 *            la posicion donde termina
	 * @return arreglo de byte con los arreglos a y b concatenados
	 */
	public static byte[] subArray(byte[] array, int index, int count) {
		try {
			byte[] sub = new byte[count];

			for (int i = index, j = 0; j < count; j++, i++) {
				sub[j] = array[i];
			}

			return sub;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Convierte una cadena a su representación en un arreglo de bytes
	 * 
	 * @param str
	 *            Cadena que sera convertida en byte[]
	 * @return La cadena convertida en byte[]
	 */
	public static byte[] stringToByte(String str) {
		try {
			byte[] byteArray = new byte[str.length()];

			for (int i = 0; i < str.length(); i++) {
				byteArray[i] = (byte) str.charAt(i);
			}
			return byteArray;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Valida si el dato es un digito
	 * 
	 * @param dat
	 * @return
	 */
	public static boolean isDigit(String dat) {
		try {
			int cont = 0;
			for (int i = 0; i < dat.length(); i++) {
				if (Character.isDigit(dat.charAt(i))) {
					cont++;
				}
			}
			if (cont == dat.length() - 1) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Método utilizado para el padding a la derecha
	 * 
	 * @param s
	 * @param n
	 * @return
	 */
	public static String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);
	}

	/**
	 * Método utilizado para el padding a la izquierda
	 * 
	 * @param s
	 * @param n
	 * @return
	 */
	public static String padLeft(String s, int n) {
		return String.format("%1$" + n + "s", s);
	}

    /**
     * Método encargado de ocultar los primeros números del string pasado como numero de tarjeta
     * @param cardNumber
     * @return
     */
    public static String hideCardNumbers(String cardNumber){
        if(cardNumber!=null && cardNumber.length()>0) {

			if (cardNumber.length() == 4){

				String card = "XXXX XXXX XXXX " + cardNumber;
				return card;

			} else if (cardNumber.length() == 10){
				String card = "XXXX XXXX XXXX XXXX";
				return card;
			}else {
				char[] cardNumberArray = cardNumber.toCharArray();
				for (int i = 0; i < cardNumberArray.length - 4; i++) {
					cardNumberArray[i] = 'X';
				}
				String hiddenCard = new String(cardNumberArray);

				// add enough space for an additional " " for every 4 chars:
				char[] chars = new char[hiddenCard.length() + (hiddenCard.length() / 4)];

				// this offset will give us the first " " position from the LEFT:
				int offset = hiddenCard.length() % 4;
				int idx = 0, strIdx = 0;

				for (; strIdx < hiddenCard.length(); idx++, strIdx++)
				{
					if (((strIdx % 4) == offset) && (strIdx != 0))
						chars[idx++] = ' ';
					chars[idx] = hiddenCard.charAt(strIdx);
				}

				return new String(chars);
			}
        }
        return null;
    }

	public static String formatTransNumber(String codigo){
		if (codigo!=null && codigo.length()>0){
			char[] codigoChar = codigo.toCharArray();
			char[] formatedcodigoChar = new char[codigoChar.length +1];

			int j = 0;
			for (int i = 0; i < codigoChar.length; i++) {
				if (i==4 ){
					formatedcodigoChar[j] = '-';
					j++;
				}
				formatedcodigoChar[j] = codigoChar[i];
				j++;
			}
			return new String(formatedcodigoChar);
		}
		return codigo;
	}

}
