package databasePart1;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import application.*;

/**
 * The DatabaseHelper class is responsible for managing the connection to the database,
 * performing operations such as user registration, login validation, and handling invitation codes.
 */



public class DatabaseHelper {
	
	// To run current database connection
	public Connection getConnection() {
		return connection;
		
	}

	// JDBC driver name and database URL 
	static final String JDBC_DRIVER = "org.h2.Driver";   
	static final String DB_URL = "jdbc:h2:~/FoundationDatabase";  

	//  Database credentials 
	static final String USER = "sa"; 
	static final String PASS = ""; 

	private Connection connection = null;
	private Statement statement = null; 
	//	PreparedStatement pstmt

	public void connectToDatabase() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement(); 
			// You can use this command to clear the database and restart from fresh.
			//statement.execute("DROP ALL OBJECTS");

			createTables();  // Create the necessary tables if they don't exist
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}

	private void createTables() throws SQLException {
		String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "name VARCHAR(255),"
				+ "email VARCHAR(255),"
				+ "userName VARCHAR(255) UNIQUE, "
				+ "password VARCHAR(255), "
				+ "adminRole BOOLEAN DEFAULT FALSE,"
				+ "studentRole BOOLEAN DEFAULT FALSE,"
				+ "instructorRole BOOLEAN DEFAULT FALSE,"
				+ "staffRole BOOLEAN DEFAULT FALSE,"
				+ "reviewerRole BOOLEAN DEFAULT FALSE,"
				+ "requestReviewerRole BOOLEAN DEFAULT FALSE)";
		statement.execute(userTable);
		
		// Create the invitation codes table
	    String invitationCodesTable = "CREATE TABLE IF NOT EXISTS InvitationCodes ("
	            + "code VARCHAR(10) PRIMARY KEY, "
	            + "isUsed BOOLEAN DEFAULT FALSE,"
	    		+ "adminRole BOOLEAN DEFAULT FALSE,"
	    		+ "studentRole BOOLEAN DEFAULT FALSE,"
	    		+ "instructorRole BOOLEAN DEFAULT FALSE,"
	    		+ "staffRole BOOLEAN DEFAULT FALSE,"
	    		+ "reviewerRole BOOLEAN DEFAULT FALSE,"
	    		+ "timeInviteMade TIMESTAMP)";
	    statement.execute(invitationCodesTable);

		// Create the one-time password table
	    String onetimePasswordTable = "CREATE TABLE IF NOT EXISTS OnetimePassword ("
	    		+ "password VARCHAR(10) PRIMARY KEY, "
	    		+ "username VARCHAR(255),"
	            	+ "isUsed BOOLEAN DEFAULT FALSE,"
	            	+ "isForgotten BOOLEAN DEFAULT FALSE)";
	    statement.execute(onetimePasswordTable);
	    
	    // Table for answer and questions
	    String questionsTable = "CREATE TABLE IF NOT EXISTS Questions (" 
	    		+ "id INT AUTO_INCREMENT PRIMARY KEY, "
	    		+ "title VARCHAR(255) NOT NULL, "
	    		+ "content TEXT NOT NULL, " 
	    		+ "author VARCHAR(100) NOT NULL, " 
	    		+ "category VARCHAR(64) NOT NULL, " 
	    		+ "resolved BOOLEAN DEFAULT FALSE, " 
	    		+ "upvotes INT DEFAULT 0)";
	    statement.execute(questionsTable);
	    
	    String answersTable = "CREATE TABLE IF NOT EXISTS Answers ("
	    		+ "id INT AUTO_INCREMENT PRIMARY KEY, "
	    		+ "question_id INT NOT NULL, "
	    		+ "author VARCHAR(100) NOT NULL, " 
	    		+ "content TEXT NOT NULL, " 
	    		+ "upvotes INT DEFAULT 0, "
	    		+ "FOREIGN KEY (question_id) REFERENCES Questions(id) ON DELETE CASCADE);";
	    statement.execute(answersTable); 
	    
	    String reviewsTable = "CREATE TABLE IF NOT EXISTS Reviews ("
	    		+ "id INT AUTO_INCREMENT PRIMARY KEY, "
	    		+ "question_id INT NOT NULL, "
	    		+ "author VARCHAR(100) NOT NULL, " 
	    		+ "content TEXT NOT NULL, " 
	    		+ "FOREIGN KEY (question_id) REFERENCES Questions(id) ON DELETE CASCADE);";
	    statement.execute(reviewsTable); 
	    
	    // Table for trusted reviewers
	    String reviewersTable = "CREATE TABLE IF NOT EXISTS Reviewers ("
	    		+ "name VARCHAR(100) NOT NULL, "
	    		+ "weight INT NOT NULL)";
	    statement.execute(reviewersTable);

	    // table for messages
	    String messagesTable = "CREATE TABLE IF NOT EXISTS Messages ("
	    	    + "id INT AUTO_INCREMENT PRIMARY KEY, "
	    	    + "sender VARCHAR(255) NOT NULL, "
	    	    + "receiver VARCHAR(255) NOT NULL, " 
	    	    + "content TEXT NOT NULL, " 
	    	    + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
	    	    + "FOREIGN KEY (sender) REFERENCES cse360users(userName) ON DELETE CASCADE, " 
	    	    + "FOREIGN KEY (receiver) REFERENCES cse360users(userName) ON DELETE CASCADE)";
	    statement.execute(messagesTable); 
	    
	    //table for ratings
	    String reviewRatingsTable = "CREATE TABLE IF NOT EXISTS ReviewRatings (" +
	    	    "id INT AUTO_INCREMENT PRIMARY KEY, " +
	    	    "review_id INT NOT NULL, " +
	    	    "student_username VARCHAR(255) NOT NULL, " +
	    	    "rating INT NOT NULL, " +
	    	    "CONSTRAINT unique_review_student UNIQUE (review_id, student_username), " +
	    	    "CONSTRAINT fk_review FOREIGN KEY (review_id) REFERENCES Reviews(id) ON DELETE CASCADE" +
	    	    ")";
	    	statement.execute(reviewRatingsTable);

	}


	// Check if the database is empty
	public boolean isDatabaseEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM cse360users";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}
	
	// print to database 
	public void printDatabase() throws SQLException {
		String query = "SELECT * FROM cse360users";
		
		try (PreparedStatement pstmt = connection.prepareStatement(query);
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				System.out.println(rs.getString("userName"));
			}
		}
	}

	// Registers a new user in the database.
	public void register(User user) throws SQLException {
		String insertUser = "INSERT INTO cse360users (name, email,userName, password, adminRole,studentRole,instructorRole,staffRole,reviewerRole) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, user.getName());
			pstmt.setString(2, user.getEmail());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getPassword());
			pstmt.setBoolean(5, user.isAdmin());
			pstmt.setBoolean(6, user.isStudent());
			pstmt.setBoolean(7, user.isInstructor());
			pstmt.setBoolean(8, user.isStaff());
			pstmt.setBoolean(9, user.isReviewer());
			pstmt.executeUpdate();
		}
	}

	// Validates a user's login credentials.
	public boolean login(User user) throws SQLException {
		String query = "SELECT * FROM cse360users WHERE userName = ? AND password = ? AND adminRole = ? AND studentRole = ? AND instructorRole = ? AND staffRole = ? AND reviewerRole = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setBoolean(3, user.isAdmin());
			pstmt.setBoolean(4, user.isStudent());
			pstmt.setBoolean(5, user.isInstructor());
			pstmt.setBoolean(6, user.isStaff());
			pstmt.setBoolean(7, user.isReviewer());
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		}
	}
	
	// Checks if a user already exists in the database based on their userName.
	public boolean doesUserExist(String userName) {
	    String query = "SELECT COUNT(*) FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the user exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume user doesn't exist
	}
	
	// Check if the user is a admin
	public boolean isUserAdmin(String userName) {
	    String query = "SELECT adminRole FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getBoolean("adminRole"); // Return the role if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // User does not have the admin role
	}
	
	// Check if the user is a student 
	public boolean isUserStudent(String userName) {
	    String query = "SELECT studentRole FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getBoolean("studentRole"); // Return the role if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // User does not have the student role
	}
	// Check if the user is a instructor 
	public boolean isUserInstructor(String userName) {
		String query = "SELECT instructorRole FROM cse360users WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, userName);
			    ResultSet rs = pstmt.executeQuery();
			        
			    if (rs.next()) {
			    	return rs.getBoolean("instructorRole"); // Return the role if user exists
			    }
			  } catch (SQLException e) {
			      e.printStackTrace();
			  }
			  return false; // User does not have the instructor role
		}
	// Check if the user is a staff
	public boolean isUserStaff(String userName) {
		String query = "SELECT staffRole FROM cse360users WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, userName);
			    ResultSet rs = pstmt.executeQuery();
			        
			    if (rs.next()) {
			    	return rs.getBoolean("staffRole"); // Return the role if user exists
			    }
			  } catch (SQLException e) {
			      e.printStackTrace();
			  }
			  return false; // User does not have the staff role
		}
	public boolean isUserReviewer(String userName) {
		String query = "SELECT reviewerRole FROM cse360users WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, userName);
			    ResultSet rs = pstmt.executeQuery();
			        
			    if (rs.next()) {
			    	return rs.getBoolean("reviewerRole"); // Return the role if user exists
			    }
			  } catch (SQLException e) {
			      e.printStackTrace();
			  }
			  return false; // User does not have the reviewer role
		}

	
	// initial roles set when creating invitation code
	public boolean[] initialRoles(String code) {
	    String query = "SELECT * FROM InvitationCodes WHERE code = ?";
	    boolean[] result = {false,false,false,false,false};
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            result[0] = rs.getBoolean("adminRole");
	            result[1] = rs.getBoolean("studentRole");
	            result[2] = rs.getBoolean("instructorRole");
	            result[3] = rs.getBoolean("staffRole");
	            result[4] = rs.getBoolean("reviewerRole");
	            
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return result; //return the roles that are assigned to the invitation code
	}		
	
	
	
	// return the amount of role that the user has been assigned
	public int numberOfRoles(String userName) {
		int total = 0;
		if(isUserAdmin(userName))
			total+=1;
		if(isUserStudent(userName))
			total+=1;
		if(isUserInstructor(userName))
			total+=1;
		if(isUserStaff(userName))
			total+=1;
		if(isUserReviewer(userName))
			total+=1;
		
		return total;
	}
	
	
	// Generates a new invitation code and inserts it into the database.
	public String generateInvitationCode(boolean adminRole, boolean studentRole, boolean instructorRole, boolean staffRole, boolean reviewerRole, Timestamp timeInviteMade) {
	    String code = UUID.randomUUID().toString().substring(0, 4); // Generate a random 4-character code
	    String query = "INSERT INTO InvitationCodes (code,adminRole,studentRole,instructorRole,staffRole,reviewerRole, timeInviteMade) VALUES (?,?,?,?,?,?,?)";

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.setBoolean(2, adminRole);
	        pstmt.setBoolean(3, studentRole);
	        pstmt.setBoolean(4, instructorRole);
	        pstmt.setBoolean(5, staffRole);
	        pstmt.setBoolean(6, reviewerRole);
	        pstmt.setTimestamp(7, timeInviteMade);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return code;
	}
	
	// Validates an invitation code to check if it is unused.
	public boolean validateInvitationCode(String code) {
	    String query = "SELECT * FROM InvitationCodes WHERE code = ? AND isUsed = FALSE";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            // Mark the code as used
	            markInvitationCodeAsUsed(code);
	            return true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	// return the timestamp in order to check if the invitation has expired
	public Timestamp getTimeInviteMade(String code) {
		String query = "SELECT * FROM InvitationCodes WHERE code = ? AND isUsed = TRUE";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	           
	            return rs.getTimestamp("timeInviteMade");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null; // should never happen, the code is validated before this function is called
	}
	
	
	// Marks the invitation code as used in the database.
	private void markInvitationCodeAsUsed(String code) {
	    String query = "UPDATE InvitationCodes SET isUsed = TRUE WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	// Marks the Delete User code as used in the database 
	public boolean deleteUser(String username) {
		//Prevent empty usernames
		if (username == null || username.trim().isEmpty()) {
	        return false;
	    }

	    try {
	        // Check if the user exists
	        String checkUserQuery = "SELECT * FROM cse360users WHERE userName = ?";
	        try (PreparedStatement pst = connection.prepareStatement(checkUserQuery)) {
	            pst.setString(1, username);
	            ResultSet ans = pst.executeQuery();
	            
	         // User not found
	            if (!ans.next()) {
	                return false; 
	            }
	         // Check if the user is an admin. If so, then Can't delete. 
	            if (isUserAdmin(username)) {
	                return false; 
	            }
	        }

	        // Delete user and return true if successful
	        String deleteQuery = "DELETE FROM cse360users WHERE userName = ?";
	        try (PreparedStatement deletePst = connection.prepareStatement(deleteQuery)) {
	            deletePst.setString(1, username);
	            int rowsAffected = deletePst.executeUpdate();
	            return rowsAffected > 0; 
	        }

	    } catch (SQLException e) {   // Database error
	        e.printStackTrace();
	        return false; 
	    }
	}
	
	public boolean add_remove_Role(String username, String action, String role) {
	    try {
	        // Check if user exists
	        if (!doesUserExist(username)) {
	            System.out.println("Error! User not found!");
	            return false;
	        }

	        // If the action is "remove" and the role is "adminRole", check if there's at least one admin left
	        if (action.equals("remove") && role.equals("adminRole")) {
	            String countAdminsQuery = "SELECT COUNT(*) FROM cse360users WHERE adminRole = TRUE";
	            ResultSet rs = statement.executeQuery(countAdminsQuery);
	            if (rs.next() && rs.getInt(1) <= 1) {
	                System.out.println("Error! At least one admin must remain in the system.");
	                return false;
	            }
	        }

	        // Update role
	        String updateQuery = "UPDATE cse360users SET " + role + " = ? WHERE userName = ?";
	        try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
	            pstmt.setBoolean(1, action.equals("add"));
	            pstmt.setString(2, username);
	            int rowsAffected = pstmt.executeUpdate();

	            if (rowsAffected > 0) {
	                System.out.println("Role " + (action.equals("add") ? "added to" : "removed from") + " " + username + " successfully!");
	                return true;
	            } else {
	                System.out.println("Error! Failed to update role.");
	                return false;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Database error occurred.");
	        return false;
	    }
	}

	// Generates a new onetime password and insert it into the database
	public String generateOnetimePassword(String username) {
		String password = OnetimePasswordGenerator.random_password();
		String query = "INSERT INTO OnetimePassword(password, username, isUsed, isForgotten) VALUES (?, ?, ?, ?)";
		
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, password);
			pstmt.setString(2, username);
			pstmt.setBoolean(3, false);
			pstmt.setBoolean(4, false);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return password;
	}
	
	// Validate a onetime password to check if it is unused
	public boolean validateOnetimePassword(String password, String username) {
		String query = "SELECT * FROM OnetimePassword WHERE password = ? AND username = ? AND isUsed = FALSE";
		
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, password);
			pstmt.setString(2, username);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				// Mark the password as used
				markOnetimePasswordAsUsed(password);
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
		
	// Mark the onetime password as used in the database
	private void markOnetimePasswordAsUsed(String password) {
		String query = "UPDATE OnetimePassword SET isUsed = TRUE WHERE password = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, password);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// change password 
	public void changePassword(String new_password, String username) {
		String query = "UPDATE cse360users SET password = ? WHERE userName = ?";
		
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, new_password);
			pstmt.setString(2, username);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public String retrieveUsersInfo() {
		String result = "";
		String query = "SELECT * FROM cse360users";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        ResultSet rs = pstmt.executeQuery();
	        
	        while(rs.next()) {
	        	String username = rs.getString("userName");
	        	result += rs.getString("name") + ",";
	        	result += rs.getString("email") + ",";
	        	result += username + ",";
	        	result += "Roles: ";
	        	if(isUserAdmin(username))
	        		result += "admin" + ",";
	        	if(isUserStudent(username))
	        		result += "student" + ",";
	        	if(isUserInstructor(username))
	        		result += "instructor" + ",";
	        	if(isUserStaff(username))
	        		result += "staff" + ",";
	        	if(isUserReviewer(username))
	        		result += "reviewer" + ",";
	        	result += "\n";
	          
	        }
	        return result;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return result; // Database emtpy
		
	}
	
	public User getUser(String username) {
		String query = "SELECT * FROM cse360users WHERE userName = ?";
		
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            User user = new User(rs.getString("name"), 
	            		rs.getString("email"), 
	            		rs.getString("userName"),
	            		rs.getString("password"),
	            		rs.getBoolean("adminRole"),
	            		rs.getBoolean("studentRole"),
	            		rs.getBoolean("instructorRole"),
	            		rs.getBoolean("staffRole"),
	            		rs.getBoolean("reviewerRole"));
	            System.out.println("User found!");
	            return user;
	        }
	    } catch (SQLException e) {
	        System.err.println("SQL Error during update: " + e.getMessage());
	        e.printStackTrace();
	    }
		return null;
	}
	
	// Closes the database connection and statement.
	public void closeConnection() {
		try{ 
			if(statement!=null) statement.close(); 
		} catch(SQLException se2) { 
			se2.printStackTrace();
		} 
		try { 
			if(connection!=null) connection.close(); 
		} catch(SQLException se){ 
			se.printStackTrace(); 
		} 
	}
	
    // - - - - - - - - - - - - - - - QUESTION METHODS - - - - - - - - - - - - - - 
	public void createQuestion(Question question) throws SQLException{
		String insertQuestion = "INSERT INTO Questions (title, content, author, category, resolved, upvotes) VALUES (?, ?, ?, ?, ?, ?)";
		
		try(PreparedStatement pstmt = connection.prepareStatement(insertQuestion)) {
			pstmt.setString(1, question.getTitle());
	        pstmt.setString(2, question.getContent());
	        pstmt.setString(3, question.getAuthor());
	        pstmt.setString(4, question.getCategory());
	        pstmt.setBoolean(5, question.getStatus());
	        pstmt.setInt(6, question.getUpvotes());  

	        pstmt.executeUpdate();  
	        System.out.println("Inserted question successfully!");
		}
	}
	
	
	public Question readQuestionById(int questionId) throws SQLException {
	    String query = "SELECT * FROM Questions WHERE id = ?";
	    
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setInt(1, questionId);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            // Create and return a Question object with retrieved values
	        	Question q = new Question(rs.getString("title"), rs.getString("content"), rs.getString("author"), rs.getString("category"));
	        	q.setId(questionId);
	            return q;
	        }
	    }
	    return null; // Return null if no question is found
	} 
	
	public boolean updateQuestion(int id, String title, String content, String category) throws SQLException {
		System.out.println("Updating question with id : " + id);
	    
	    String query = "UPDATE Questions SET title = ?, content = ?, category = ? WHERE id = ?";
	    
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, title);
	        pstmt.setString(2, content);
	        pstmt.setString(3, category);
	        pstmt.setInt(4, id);
	        
	        int rowsUpdated = pstmt.executeUpdate(); // Execute update

	        if (rowsUpdated > 0) {
	            System.out.println("Question updated successfully!");
	            return true;
	        } else {
	            System.out.println("No question found with ID: " + id);
	            return false;
	        }
	    } catch (SQLException e) {
	        System.err.println("SQL Error during update: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public boolean deleteQuestion(int id) {
		System.out.println("Deleting question with id : " + id);
		
		String query = "DELETE FROM Questions WHERE ID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setInt(1, id);
	        int rowsDeleted = pstmt.executeUpdate(); // Execute update

	        if (rowsDeleted > 0) {
	            System.out.println("Question deleted successfully!");
	            return true;
	        } else {
	            System.out.println("No question found with ID: " + id);
	            return false;
	        }
	    } catch (SQLException e) {
	        System.err.println("SQL Error during update: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
		
	}
	
	public List<Question> getQuestionTitles() throws SQLException {
		String query = "SELECT * FROM Questions";
	    List<Question> questions = new ArrayList<>();

	    try (PreparedStatement pstmt = connection.prepareStatement(query);
	         ResultSet rs = pstmt.executeQuery()) {

	        if (!rs.isBeforeFirst()) {  
	            System.out.println("No questions found in the database.");
	            return questions; // Return an empty list instead of null
	        }

	        while (rs.next()) {
	        	Question q = new Question(rs.getString("title"), rs.getString("content"), rs.getString("author"), rs.getString("category"));
	        	q.setId(rs.getInt("id"));
	            questions.add(q);
	        }
	    }

	    return questions;
	} 
	
	public void printQuestions() throws SQLException {
		String query = "SELECT * FROM Questions";
		
		try (PreparedStatement pstmt = connection.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery()) {
			
			// Checks if the result set is empty
	        if (!rs.isBeforeFirst()) {  
	            System.out.println("No questions found in the database.");
	            return;
	        }
	        
			while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + "| Title: " + rs.getString("title"));
			}
		}
	}
	// retrieve questions by author
	public List<Question> getQuestionByAuthor(String author) throws SQLException {
		String query = "SELECT * FROM Questions Where author = ?";
	    List<Question> questions = new ArrayList<>();

	    try(PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, author);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Question question = new Question(rs.getString("title"), rs.getString("content"), rs.getString("author"), rs.getString("category"));
				
				question.setId(rs.getInt("id"));
				questions.add(question);
			}
			
			return questions;
		}
		catch (SQLException e){
			System.err.println("SQL Error fetching answers: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	} 
	
	
	
	// - - - - - - - - - - - - - - - QUESTION METHODS END - - - - - - - - - - - - - -

	// - - - - - - - - - - - - - - - ANSWER METHODS - - - - - - - - - - - - - - - - - 
		public boolean createAnswer(Answer answer) {
			System.out.println("Inserting answer for question with id " + answer.getQuestionId());
			
			String query = "INSERT INTO Answers (question_id, author, content, upvotes) VALUES (?, ?, ?, 0)";
			
			try(PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setInt(1, answer.getQuestionId());
				pstmt.setString(2, answer.getAuthor());
				pstmt.setString(3, answer.getContent());
				
				int rowsInserted = pstmt.executeUpdate();
				
		        if (rowsInserted > 0) {
		            System.out.println("Answer inserted successfully!");
		            return true;
		        } else {
		            System.out.println("Failed to insert answer.");
		            return false;
		        }
			}
			catch (SQLException e) {
		        System.err.println("SQL Error during answer insertion: " + e.getMessage());
		        e.printStackTrace();
		        return false;
			}
		}
		
		public List<Answer> readAnswersByQuestionId(int questionId) throws SQLException {
			String query = "SELECT * FROM Answers WHERE question_id = ? ORDER BY upvotes DESC";
			List<Answer> answers = new ArrayList<>();
			
			try(PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setInt(1, questionId);
				
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()) {
					Answer answer = new Answer(rs.getInt("question_id"), rs.getString("author"), rs.getString("content"));
					
					answer.setId(rs.getInt("id"));
					answers.add(answer);
				}
				
				return answers;
			}
			catch (SQLException e){
				System.err.println("SQL Error fetching answers: " + e.getMessage());
				e.printStackTrace();
				return null;
			}
		   
		}
		
		public Answer readAnswerById(int answerId) throws SQLException {
		    String query = "SELECT * FROM Answers WHERE id = ?";
		    
		    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setInt(1, answerId);
		        ResultSet rs = pstmt.executeQuery();

		        if (rs.next()) {
		            // Create and return a Question object with retrieved values
		        	Answer a = new Answer(rs.getInt("question_id"), rs.getString("author"), rs.getString("content"));
		        	a.setId(answerId);
		            return a;
		        }
		    }
		    return null; // Return null if no question is found
		} 
		
		public boolean updateAnswer(int id, String content) throws SQLException {
			System.out.println("Updating answer with id : " + id);
		    
		    String query = "UPDATE Answers SET content = ? WHERE id = ?";
		    
		    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setString(1, content);
		        pstmt.setInt(2, id);
		        
		        int rowsUpdated = pstmt.executeUpdate(); // Execute update

		        if (rowsUpdated > 0) {
		            System.out.println("Answer updated successfully!");
		            return true;
		        } else {
		            System.out.println("No answer found with ID: " + id);
		            return false;
		        }
		    } catch (SQLException e) {
		        System.err.println("SQL Error during update: " + e.getMessage());
		        e.printStackTrace();
		        return false;
		    }
		}
		
		public boolean deleteAnswer(int id) {
			System.out.println("Deleting question with id : " + id);
			
			String query = "DELETE FROM Answers WHERE ID = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setInt(1, id);
		        int rowsDeleted = pstmt.executeUpdate(); // Execute update

		        if (rowsDeleted > 0) {
		            System.out.println("Answer deleted successfully!");
		            return true;
		        } else {
		            System.out.println("No answer found with ID: " + id);
		            return false;
		        }
		    } catch (SQLException e) {
		        System.err.println("SQL Error during update: " + e.getMessage());
		        e.printStackTrace();
		        return false;
		    }
			
		}
		// upvote incrementation
		public void incrementUpvote(int answerId){
			String query = "UPDATE Answers SET upvotes=upvotes+1 WHERE id = ? ";
			
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setInt(1, answerId);
		        pstmt.executeUpdate(); // Execute update
		    } catch (SQLException e) {
		        System.err.println("SQL Error during update: " + e.getMessage());
		        e.printStackTrace();
		    }
		}
		public int getUpvote(int answerId){
			String query = "SELECT upvotes FROM Answers WHERE id =?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setInt(1, answerId);
		        ResultSet rs = pstmt.executeQuery();
		        if(rs.next()) {
		        	return rs.getInt("upvotes");   
		        	}   
		        return 0; 
		    } catch (SQLException e) {
		        System.err.println("SQL Error during update: " + e.getMessage());
		        e.printStackTrace();
		        return 0;
		    }
		}
		//Retrieve answer based on author
		public List<Answer> readAnswersByAuthor(String author) throws SQLException {
			String query = "SELECT * FROM Answers WHERE author = ?";
			List<Answer> answers = new ArrayList<>();
			
			try(PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, author);
				
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()) {
					Answer answer = new Answer(rs.getInt("question_id"), rs.getString("author"), rs.getString("content"));
					
					answer.setId(rs.getInt("id"));
					answers.add(answer);
				}
				
				return answers;
			}
			catch (SQLException e){
				System.err.println("SQL Error fetching answers: " + e.getMessage());
				e.printStackTrace();
				return null;
			}
		   
		}
		
		
		
		// - - - - - - - - - - - - - - - ANSWER METHODS END - - - - - - - - - - - - - - - - -
		
		// - - - - - - - - - - - - - - - MESSAGE METHODS - - - - - - - - - - - - - - - - -
		
		public void sendMessage(Message message) {
			String query = "INSERT INTO Messages (sender, receiver, content) VALUES (?, ?, ?)";
			
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setString(1, message.getSender());
		        pstmt.setString(2, message.getReceiver());
		        pstmt.setString(3, message.getContent());
		        
		        int rowsSent = pstmt.executeUpdate(); // Execute update

		        if (rowsSent > 0) {
		            System.out.println("Message sent successfully!");
		        } else {
		            System.out.println("Message failed to send!");
		        }
		    } catch (SQLException e) {
		        System.err.println("SQL Error during update: " + e.getMessage());
		        e.printStackTrace();
		    }
		}
		
		public List<Message> getMessages(User user) {
			String query = "SELECT * FROM Messages WHERE receiver = ? ORDER BY timestamp DESC";
			List<Message> mList = new ArrayList<>();
			
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setString(1, user.getUserName());
				
		        ResultSet rs = pstmt.executeQuery();
		        while (rs.next()) {
		        	mList.add(new Message(rs.getInt("id"), 
		        			rs.getString("sender"), 
		        			rs.getString("receiver"), 
		        			rs.getString("content"), 
		        			rs.getTimestamp("timestamp")));
		        }
		        
		    } catch (SQLException e) {
		        System.err.println("SQL Error during update: " + e.getMessage());
		        e.printStackTrace();
		    }
			
			return mList;
		}
		
		
		
		public List<Message> getConversation(String user1, String user2) {
			String query = "SELECT * FROM Messages WHERE (sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?) ORDER BY timestamp";
			List<Message> mList = new ArrayList<>();
			
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, user1);
				pstmt.setString(2, user2);
				pstmt.setString(3, user2);
				pstmt.setString(4, user1);
		        
		        ResultSet rs = pstmt.executeQuery();
		        while (rs.next()) {
		        	mList.add(new Message(rs.getInt("id"), 
		        			rs.getString("sender"), 
		        			rs.getString("receiver"), 
		        			rs.getString("content"), 
		        			rs.getTimestamp("timestamp")));
		        }
		        
		    } catch (SQLException e) {
		        System.err.println("SQL Error during update: " + e.getMessage());
		        e.printStackTrace();
		    }
			
			return mList;
		}
		
		// - - - - - - - - - - - - - - - MESSAGE METHODS END - - - - - - - - - - - - - - - - -
		
		// - - - - - - - - - - - - - - - REVIEWS METHODS START - - - - - - - - - - - - - - - - -
		// Create a new review
		public boolean createReview(Review review) {
		    String query = "INSERT INTO Reviews (question_id, author, content) VALUES (?, ?, ?)";
		    
		    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setInt(1, review.getQuestionId());
		        pstmt.setString(2, review.getAuthor());
		        pstmt.setString(3, review.getContent());
		        
		        int rowsInserted = pstmt.executeUpdate();
		        
		        if (rowsInserted > 0) {
		            System.out.println("Review inserted successfully!");
		            return true;
		        } else {
		            System.out.println("Failed to insert review.");
		            return false;
		        }
		    }
		    catch (SQLException e) {
		        System.err.println("SQL Error during review insertion: " + e.getMessage());
		        e.printStackTrace();
		        return false;
		    }
		}

		// Update an existing review
		public boolean updateReview(int id, String content) throws SQLException {
		    System.out.println("Updating review with id: " + id);
		    
		    String query = "UPDATE Reviews SET content = ? WHERE id = ?";
		    
		    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setString(1, content);
		        pstmt.setInt(2, id);
		        
		        int rowsUpdated = pstmt.executeUpdate();

		        if (rowsUpdated > 0) {
		            System.out.println("Review updated successfully!");
		            return true;
		        } else {
		            System.out.println("No review found with ID: " + id);
		            return false;
		        }
		    } catch (SQLException e) {
		        System.err.println("SQL Error during update: " + e.getMessage());
		        e.printStackTrace();
		        return false;
		    }
		}

		// Delete a review
		public boolean deleteReview(int id) {
		    System.out.println("Deleting review with id: " + id);
		    
		    String query = "DELETE FROM Reviews WHERE id = ?";
		    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setInt(1, id);
		        int rowsDeleted = pstmt.executeUpdate();

		        if (rowsDeleted > 0) {
		            System.out.println("Review deleted successfully!");
		            return true;
		        } else {
		            System.out.println("No review found with ID: " + id);
		            return false;
		        }
		    } catch (SQLException e) {
		        System.err.println("SQL Error during deletion: " + e.getMessage());
		        e.printStackTrace();
		        return false;
		    }
		}

		// Get a single review by its ID
		public Review getReviewById(int reviewId) throws SQLException {
		    String query = "SELECT * FROM Reviews WHERE id = ?";
		    
		    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setInt(1, reviewId);
		        ResultSet rs = pstmt.executeQuery();

		        if (rs.next()) {
		            Review review = new Review(
		                rs.getInt("question_id"),
		                rs.getString("author"),
		                rs.getString("content")
		            );
		            review.setId(reviewId);
		            return review;
		        }
		    }
		    return null; // Return null if no review is found
		}
		
		
		public List<Review> getReviewsByQuestionId(int questionId) throws SQLException {
		    String query = "SELECT * FROM Reviews WHERE question_id = ?";
		    List<Review> reviews = new ArrayList<>();
		    
		    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setInt(1, questionId);
		        
		        ResultSet rs = pstmt.executeQuery();
		        
		        while (rs.next()) {
		            Review review = new Review(
		                rs.getInt("question_id"), 
		                rs.getString("author"), 
		                rs.getString("content")
		            );
		            
		            review.setId(rs.getInt("id"));
		            reviews.add(review);
		        }
		        
		        return reviews;
		    }
		    catch (SQLException e) {
		        System.err.println("SQL Error fetching reviews: " + e.getMessage());
		        e.printStackTrace();
		        return new ArrayList<>(); // Return empty list instead of null
		    }
		}
		
		
		public List<Review> getAllReviews(String username) throws SQLException {
		    String query = "SELECT * FROM Reviews WHERE author = ?";
		    List<Review> reviews = new ArrayList<>();
		    
		    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setString(1, username);
		        
		        ResultSet rs = pstmt.executeQuery();
		        
		        while (rs.next()) {
		            Review review = new Review(
		                rs.getInt("question_id"), 
		                rs.getString("author"), 
		                rs.getString("content")
		            );
		            
		            review.setId(rs.getInt("id"));
		            reviews.add(review);
		        }
		        
		        return reviews;
		    }
		    catch (SQLException e) {
		        System.err.println("SQL Error fetching reviews: " + e.getMessage());
		        e.printStackTrace();
		        return new ArrayList<>(); // Return empty list instead of null
		    }
		}

		//Retrieve all of users that request to become a reviewer
		public List<User> retrieveStudentsReviewerRequest() {
				String query = "SELECT * FROM cse360users WHERE studentRole = TRUE AND reviewerRole = FALSE AND requestReviewerRole = TRUE";
				List<User> result = new ArrayList<>();
				try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			       
			        ResultSet rs = pstmt.executeQuery();
			        
			       while(rs.next()) {
			            result.add(new User(rs.getString("name"), 
			            		rs.getString("email"), 
			            		rs.getString("userName"),
			            		rs.getString("password"),
			            		rs.getBoolean("adminRole"),
			            		rs.getBoolean("studentRole"),
			            		rs.getBoolean("instructorRole"),
			            		rs.getBoolean("staffRole"),
			            		rs.getBoolean("reviewerRole")));
			            System.out.println("User found!");
			           
			              
			    }
			  
				}catch (SQLException e) {
			        System.err.println("SQL Error during update: " + e.getMessage());
			        e.printStackTrace();
			    }
				return result;
			}
		
		

		// - - - - - - - - - - - - - - - REVIEWS METHODS END - - - - - - - - - - - - - - - - -


		// - - - - - - - - - - - - - - - QUESTIONS LIST METHOD TO SEARCH QUESTION END  - - - - - - - - - - - - - - - - -

		public List<Question> getQuestions() throws SQLException {
		    List<Question> questions = new ArrayList<>();
		    String sql = "SELECT * FROM Questions"; 
		    try (PreparedStatement pstmt = connection.prepareStatement(sql);
		         ResultSet rs = pstmt.executeQuery()) {
		        while (rs.next()) {
		            Question question = new Question(
		                rs.getString("title"),
		                rs.getString("content"),
		                rs.getString("author"),
		                rs.getString("category")
		            );
		            questions.add(question);
		        }
		    }
		    return questions;
		}
		
		// - - - - - - - - - - - - - - - QUESTIONS LIST METHOD TO SEARCH QUESTION END  - - - - - - - - - - - - - - - - -
		
		// - - - - - - - - - - - - - - - RATINGS METHODS - - - - - - - - - - - - - - - - -
		
		 //add or update a rating
		 public void addOrUpdateReviewRating(int reviewId, String studentUsername, int rating) throws SQLException {
		     connectToDatabase();
		     String query = "MERGE INTO ReviewRatings (review_id, student_username, rating) KEY (review_id, student_username) VALUES (?, ?, ?)";
		     try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		         pstmt.setInt(1, reviewId);
		         pstmt.setString(2, studentUsername);
		         pstmt.setInt(3, rating);
		         pstmt.executeUpdate();
		     }
		 }
		 
		 //average ratings for all reviews
		 public Map<Integer, Double> getAverageRatingsForAllReviews() throws SQLException {
		     connectToDatabase();
		     Map<Integer, Double> avgRatings = new HashMap<>();

		     String query = "SELECT review_id, AVG(rating) AS avg_rating FROM ReviewRatings GROUP BY review_id";
		     try (Statement stmt = connection.createStatement();
		          ResultSet rs = stmt.executeQuery(query)) {
		         while (rs.next()) {
		             avgRatings.put(rs.getInt("review_id"), rs.getDouble("avg_rating"));
		         }
		     }

		     return avgRatings;
		 }
		 
		 
}
