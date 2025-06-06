package com.bot.marketing.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.bot.marketing.config.FirebaseConfig;
import com.bot.marketing.domain.Company;
import com.bot.marketing.domain.CompanySearchRequest;
import com.bot.marketing.domain.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.Query.Direction;
import com.google.cloud.firestore.QueryDocumentSnapshot;



@Service
public class FirestoreService {
	
	//Adding Company object to firebase
	public static void addObject(Company company, String collection) {
		try {
			Firestore db = FirebaseConfig.InitializeDatabase();
			DocumentReference docRef = db.collection(collection).document(company.getBusinessId());
		
		
			Map<String, Object> data = new HashMap<>();
			data.put("area", company.getArea());
			data.put("businessId", company.getBusinessId());
			data.put("email", company.getEmail());
			data.put("link", company.getLink());
			data.put("name", company.getName());
			data.put("operational", company.isOperational());
			data.put("createdAt", company.getCreatedAt());
			data.put("send", company.isSend());
			data.put("source", company.getSource());
		
			//save the object to database
			docRef.set(data);

			} catch(Exception e) {
				System.out.println("FirestoreService.addObject: " + e);
		}
	}
	
	
	//Returning every company from database
	//TODO query class for firebase querys(?)
	public static List<Company> getAll(@RequestParam(required = false) String cursor, @RequestParam(required = false) String area, @RequestParam(required = false) String industry) {
		List<Company> companies = new ArrayList<>();
		Firestore db = FirebaseConfig.InitializeDatabase();
		CollectionReference companiesRef = db.collection("Mylist");
		Query query;
		try {
			if (area == null || area.isEmpty() && industry == null || industry.isEmpty()) {
				query = companiesRef.orderBy("createdAt", Direction.DESCENDING).limit(2);
			} else {
				query = companiesRef.orderBy("createdAt", Direction.DESCENDING).limit(2).whereEqualTo("area", area);
			}

			//If not the first api call
			if (cursor != null && !cursor.isEmpty()) {
				Timestamp cursorTimestamp = Timestamp.parseTimestamp(cursor);
				query = query.startAfter(cursorTimestamp);
			}
			
		    ApiFuture<QuerySnapshot> future = query.get();
		    List<QueryDocumentSnapshot> documents = future.get().getDocuments();
			    
		    for (QueryDocumentSnapshot doc : documents) {
		    	Company company = doc.toObject(Company.class);
		    	companies.add(company);
		    	}
		    
		} catch (Exception e) {
			System.out.println("FirestoreService.getAll: " + e);
		}
		return companies;
	}
	
	public List<Company> getAllWithFilter(CompanySearchRequest filter) {
		String area = filter.getArea();
		String industry = filter.getCategoryText();
		List<Company> companies = new ArrayList<>();
		Firestore db = FirebaseConfig.InitializeDatabase();
		CollectionReference companiesRef = db.collection("Mylist");
		Query query;
		try {
			if (area.isEmpty() && industry.isEmpty()) {
				query = companiesRef.orderBy("createdAt", Direction.DESCENDING).limit(2);
			} else if (!area.isEmpty()){
				query = companiesRef.orderBy("createdAt", Direction.DESCENDING).limit(2).whereEqualTo("area", area);
			} else {
				query = companiesRef.orderBy("createdAt", Direction.DESCENDING).limit(2).whereEqualTo("industry", industry);
			}
		    ApiFuture<QuerySnapshot> future = query.get();
		    List<QueryDocumentSnapshot> documents = future.get().getDocuments();
			    
		    for (QueryDocumentSnapshot doc : documents) {
		    	Company company = doc.toObject(Company.class);
		    	companies.add(company);
		    	}
		} catch (Exception e) {
			System.out.println("FirestoreService.getAllWithFilter: ");
			e.printStackTrace();
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
		CollectionReference User = db.collection("Users");
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

				if(dbPassword.equals(user.getPassword())) {
					return true;
				}
		}
		}catch(Exception e) {
			System.out.println("verifyUser: " + e);
		}
		return false;	
	}

	public Map<String, String> getInfo() {
		HashMap<String, String> retInfo = new HashMap<>();
        try {
            Firestore db = FirebaseConfig.InitializeDatabase();            
			CollectionReference infoRef = db.collection("Info");
			ApiFuture<QuerySnapshot> future = infoRef.get();
		    List<QueryDocumentSnapshot> documents = future.get().getDocuments();
		    
		    for (QueryDocumentSnapshot doc : documents) {
		    	retInfo.put("info", doc.get("info").toString());
		    	retInfo.put("info2", doc.get("info2").toString());
		    }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retInfo;
    }
}
