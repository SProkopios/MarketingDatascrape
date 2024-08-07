package com.bot.marketing.config;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;


@Configuration
public class FirebaseConfig {
	
	//env variable
	private static String credentials = System.getenv("firebase.credentials.path");

	
	//Database
	private static Firestore db;
	
	
	//As the name suggests, initializing the database
	public static Firestore InitializeDatabase(){
		
		
		//If it has been initialized once, just returning the current one
		if (db != null) {
			return db;
		}
		
		//If not, initialize it here
		try {
			FileInputStream serviceAccount = new FileInputStream(credentials);
			GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);

			FirebaseOptions options = new FirebaseOptions.Builder()
                 .setCredentials(credentials)
                 .build();
			FirebaseApp.initializeApp(options);

			// You can now obtain Firestore instance as needed
			db = FirestoreClient.getFirestore();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Tää on Config db: " + db);
		return db;
	}
}

