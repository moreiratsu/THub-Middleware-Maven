package com.alvaro.thub.service.impl;

import org.mindrot.jbcrypt.BCrypt;

import com.alvaro.thub.service.EncryptionService;

public class EncryptionServiceBCryptImpl implements EncryptionService {
	
	public EncryptionServiceBCryptImpl() {			
	}

	@Override
	public String encrypt(String data) {
		return BCrypt.hashpw(data, BCrypt.gensalt());
	}

	@Override
	public boolean checkEncryption(String clearData, String encryptedData) {
		/*El método debe comparar la contraseña en texto claro (clearData) con la versión encriptada (encryptedData)
		*para verificar si coinciden, no usar una cadena literal fija*/
		return BCrypt.checkpw(clearData, encryptedData); 
	}


}
