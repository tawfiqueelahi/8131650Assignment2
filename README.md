
# 8131650Assignment2

## NIT3213 - Android Application Development
**Assignment 2: API Integration Android Application**

**Student:** Tawfique Elahi  
**Student ID:** s8131650  
**Campus:** Sydney

## 1. Project Overview

This project is a native Android application developed in Kotlin for NIT3213 Assignment 2.

The application integrates with the university's REST API to authenticate students and retrieve a collection of entities.

It follows the Model-View-ViewModel (MVVM) architectural pattern and uses Hilt for dependency injection.

### Main Features

- Student authentication through the university API
- Login validation and error handling
- Dashboard displaying entities using RecyclerView
- Dynamic data retrieval using the authentication keypass
- Details screen displaying all entity properties
- Loading indicators and network error handling
- Unit tests for critical ViewModel functionality

## 2. Technologies Used

| Technology | Purpose |
|---|---|
| Kotlin | Main programming language |
| Android SDK | Native Android development |
| XML | User interface layouts |
| MVVM | Application architecture |
| Hilt | Dependency injection |
| Retrofit | REST API integration |
| Gson | JSON parsing |
| Kotlin Coroutines | Asynchronous operations |
| StateFlow | ViewModel UI state management |
| RecyclerView | Dashboard entity list |
| JUnit | Unit testing |
| Git and GitHub | Version control |

## 3. Application Screens

### Login Screen
Students enter their student ID without the leading 's' and their case-sensitive first name as the password.

The application validates the input and sends an authentication request.

### Dashboard Screen
After authentication, the application uses the returned keypass to retrieve entities.

The entities are displayed in a RecyclerView. Each item shows a summary without the full description.

### Details Screen
Selecting a dashboard entity opens the Details screen.

This screen displays all entity properties, including the complete description.

## 4. API Integration

**Base URL:**

https://nit3213apinew.onrender.com/

### Authentication

Endpoint: `POST /sydney/auth`

Example request:

```json
{
  "username": "YourStudentID",
  "password": "YourFirstName"
}
```

Successful response:

```json
{
  "keypass": "topicName"
}
```

### Dashboard Data

Endpoint: `GET /dashboard/{keypass}`

The keypass is obtained dynamically from the authentication response.

The dashboard response includes an `entities` list and an `entityTotal` value.

The application supports entities with different properties rather than relying on hardcoded data.

## 5. Architecture

The application follows the MVVM architecture.

### UI Layer
Activities and XML layouts display information and respond to user interactions.

### ViewModel Layer
ViewModels manage UI states, input validation and communication with repositories.

### Repository Layer
The repository provides an abstraction between ViewModels and API operations.

### Network Layer
Retrofit handles HTTP requests and Gson converts JSON responses into application data models.

### Dependency Injection
Hilt provides dependencies such as Retrofit, ApiService and AppRepository.

This reduces unnecessary coupling and improves maintainability and testability.

### Data Flow

User Interface
↓
ViewModel
↓
Repository
↓
Retrofit API Service
↓
University REST API

## 6. Requirements

To build and run this application:

- Android Studio
- Android SDK
- Minimum Android SDK: API 26
- Internet connection
- Android emulator or physical Android device
- University-issued student login credentials

## 7. Installation and Setup

1. Clone or download the GitHub repository.
2. Open Android Studio.
3. Select **Open** and choose the project folder.
4. Allow Gradle to sync and download dependencies.
5. Select an Android emulator or connect an Android device.
6. Click **Run** to build and launch the application.

No separate API key configuration is required.

## 8. How to Use the Application

1. Launch the application.
2. Enter the student ID without the leading 's'.
3. Enter your registered first name in ALL CAPITAL LETTERS as the password. The API password is case-sensitive.
4. Press LOGIN.
5. Wait for authentication to complete.
6. Browse the retrieved entities on the Dashboard.
7. Tap an entity to view its complete details.
8. Use the Back button to return to the Dashboard.

## 9. Unit Testing

The application includes local unit tests for its ViewModels.

Tests use a fake repository to simulate API responses without making real network requests.

Test scenarios include:

- Empty student ID validation
- Empty password validation
- Successful login response
- Login network failure
- Successful dashboard retrieval
- Missing keypass handling
- Dashboard network failure

### Running Tests

Open the project terminal.

On Windows:

```powershell
.\gradlew.bat testDebugUnitTest
```

On macOS/Linux:

```bash
./gradlew testDebugUnitTest
```

Test results can be viewed in Android Studio or in the generated HTML test report.

## 10. Error Handling

The application provides feedback for invalid credentials, missing input and network-related failures.

Loading indicators inform users when API requests are being processed.

The university API may occasionally respond slowly or become temporarily unavailable. The application handles such failures by displaying appropriate feedback.

## 11. Version Control

Git is used to track project development.

Meaningful commits document major development milestones, including:

- API authentication and RecyclerView dashboard
- Details screen and navigation
- Unit testing
- Documentation and final improvements

## 12. Conclusion

This application demonstrates native Android development using Kotlin, REST API integration, MVVM architecture, Hilt dependency injection, RecyclerView and unit testing.

The project was developed to fulfil the requirements of NIT3213 Android Application Development Assignment 2.
