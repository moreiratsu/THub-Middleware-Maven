package com.alvaro.thub.service.impl;

import java.io.IOException;
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
		.append("/").append(metadata.getResourceId())
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

	@Override
	public List<ContentData> get(String entityType, String entityId) throws ContentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(ContentData data) throws ContentException {
		ContentMetadata metadata = data.getMetadata();
		// En qué fichero en disco se va a guarda data.
		// Convencion de nombrado.
		String contentPath = getPath(metadata);
		try {
			FileUtils.saveFile(data.getRaw(), contentPath);
		} catch (IOException ioe) {
			logger.error("Guardando "+contentPath+": "+ioe.getMessage(), ioe);			
			throw new ContentException(contentPath, ioe);
		}
	}

	@Override
	public void remove(ContentMetadata metadata) throws DataException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(String id) throws DataException {
		// TODO Auto-generated method stub
		
	}

}
