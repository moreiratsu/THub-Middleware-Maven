package com.alvaro.thub.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

	/**
	 * Saves a file to the specified path.
	 *
	 * @param data The byte array representing the file data.
	 * @param path The path where the file should be saved.
	 * @return void
	 */
	public static void saveFile(byte[] data, String rutaDestio) throws IOException {
		File file = new File(rutaDestio);
		if (!file.exists()) {
			file.createNewFile();
		} 
		
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			throw new IOException("Error al guardar el archivo: " + e.getMessage());
		}
	}
	
	/**
	 * Reads a file from the specified path and returns its content as a byte array.
	 *
	 * @param path The path of the file to be read.
	 * @return A byte array containing the file's content.
	 * @throws IOException If an I/O error occurs or if the file does not exist.
	 */
	public static byte[] readFile(String ruta) throws IOException {
		File file = new File(ruta);
		if (!file.exists()) {
			throw new IOException("El archivo no existe: " + ruta);
		}

		try (FileInputStream fis = new FileInputStream(file)) {
			return fis.readAllBytes();
		}
	}

	}