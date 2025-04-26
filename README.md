# 📱 Android Developer Assignment


An Android application demonstrating Firebase Authentication, FCM Push Notifications, image handling,
and PDF viewing using modern Android development practices.

---

## ✨ Features Implemented

### 🔐 1. User Authentication
- Google Sign-In using Firebase Authentication.
- Stores user data in Room Database. 
- You can verify that the user is saved successfully in the database using the log:
  Log.d("DB_CHECK", "Saved User: $savedUser") 

### 📄 2. PDF Viewer
- Displays a remote PDF using a third-party viewer.
- PDF URL: [View PDF](https://fssservices.bookxpert.co/GeneratedPDF/Companies/nadc/2024-2025/BalanceSheet.pdf)

### 📷 3. Image Capture & Gallery Selection
- Capture image using the device's camera.
- Select image from the gallery.
- Displays the selected or captured image in an ImageView.

### 🛠️ 4. Room DB & API Integration *(In Progress )*
- Will fetch data from `https://api.restful-api.dev/objects` using Retrofit.
- Plan to store data in Room Database.
- Will include Update and Delete operations.
- To include error handling and input validation.
- ⚠️ Not yet added to the project — will be updated once completed.

### 🔔 5. Push Notifications
- Sends a Firebase Cloud Messaging (FCM) notification when an item is deleted.
- Notifications include item details.
- Users can enable or disable notifications via Shared Preferences.


---

## 🛠️ Tech Stack

- **Kotlin**
- **MVVM Architecture**
- **Firebase Authentication & FCM**
- **Room DB** 
- **Glide**
- **PDF Viewer Library**
- **LiveData / ViewModel**


---


## 📦 Libraries Used

- Firebase Auth + Messaging
- Coil
- Room Database 
- AndroidPdfViewer ("com.github.mhiew:android-pdf-viewer:3.2.0-beta.3")

---

## ✅ Permissions Used

- `INTERNET`
- `CAMERA`
- `READ_EXTERNAL_STORAGE`
- `POST_NOTIFICATIONS` (Android 13+)

---

## 🚀 How to Build the APK

### Installation and Build APK Instructions
1. Clone this repo:
   ```bash
   git clone https://github.com/ArunkumarThatikanti/prices-tracker.git

2. Open the Project in Android Studio
- Launch Android Studio.
- Choose "File > Open > Navigate to the folder you just cloned" or you can click on "file > new > project from version control > paste clone url".
- Go to Build > Build Bundle(s) / APK(s) > Build APK(s).
  After the build completes, click "locate" to find the APK in:
  app/build/outputs/apk/debug/app-debug.apk
- you can add this apk to mobile device and install.
