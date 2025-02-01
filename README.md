📌 Smart Contact Manager

A secure contact management web application built using Spring Boot, Spring Security, and OAuth2 authentication. Users can store, manage, and search for contacts, with support for Google and GitHub login, OTP verification, payment integration, and cloud image uploads.

🛠 Features

✅ User Authentication: OAuth2 login with Google & GitHub

✅ Secure Contact Management: Add, edit, delete contacts

✅ OTP Verification: Secure email-based authentication

✅ Payment Integration: Allows Donation features through payments

✅ Cloud Image Upload: Upload and manage contact images

🚀 Tech Stack

Backend: Spring Boot, Spring Security, Spring JDBC

Frontend: Thymeleaf, Bootstrap, HTML, CSS, JavaScript

Database: MySQL

Authentication: OAuth2 (Google, GitHub)

Cloud Services: Cloudinary (for image uploads)

Payment Gateway: Stripe (for payment integration)

🏗 Installation & Setup

1️⃣ Clone the Repository

git clone 
https://github.com/Vipulkjayant/SmartContactManager.git
cd SmartContactManager

2️⃣ Set Up the Database (MySQL)

CREATE DATABASE smartcontact_manager;

spring.datasource.url=jdbc:mysql://localhost:3306/smartcontact_manager

spring.datasource.username=your-username

spring.datasource.password=your-password

3️⃣ Configure OAuth2 Credentials

Do NOT hardcode credentials in application.properties.
Instead, set them as environment variables:

set GOOGLE_CLIENT_ID=your-client-id
set GOOGLE_CLIENT_SECRET=your-client-secret

export GOOGLE_CLIENT_ID=your-client-id
export GOOGLE_CLIENT_SECRET=your-client-secret

spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}

4️⃣ Install Dependencies & Build the Project

mvn clean install

5️⃣ Run the Application

The app will be available at:
http://localhost:8080

🔐 Rules for Using Locally

✔️ Set up OAuth credentials locally (don’t expose them)

✔️ Add application.properties to .gitignore (to prevent accidental leaks)

✔️ Use environment variables for sensitive data

✔️ Ensure MySQL is running before starting the project

🛠 Future Enhancements

🚀 Mobile App Integration

🚀 AI-Based Contact Suggestions

🚀 Advanced Search & Filters

🤝 Contributing

Fork the repository

Create a new branch (feature-branch)

Commit your changes

Push to your branch and create a Pull Request

📬 Contact
👤 Vipul Jayant
📧 [vipuljayant151@gmail.com]
