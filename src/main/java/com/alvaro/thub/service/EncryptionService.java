package com.alvaro.thub.service;

public interface EncryptionService {

	
	public String encrypt(String data);

	public boolean checkEncryption(String clearData, String encryptedData);
	
	// public String decrypt(String data); 
	
}
