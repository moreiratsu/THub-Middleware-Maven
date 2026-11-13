package com.alvaro.thub.service.impl;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
//import java.nio.file.Files;
//import java.nio.file.Path;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.DataException;
import com.alvaro.thub.model.ContentData;
import com.alvaro.thub.model.ContentException;
import com.alvaro.thub.model.ContentMetadata;
import com.alvaro.thub.service.ContentService;
import com.alvaro.thub.utils.FileUtils;


	

public class ContentServiceFileImpl implements ContentService {
	
	private static Logger logger = LogManager.getLogger(ContentServiceFileImpl.class);
	
	private static final String CMS_BASE_FOLDER = "c:/t/cms";
	
	
	public ContentServiceFileImpl() {
		
	}

	/**
	 * Genera la ruta del contenido en base a sus metadatos.
	 * @param metadata
	 * @return
	 */
	protected String getPath(ContentMetadata metadata) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(CMS_BASE_FOLDER)
		.append("/").append(metadata.getEntityType())
		.append("/").append(metadata.getEntityId())
		.append("/").append(metadata.getResourceType())
		.append(metadata.getFormat());		
		return buffer.toString();
	}
	
	@Override
	public ContentData get(ContentMetadata metadata) throws ContentException {

        String contentPath = getPath(metadata);

        try {
            byte[] raw = FileUtils.readFile(contentPath);

            ContentData data = new ContentData();
            data.setMetadata(metadata);
            data.setRaw(raw);

            return data;

        } catch (IOException ioe) {
			logger.error("Leyendo "+contentPath+": "+ioe.getMessage(), ioe);			
			throw new ContentException(contentPath, ioe);
        }
	}

	// con este metodo recupero todos los archivos de una entidad por su id y tipo de entidad por ejemplo todos los archivos de evento con id 1
	@Override
	public List<ContentData> get(String entityType, String entityId)
	        throws ContentException {

	    List<ContentData> contenidos = new ArrayList<>();

	    if (entityType == null || entityType.isBlank()
	            || entityId == null || entityId.isBlank()) {
	        return contenidos;
	    }

	    Path carpetaCms = Paths.get(CMS_BASE_FOLDER)
	            .toAbsolutePath()
	            .normalize();

	    Path carpetaEntidad = carpetaCms
	            .resolve(entityType)
	            .resolve(entityId)
	            .normalize();

	    // Evita que los valores recibidos apunten fuera de la carpeta del CMS.
	    if (!carpetaEntidad.startsWith(carpetaCms)) {
	        throw new ContentException("La ruta del contenido no es válida.");
	    }

	    // Si el evento aún no tiene archivos, devolvemos una lista vacía.
	    if (!Files.isDirectory(carpetaEntidad)) {
	        return contenidos;
	    }

	    try (DirectoryStream<Path> archivos =
	            Files.newDirectoryStream(carpetaEntidad)) {

	        for (Path archivo : archivos) {
	            if (!Files.isRegularFile(archivo)) {
	                continue;
	            }

	            String nombreArchivo = archivo.getFileName().toString();
	            int posicionPunto = nombreArchivo.lastIndexOf('.');

	            // El nombre del archivo guarda el tipo de recurso y su formato:
	            // cartel.jpg -> recurso "cartel", formato ".jpg"
	            if (posicionPunto <= 0) {
	                continue;
	            }

	            String tipoRecurso = nombreArchivo.substring(0, posicionPunto);
	            String formato = nombreArchivo.substring(posicionPunto);

	            ContentMetadata metadata = new ContentMetadata();
	            metadata.setEntityType(entityType);
	            metadata.setEntityId(entityId);
	            metadata.setResourceType(tipoRecurso);
	            metadata.setFormat(formato);

	            ContentData contenido = new ContentData();
	            contenido.setMetadata(metadata);
	            contenido.setRaw(Files.readAllBytes(archivo));

	            contenidos.add(contenido);
	        }

	        return contenidos;

	    } catch (IOException e) {
	        throw new ContentException(
	                "No se pudieron leer los contenidos de la entidad.",
	                e
	        );
	    }
	}

	@Override
	public void save(ContentData data) throws ContentException {
		ContentMetadata metadata = data.getMetadata();
		// En qué fichero en disco se va a guarda data.
		// Convencion de nombrado.
		String contentPath = getPath(metadata);
		try {
			FileUtils.saveFile(data.getRaw(), contentPath);
			if (logger.isInfoEnabled()) {
				logger.info("Guardado "+contentPath);
			}
		} catch (IOException ioe) {
			logger.error("Guardando "+contentPath+": "+ioe.getMessage(), ioe);			
			throw new ContentException(contentPath, ioe);
		}
	}

	// borra los archivos de la ruta de los metadatos
	@Override
	public void remove(ContentMetadata metadata) throws DataException {
	    if (metadata == null) {
	        return;
	    }

	    String ruta = getPath(metadata); // construye la ruta completa usando el tipo de entidad, su ID, el tipo de recurso y el formato. Por ejemplo: .../event/11/cartel.png.

	    try {
	        Files.deleteIfExists(Paths.get(ruta));
	    } catch (IOException e) {
	        logger.error("No se pudo eliminar el contenido " + ruta, e);
	        throw new DataException("No se pudo eliminar el contenido.", e);
	    }
	}

	@Override
	public void remove(String id) throws DataException {
		// TODO Auto-generated method stub
		
	}

}
