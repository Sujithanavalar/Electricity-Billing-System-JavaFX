

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.text.NumberFormat;
import java.util.Locale;

public class twilio extends Application {

    private String correctUsername = "user"; // Hardcoded username
    private String correctPassword = "password"; // Hardcoded password
    private String defaultSecurityCode = "1234"; // Default security code

    // Twilio credentials
    public static final String ACCOUNT_SID = "YOUR_ACCOUNT_SID"; // Replace with your Twilio Account SID
    public static final String AUTH_TOKEN = "YOUR_AUTH_TOKEN";   // Replace with your Twilio Auth Token
    public static final String TWILIO_PHONE_NUMBER = "YOUR_TWILIO_PHONE_NUMBER"; // Replace with your Twilio phone number

    @Override
    public void start(Stage primaryStage) {
        // Initialize Twilio
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        // Create the login UI components
        Label labelUsername = new Label("Username:");
        TextField tfUsername = new TextField();
        tfUsername.setMaxWidth(200); // Set max width

        Label labelPassword = new Label("Password:");
        PasswordField pfPassword = new PasswordField();
        pfPassword.setMaxWidth(200); // Set max width

        Button btnLogin = new Button("Login");
        Label labelLoginMessage = new Label();

        // Layout for the login screen
        VBox loginLayout = new VBox(10);
        loginLayout.setStyle("-fx-padding: 20;");
        loginLayout.getChildren().addAll(labelUsername, tfUsername, labelPassword, pfPassword, btnLogin ,labelLoginMessage);

        // Create the billing system UI components
        Label labelUnits = new Label("Enter Units Consumed:");
        TextField tfUnits = new TextField();
        tfUnits.setMaxWidth(200); // Set max width

        Button btnCalculate = new Button("Calculate Bill");
        Label labelResult = new Label("Total Bill: ₹0.00");

        Label labelPayment = new Label("Enter Payment Amount:");
        TextField tfPayment = new TextField();
        tfPayment.setMaxWidth(200); // Set max width

        // New components for payment method and security code
        Label labelPaymentMethod = new Label("Select Payment Method:");
        ComboBox<String> comboPaymentMethod = new ComboBox<>();
        comboPaymentMethod.getItems().addAll("Credit Card", "Debit Card", "UPI");

        Label labelSecurityCode = new Label("Enter Security Code:");
        TextField tfSecurityCode = new TextField();
        tfSecurityCode.setMaxWidth(200); // Set max width
        // Set default security code

        Label labelPhoneNumber = new Label("Enter Phone Number:");
        TextField tfPhoneNumber = new TextField();
        tfPhoneNumber.setMaxWidth(200); // Set max width

        Button btnPay = new Button("Pay Bill");
        Label labelPaymentStatus = new Label();

        // Layout for the billing system
        VBox billingLayout = new VBox(10);
        billingLayout.setStyle("-fx-padding: 20;");
        billingLayout.getChildren().addAll(
                labelUnits, tfUnits, btnCalculate, labelResult,
                labelPayment, tfPayment,
                labelPaymentMethod, comboPaymentMethod,
                labelSecurityCode, tfSecurityCode,
                labelPhoneNumber, tfPhoneNumber,
                btnPay, labelPaymentStatus
        );

        // Variable to store the total bill amount
        double[] totalBill = new double[1];

        // Number formatter for Indian Rupees
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

        // Define button action for Login
        btnLogin.setOnAction(e -> {
            String username = tfUsername.getText();
            String password = pfPassword.getText();

            if (username.equals(correctUsername) && password.equals(correctPassword)) {
                // If login is successful, switch to the billing screen
                primaryStage.setScene(new Scene(billingLayout, 350, 470));
            } else {
                // Show login failure message
                labelLoginMessage.setText("Invalid username or password.");
            }
        });

        // Define button action for Calculate Bill
        btnCalculate.setOnAction(e -> {
            try {
                double unitsConsumed = Double.parseDouble(tfUnits.getText());
                totalBill[0] = calculateBill(unitsConsumed);
                labelResult.setText("Total Bill: " + currencyFormatter.format(totalBill[0]));
                labelPaymentStatus.setText("");  // Clear previous payment status
            } catch (NumberFormatException ex) {
                labelResult.setText("Please enter a valid number.");
            }
        });

        // Define button action for Pay Bill
        btnPay.setOnAction(e -> {
            try {
                double paymentAmount = Double.parseDouble(tfPayment.getText());
                String paymentMethod = comboPaymentMethod.getValue();
                String securityCode = tfSecurityCode.getText();
                String phoneNumber = tfPhoneNumber.getText();

                // Validate payment method, security code, and phone number
                if (paymentMethod == null || paymentMethod.isEmpty()) {
                    labelPaymentStatus.setText("Please select a payment method.");
                    return;
                }

                if (!securityCode.equals(defaultSecurityCode)) {
                    labelPaymentStatus.setText("Invalid security code.");
                    return;
                }

                if (phoneNumber.isEmpty()) {
                    labelPaymentStatus.setText("Please enter a phone number.");
                    return;
                }

                // Check the payment amount
                String smsMessage;
                if (paymentAmount < totalBill[0]) {
                    double balanceDue = totalBill[0] - paymentAmount;
                    smsMessage = "Payment successful! Balance Due: " + currencyFormatter.format(balanceDue);
                    labelPaymentStatus.setText(smsMessage);
                } else if (paymentAmount == totalBill[0]) {
                    smsMessage = "Payment successful! No balance due.";
                    labelPaymentStatus.setText(smsMessage);
                } else {
                    double refund = paymentAmount - totalBill[0];
                    smsMessage = "Payment successful! Refund: " + currencyFormatter.format(refund);
                    labelPaymentStatus.setText(smsMessage);
                }

                // Send SMS
                sendSms(phoneNumber, smsMessage);

            } catch (NumberFormatException ex) {
                labelPaymentStatus.setText("Please enter a valid payment amount.");
            }
        });

        // Set up the scene for the login screen
        Scene loginScene = new Scene(loginLayout, 300, 350);
        primaryStage.setTitle("Electricity Billing System - Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    // Method to calculate the bill
    private double calculateBill(double units) {
        double bill = 0.0;

        if (units <= 100) {
            bill = units * 1.5;  // First 100 units at ₹1.50/unit
        } else if (units <= 300) {
            bill = (100 * 1.5) + ((units - 100) * 2.5);  // Next 200 units at ₹2.50/unit
        } else {
            bill = (100 * 1.5) + (200 * 2.5) + ((units - 300) * 3.5);  // Above 300 units at ₹3.50/unit
        }

        return bill;
    }

    // Method to send SMS using Twilio
    private void sendSms(String phoneNumber, String message) {
        Message.creator(
                new com.twilio.type.PhoneNumber("YOUR_PHONE_NUMBER"), // To
                new com.twilio.type.PhoneNumber("YOUR_TWILIO_PHONE_NUMBER"), // From
                message
        ).create();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

