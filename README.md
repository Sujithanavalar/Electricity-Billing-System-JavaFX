Electricity Billing System with SMS Notification
A JavaFX application that provides electricity billing services with integrated SMS notifications via Twilio. The system supports secure login, dynamic bill calculation, and payment handling through a sleek graphical interface.

Features
- JavaFX GUI for user interaction
- Login authentication with username and password
- Bill calculation based on unit slabs
- Payment processing with method selection and security code
- SMS notifications sent via Twilio API

Technologies Used
| Technology | Purpose | 
| JavaFX | User interface | 
| Twilio API | SMS functionality | 
| Java SE | Core application logic | 
| NumberFormat | Currency formatting (INR) | 



Prerequisites
- JDK 8 or higher
- JavaFX SDK
- Twilio Java Helper Library

Setup Instructions
- Clone the Repository
git clone https://github.com/your-username/ElectricityBillingSystem.git
cd ElectricityBillingSystem
- Add Twilio SDK
- Download twilio-java-sdk.jar from Twilio and include it in your classpath.
- Configure Credentials
- Replace placeholders in the main class twilio.java:
public static final String ACCOUNT_SID = "YOUR_ACCOUNT_SID";
public static final String AUTH_TOKEN = "YOUR_AUTH_TOKEN";
public static final String TWILIO_PHONE_NUMBER = "YOUR_TWILIO_PHONE_NUMBER";
- Run the Application
- Compile and run using your preferred IDE or via command line:
javac -cp "lib/twilio-java-sdk.jar" twilio.java
java -cp ".;lib/twilio-java-sdk.jar" twilio
How It Works- Login Page: Validates hardcoded credentials
- Bill Calculation: Calculates bill based on slabs:
- ₹1.50/unit for first 100 units
- ₹2.50/unit for next 200 units
- ₹3.50/unit for usage above 300 units
- Payment: Accepts method, checks security code, and compares payment amount with bill
- SMS Sending: Uses Twilio API to send confirmation message to the provided mobile number
File StructureElectricityBillingSystem/
├── src/
│   └── twilio.java
├── lib/
│   └── twilio-java-sdk.jar
└── README.md
Notes- For simplicity, user credentials and security code are hardcoded
- Twilio phone number and recipient number should be in E.164 format (e.g., "+91XXXXXXXXXX")
- Customize UI components and business logic as needed
If you'd like help creating .gitignore, .classpath, or a license file to match this, I’d be happy to add that too. Just let me know how you'd like to polish it further.
