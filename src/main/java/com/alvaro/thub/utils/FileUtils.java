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
	    
	    // 1. Obtener la carpeta contenedora (ejemplo: C:\t\cms\TEAM\1)
	    File parentDir = file.getParentFile();
	    
	    // 2. Si la estructura de carpetas no existe, la creamos al completo
	    if (parentDir != null && !parentDir.exists()) {
	        parentDir.mkdirs(); // mkdirs() crea C:, t, cms, TEAM e ID si no existen
	    }

	    // 3. Crear el archivo si no existe
	    if (!file.exists()) {
	        file.createNewFile();
	    }

	    // 4. Escribir los bytes en el archivo
	    try (FileOutputStream fos = new FileOutputStream(file)) {
	        fos.write(data);
	        fos.flush();
	    } catch (IOException e) {
	        throw new IOException("Error al guardar el archivo en " + rutaDestio + ": " + e.getMessage(), e);
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