package com.thub;

import com.alvaro.thub.model.ContentData;
import com.alvaro.thub.model.ContentMetadata;
import com.alvaro.thub.service.ContentService;
import com.alvaro.thub.service.impl.ContentServiceFileImpl;
import com.alvaro.thub.utils.FileUtils;

public class THubUI {

	public static void main(String args[]) {
		ContentService contentService = new ContentServiceFileImpl();
				
		try {
			
			ContentMetadata metadata = new ContentMetadata();
			metadata.setEntityType("competicion");
			metadata.setEntityId("762");
			metadata.setResourceId("small-image");
			metadata.setFormat("webp");
			
			byte[] cartel = FileUtils.readFile("c:/t/cartel_futbolin.webp");
			ContentData data = new ContentData();
			data.setRaw(cartel);			
			data.setMetadata(metadata);			
			contentService.save(data);
			
			System.out.println("Subido al CMS");

			// En otro momento del tiempo... y de la aplicación			
			ContentData cartelContent = contentService.get(metadata);
			cartel = cartelContent.getRaw();
			FileUtils.saveFile(cartel, "c:/t/cartel-descargado.webp");
			
			System.out.println("Descargado");
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}
}
