package com.onlinebankingsystem.utility;

import java.util.UUID;

public class TransactionIdGenerator {
	
	public static String generate() {
		UUID uuid = UUID.randomUUID();
        String uuidHex = uuid.toString().replace("-", ""); // Remove hyphens
        String uuid16Digits = uuidHex.substring(0, 16); // Take the first 16 characters
        
        return uuid16Digits;
    }

}
