package application;

/**
 * The User class represents a user entity in the system.
 * It contains the user's details such as name, email, userName, password, and role.
 */
public class User {
	private String name; 
	private String email; 
    private String userName;
    private String password;
    private boolean adminRole;
    private boolean studentRole;
    private boolean instructorRole;
    private boolean staffRole;
    private boolean reviewerRole;

    // Constructor to initialize a new User object with userName, password, and role.
    public User( String userName, String password, boolean adminRole, boolean studentRole, boolean instructorRole, boolean staffRole, boolean reviewerRole) {
        this.userName = userName;
        this.password = password;
        this.adminRole = adminRole;
        this.studentRole= studentRole;
        this.instructorRole= instructorRole;
        this.staffRole= staffRole;
        this.reviewerRole = reviewerRole;
    }
    // Constructor to initialize a new User object with name, email, userName, password, and role. 
    public User(String name, String email, String userName, String password, boolean adminRole, boolean studentRole, boolean instructorRole, boolean staffRole, boolean reviewerRole) {
        this.name = name; 
        this.email = email; 
    	this.userName = userName;
        this.password = password;
        this.adminRole = adminRole;
        this.studentRole= studentRole;
        this.instructorRole= instructorRole;
        this.staffRole= staffRole;
        this.reviewerRole = reviewerRole;
    }
    
    // Sets the role of the user.
    public void setAdminRole(boolean adminRole) {
    	this.adminRole=adminRole;
    }
    public void setStudentRole(boolean studentRole) {
    	this.studentRole=studentRole;
    }
    public void setInstructorRole(boolean instructorRole) {
    	this.instructorRole=instructorRole;
    }
    public void setStaffRole(boolean staffRole) {
    	this.staffRole=staffRole;
    }
    public void setReviewerRole(boolean reviewerRole) {
    	this.reviewerRole=reviewerRole;
    }
    
    // accessor method for the current role of users
    public String getName() { return name; }
    public String getEmail() { return email;}
    public String getUserName() { return userName; }
    public String getPassword() { return password; }
    public boolean isAdmin() { return adminRole; }
    public boolean isStudent() { return studentRole; }
    public boolean isInstructor() { return instructorRole; }
    public boolean isStaff() { return staffRole; }
    public boolean isReviewer() { return reviewerRole; }
}
