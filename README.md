# MobileApp_Kaami
Intern Assignment for Kaami. 

## Technology Used
1. Android
2. Java
3. XML
4. Firebase

## Limitations
1. Email Verification or Login OTP verification at one particular time.
2. Internet connection is must for running the app.

### Follow the Working section to see the functionality of the app.

## Screenshots

![alt text](https://raw.githubusercontent.com/NikhilMishra1999/MobileApp_Kaami/nikhil_work/Screenshots/welcome_activity.jpg)
![alt text](https://raw.githubusercontent.com/NikhilMishra1999/MobileApp_Kaami/nikhil_work/Screenshots/signup_activity.jpg)
![alt text](https://raw.githubusercontent.com/NikhilMishra1999/MobileApp_Kaami/nikhil_work/Screenshots/login_activity.jpg)
![alt text](https://raw.githubusercontent.com/NikhilMishra1999/MobileApp_Kaami/nikhil_work/Screenshots/otp_check.jpg)
![alt text](https://raw.githubusercontent.com/NikhilMishra1999/MobileApp_Kaami/nikhil_work/Screenshots/email_verify.jpg)
![alt text](https://raw.githubusercontent.com/NikhilMishra1999/MobileApp_Kaami/nikhil_work/Screenshots/home_activity.jpg)


## Working
1. User signs up to the application with Sign-up Verification provided by firebase and gets registered in DB, options for signup are:
  a. Google auth
  b. Facebook auth
  c. Email-Password
2. User Verification:
  a. Phone OTP
  b. Email verification
3. Details are sent to the DB i.e. Firebase Firestore with a collection named 'users'.
4. On the off chance that user wants to signout he/she can 'Logout'.
