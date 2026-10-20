# 8131650Assignment2

## NIT3213 — Android Application Development
**Assignment 2: API Integration Android Application**

**Student:** Tawfique Elahi  
**Student ID:** s8131650  
**University:** Victoria University Sydney  
**Campus:** Sydney

---

## 1. Project Overview

This project is a native Android application developed using **Kotlin and XML** for NIT3213 Android Application Development Assignment 2.

The application integrates with the university-provided REST API to authenticate students, retrieve dynamically assigned data, and display detailed information.

It follows the **Model-View-ViewModel (MVVM)** architectural pattern and uses **Hilt** for dependency injection, **Retrofit** for networking, and **RecyclerView** to present data efficiently.

### Main Features

- Student authentication through the university REST API
- Login input validation and authentication error handling
- Dashboard displaying dynamic API data using RecyclerView
- Data retrieval using the authentication `keypass`
- Details screen displaying complete information about a selected item
- Loading indicators and network error feedback
- Retry functionality for unsuccessful Dashboard requests
- Navigation between Login, Dashboard and Details screens
- Unit testing of ViewModel functionality using a fake Repository
- Consistent user interface using XML and Material Design components

---

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
| RecyclerView | Displaying dynamic Dashboard data |
| Material Components | User interface components |
| JUnit | Local unit testing |
| Git and GitHub | Version control and project submission |

---

## 3. Application Screens

The application contains three main screens.

### 3.1 Login Screen

The Login screen allows students to enter their university student ID and password.

- The student ID is entered without the leading `s`.
- The password is the student's first name, using the capitalisation required by the university API.
- The application validates the input before making the authentication request.
- A loading indicator appears while authentication is in progress.
- Appropriate error messages are displayed when authentication fails.

After successful authentication, the application receives a `keypass` and opens the Dashboard.

### 3.2 Dashboard Screen

The Dashboard retrieves data using the `keypass` returned by the authentication API.

- API data is displayed dynamically using RecyclerView.
- Each card displays the available item properties except the full description.
- The screen shows the number of items retrieved.
- Loading and error states provide feedback during data retrieval.
- A retry option allows users to request the data again if loading fails.

Selecting a card opens the Details screen.

### 3.3 Details Screen

The Details screen displays the selected item's complete information.

- Displays the selected item's name or title.
- Presents the properties returned by the API.
- Displays the full description separately for readability.
- Provides a Back to Dashboard button for navigation.

The content is based on the selected API response rather than hardcoded values.

---

## 4. API Integration

The application communicates with the university-provided REST API using Retrofit.

**Base URL:**

`https://nit3213apinew.onrender.com/`

### 4.1 Authentication

**HTTP Method:** `POST`  
**Endpoint:** `/sydney/auth`

The authentication endpoint accepts a student ID and password.

**Example Request:**

```json
{
  "username": "YourStudentID",
  "password": "YourFirstName"
}
```

**Example Successful Response:**

```json
{
  "keypass": "topicName"
}
```

The `keypass` is retrieved dynamically from the authentication response.

It is not hardcoded into the application.

### 4.2 Dashboard Data Retrieval

**HTTP Method:** `GET`  
**Endpoint:** `/dashboard/{keypass}`

The application sends the authentication `keypass` to retrieve the assigned dataset.

**Example Response Structure:**

```json
{
  "entities": [
    {
      "name": "Example Item",
      "property": "Example Value",
      "description": "Detailed item description"
    }
  ],
  "entityTotal": 1
}
```

These examples illustrate the API structure. Actual response values and item properties depend on the dataset assigned by the university API.

The Dashboard response contains:

- **entities:** A collection of items with dynamically returned properties.
- **entityTotal:** The total number of items in the response.

The application supports different item properties without requiring a hardcoded dataset.

---

## 5. Application Architecture

The application follows the **MVVM (Model-View-ViewModel)** architecture.

This approach separates user interface responsibilities, application logic and data access to improve readability, maintainability and testability.

### 5.1 UI Layer

Activities and XML layouts are responsible for displaying information and handling user interactions.

The three main Activities are:

- `MainActivity.kt` — Login screen and authentication interaction
- `DashboardActivity.kt` — Dashboard display and item selection
- `DetailsActivity.kt` — Selected item information and navigation

### 5.2 ViewModel Layer

ViewModels handle business logic and UI state management.

- `LoginViewModel.kt` manages login validation, loading, success and error states.
- `DashboardViewModel.kt` manages Dashboard data retrieval, loading, success and error states.

**StateFlow** is used to communicate UI states to the Activities.

### 5.3 Repository Layer

The Repository provides an abstraction between the ViewModels and the network layer.

This allows data operations to remain separate from UI logic and makes it possible to test ViewModels using a fake Repository.

### 5.4 Network Layer

Retrofit is responsible for HTTP communication with the university API.

Gson converts JSON responses into application data models, while Kotlin Coroutines support asynchronous network operations.

### 5.5 Data Flow

The application follows this flow:

**Activity → ViewModel → Repository → Retrofit → University REST API**

The API response returns through the Repository and ViewModel, which update the UI state and display the information.

---

## 6. Hilt Dependency Injection

The project uses **Hilt** to manage dependencies and reduce manual object creation.

Important annotations include:

