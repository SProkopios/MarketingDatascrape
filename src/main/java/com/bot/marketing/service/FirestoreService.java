package com.bot.marketing.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bot.marketing.config.FirebaseConfig;
import com.bot.marketing.domain.Company;
import com.bot.marketing.domain.User;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;



@Service
public class FirestoreService {
	
	//Adding Company object to firebase
	public static void addObject(Company company) {
		try {
			System.out.println("Company: " + company.getBusinessId());
			Firestore db = FirebaseConfig.InitializeDatabase();
			DocumentReference docRef = db.collection("Company").document(company.getBusinessId());
		
		
			Map<String, Object> data = new HashMap<>();
			data.put("area", company.getArea());
			data.put("businessId", company.getBusinessId());
			data.put("email", company.getEmail());
			data.put("link", company.getLink());
			data.put("name", company.getName());
			data.put("operational", company.isOperational());
			data.put("personName", company.getPersonName());
			data.put("send", company.isSend());
			data.put("source", company.getSource());
		
			//save the object to database
			docRef.set(data);

			} catch(Exception e) {
				System.out.println("FirestoreService.addObject: " + e);
		}
	}
	
	
	//Returning every company from database
	public static List<Company> getAll() {
		
		List<Company> companies = new ArrayList<>();
		try {
			Firestore db = FirebaseConfig.InitializeDatabase();
			ApiFuture<QuerySnapshot> query = db.collection("Company").get();
		
			QuerySnapshot querySnapshot = query.get();
			
			//I have no idea how to use ApiFuture so lets change that to list
		    List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
		    for (QueryDocumentSnapshot document : documents) {
		        Company company = document.toObject(Company.class);
		        companies.add(company);
		    }
		} catch (Exception e) {
			System.out.println("FirestoreService.getAll: " + e);
		}
		return companies;
	}
	
	
	//Returning Company 
	public static Optional<Company> getCompanyById(String id) {
		
		Optional<Company> optionalCompany = Optional.empty();
		
		try {
			
			
			//making a query to database
			Firestore db = FirebaseConfig.InitializeDatabase();
			CollectionReference companies = db.collection("Company");
			Query query = companies.whereEqualTo("businessId", id);
		
			
			// Asynchronously retrieve multiple documents
			ApiFuture<QuerySnapshot> querySnapshot = query.get();

			
			// Get the list of documents returned by the query
			List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
			
			if (! documents.isEmpty()) {

				// Check if the documents list is not empty and return the first matching document as a Company object
				QueryDocumentSnapshot document = documents.get(0);
				Company company = document.toObject(Company.class);
				optionalCompany = Optional.of(company);
			}
		}catch (Exception e) {
			System.out.println("getCompanyById: " + e);
		}
		return optionalCompany;
	}
	
	
	//Verifying username and password
	public static Boolean verifyUser(User user) {
		
		
		//making a query to database
		Firestore db = FirebaseConfig.InitializeDatabase();
		CollectionReference User = db.collection("User");
		Query query = User.whereEqualTo("username", user.getUsername());
		
		// Asynchronously retrieve multiple documents
		ApiFuture<QuerySnapshot> querySnapshot = query.get();

		
		// Get the list of documents returned by the query
		try {
			
			QuerySnapshot snapshot = querySnapshot.get();
		
			if (! snapshot.isEmpty()) {
				
				DocumentSnapshot document = snapshot.getDocuments().get(0);

				//Getting the fields from database
				String dbPassword = document.getString("password");

				
				if(dbPassword.equals(user.getUsername())) {
					return true;
				}
		}
		}catch(Exception e) {
			System.out.println("verifyUser: " + e);
		}
		return false;
		
	}


}
