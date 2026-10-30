package com.alvaro.thub.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.alvaro.thub.model.ContentData;
import com.alvaro.thub.model.ContentMetadata;
import com.alvaro.thub.service.ContentService;

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

	
	
	/*
	 * Guarda un recurso limpiando previamente cualquier versión antigua
	 * 
	 * @param el contentService para guardar el recurso y eliminar los antiguos y los metadatos para identificar el recurso
	 * @return void
	 * @throws IOException si ocurre un error al guardar el recurso
	 */
	public static void saveOrUpdateResource(ContentService contentService, String entityType, String entityId, String resourceId, byte[] rawData, String extension) throws IOException {
        if (rawData == null || rawData.length == 0 || entityId == null) {
            return;
        }

        // 1. Limpieza de formatos antiguos
        String[] formatos = {".png", ".jpg", ".jpeg"}; // Lista de formatos antiguos a eliminar
        for (String fmt : formatos) {
            ContentMetadata oldMeta = new ContentMetadata();
            oldMeta.setEntityType(entityType);
            oldMeta.setEntityId(entityId);
            oldMeta.setResourceId(resourceId);
            oldMeta.setFormat(fmt);
            try {
                contentService.remove(oldMeta);
            } catch (Exception e) {
            	throw new IOException("Error al eliminar el recurso antiguo con formato " + fmt + ": " + e.getMessage(), e);
            }
        }

        // 2. Guardar el nuevo recurso
        ContentMetadata metadata = new ContentMetadata();
        metadata.setEntityType(entityType);
        metadata.setEntityId(entityId);
        metadata.setResourceId(resourceId);
        metadata.setFormat(extension != null ? extension : ".png");

        ContentData contentData = new ContentData();
        contentData.setMetadata(metadata);
        contentData.setRaw(rawData);

        contentService.save(contentData);
    }
	
	
	
	}