- `@HiltAndroidApp` — Initialises Hilt for the application.
- `@AndroidEntryPoint` — Enables dependency injection in Activities.
- `@HiltViewModel` — Supports Hilt-managed ViewModels.
- `@Inject` — Declares constructor dependencies.
- `@Module` — Defines Hilt modules.
- `@Provides` — Specifies how dependencies are created.
- `@Binds` — Binds an interface to its implementation.

### Dependency Injection Modules

**NetworkModule.kt**

Provides networking dependencies, including Retrofit and ApiService.

**RepositoryModule.kt**

Binds the AppRepository interface to its implementation.

Hilt improves the separation of concerns and supports easier maintenance and unit testing.

---

## 7. RecyclerView Implementation

The Dashboard uses RecyclerView to display the collection retrieved from the API.

The main components include:

| File | Responsibility |
|---|---|
| `activity_dashboard.xml` | Defines the Dashboard layout and RecyclerView |
| `DashboardActivity.kt` | Configures RecyclerView and observes Dashboard states |
| `EntityAdapter.kt` | Binds API data to individual list items |
| `item_entity.xml` | Defines the layout of each item card |

RecyclerView efficiently displays multiple items by reusing item views.

The Adapter supports dynamically returned properties and handles item selection.

When an item is selected, its data is passed to DetailsActivity for display.

---

## 8. System Requirements

To build and run the application, the following are required:

- Android Studio
- Android SDK
- Minimum supported Android API level: 26
- Internet connection
- Android emulator or compatible physical Android device
- Valid student credentials accepted by the university API

---

## 9. Installation and Setup

### Step 1: Clone the Repository

Clone the project using Git:

```bash
git clone https://github.com/tawfiqueelahi/8131650Assignment2.git
```

Alternatively, download the project as a ZIP file from GitHub and extract it.

### Step 2: Open in Android Studio

1. Launch Android Studio.
2. Select **Open**.
3. Choose the `8131650Assignment2` project folder.
4. Allow Gradle to sync and download the required dependencies.

### Step 3: Run the Application

1. Select an Android emulator or connect a compatible Android device.
2. Select the `app` run configuration.
3. Click **Run**.
4. Wait for the application to build and launch.

No additional API key configuration is required.

---

## 10. How to Use the Application

1. Launch the application.
2. Enter the student ID without the leading `s`.
3. Enter the student's first name as the password, using the exact capitalisation required by the API.
4. Press **Sign In**.
5. Wait for authentication to complete.
6. Browse the returned items on the Dashboard.
7. Select an item to view its complete information.
8. Use **Back to Dashboard** to return to the list.

An active internet connection is required for authentication and data retrieval.

---

## 11. Unit Testing

The project includes **seven custom local ViewModel tests**, plus the default Android Studio example unit test.

The tests use a fake Repository to simulate API responses and failures without making live network requests.

### Test Scenarios

The custom tests cover:

1. Empty student ID validation
2. Empty password validation
3. Successful login response
4. Login network failure
5. Successful Dashboard data retrieval
6. Missing keypass handling
7. Dashboard network failure

A previous test run completed **eight local unit tests with zero failures**.

### Running Unit Tests

Open the project Terminal in Android Studio.

**Windows:**

```powershell
.\gradlew.bat testDebugUnitTest
```

**macOS / Linux:**

```bash
./gradlew testDebugUnitTest
```

### Running a Clean Build and Unit Tests

**Windows:**

```powershell
.\gradlew.bat clean testDebugUnitTest assembleDebug
```

**macOS / Linux:**

```bash
./gradlew clean testDebugUnitTest assembleDebug
```

### Viewing Test Results

Test results can be viewed directly in Android Studio or through the generated HTML reports.

**Report location:**

`app/build/reports/tests/testDebugUnitTest/`

---

## 12. Error Handling

The application includes error handling for authentication and Dashboard data retrieval.

The application provides feedback for:

- Empty or missing login input
- Invalid authentication credentials
- Network connection failures
- Failed API requests
- Missing authentication keypass
- Dashboard data retrieval failures

Loading indicators show when requests are being processed.

The Dashboard also includes a retry option when data retrieval fails.

As the application depends on a university-hosted API, response times and availability may vary. Network failures are handled by displaying appropriate feedback to the user.

---

## 13. Git and Version Control

Git is used to track project development and maintain version history.

The repository includes the application source code, XML layouts, Gradle configuration, dependency injection modules, unit tests and documentation.

Development work includes:

- Project configuration
- Authentication and Login implementation
- Dashboard API integration
- RecyclerView and Adapter implementation
- Details screen and navigation
- MVVM architecture and Hilt dependency injection
- Unit testing and error handling
- User interface refinements
- README documentation

**GitHub Repository:**

https://github.com/tawfiqueelahi/8131650Assignment2

---

## 14. Conclusion

This project demonstrates native Android application development using Kotlin, XML, REST API integration, MVVM architecture, Hilt dependency injection, Retrofit, RecyclerView, Kotlin Coroutines, StateFlow and unit testing.

The application implements the three required screens — **Login, Dashboard and Details** — and retrieves information dynamically from the university-provided API.

The project was developed to fulfil the requirements of **NIT3213 Android Application Development Assignment 2** at Victoria University Sydney.