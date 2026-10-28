# Atten - Location-based HTTP Request Android App

## Overview

Atten is an Android application that allows users to configure and send HTTP requests to a specified endpoint with randomized location coordinates. The app includes:

- **Configuration Management**: Set up endpoint URL, HTTP method, user credentials, and response criteria
- **Location Management**: Add and manage multiple locations with precise coordinates
- **Smart Location Randomization**: Automatically randomizes location coordinates (+/- 100 meters) for privacy
- **Request Logging**: Tracks all requests from the past 7 days with full details
- **Response Handling**: Displays success/failure status with raw response data

## Project Structure

```
app/src/main/
├── java/com/example/atten/
│   ├── MainActivity.kt                 # Entry point
│   ├── data/
│   │   ├── database/
│   │   │   └── AttenDatabase.kt        # Room database setup
│   │   ├── dao/
│   │   │   ├── AppConfigDao.kt         # Config data access
│   │   │   ├── LocationDao.kt          # Location data access
│   │   │   ├── RequestLogDao.kt        # Log data access
│   │   │   └── TaskDao.kt              # Task data access
│   │   ├── entity/
│   │   │   ├── AppConfigEntity.kt      # Configuration data model
│   │   │   ├── LocationEntity.kt       # Location data model
│   │   │   ├── RequestLogEntity.kt     # Request log data model
│   │   │   └── TaskEntity.kt           # Task data model
│   │   └── repository/
│   │       ├── ConfigRepository.kt     # Config business logic
│   │       ├── LocationRepository.kt   # Location business logic
│   │       ├── RequestLogRepository.kt # Log business logic
│   │       ├── RequestService.kt       # HTTP request handling
│   ├── network/
│   │   ├── ApiService.kt               # Retrofit API interface
│   │   ├── ApiModels.kt                # API response models
│   │   └── NetworkClient.kt            # Retrofit setup
│   ├── ui/
│   │   ├── Navigation.kt               # Navigation setup
│   │   ├── theme/
│   │   │   └── Theme.kt                # Material3 theme
│   │   ├── screen/
│   │   │   ├── HomeScreen.kt           # Main location map screen
│   │   │   ├── ConfigScreen.kt         # Configuration screen
│   │   │   └── LogScreen.kt            # Request history screen
│   │   └── viewmodel/
│   │       ├── HomeViewModel.kt        # Home screen state management
│   │       ├── ConfigViewModel.kt      # Config screen state management
│   │       └── LogViewModel.kt         # Log screen state management
│   └── util/
│       ├── LocationUtils.kt            # Location randomization logic
│       └── JsonPathUtils.kt            # JSON response parsing
├── res/
│   ├── values/
│   │   ├── strings.xml                 # String resources
│   │   └── themes.xml                  # Theme styles
└── AndroidManifest.xml                 # App manifest with permissions
```

## Key Features

### 1. Configuration Management
- **Endpoint URL**: Set the API endpoint to send requests to
- **HTTP Method**: Choose between GET or POST
- **User Credentials**: Enter userId and userEmail
- **Response Criteria**: Define JSON path and expected value for success/failure detection
- **Persistent Storage**: Configuration is saved to local database

### 2. Location Management
- **Add Locations**: Create multiple named locations with latitude/longitude
- **Manage Locations**: Edit or delete existing locations
- **List View**: View all configured locations with coordinates
- **Location Groups**: Each location is associated with the configuration

### 3. Request Execution
- **Send Requests**: Click any location to send a configured HTTP request
- **Randomized Coordinates**: Actual sent coordinates are randomized within 100 meters
- **Payload Building**: Automatically builds request with:
  - userId
  - userEmail
  - randomized latitude/longitude
  - Additional custom JSON parameters

### 4. Response Handling
- **Status Display**: Shows success/failure status based on criteria
- **Status Code**: Displays HTTP status code
- **Raw Response**: Option to view full response body
- **Error Handling**: Network errors are caught and logged
- **Bottom Sheet**: Results shown in a convenient bottom sheet

### 5. Request Logging
- **7-Day History**: Keeps logs for the past 7 days
- **Detailed Information**:
  - Request timestamp
  - Location name
  - Used coordinates (with randomization applied)
  - HTTP method and URL
  - Request body and response body
  - Status code
  - Success/failure status
- **Expandable Details**: View full request/response in each log entry

## Data Models

### AppConfigEntity
```kotlin
data class AppConfigEntity(
    val id: Int = 1,
    val endpoint: String,
    val httpMethod: String, // GET or POST
    val userId: String,
    val userEmail: String,
    val jsonPayload: String, // JSON string
    val successCriteriaJsonPath: String, // e.g., "response.status"
    val successCriteriaValue: String,
    val failureCriteriaJsonPath: String,
    val failureCriteriaValue: String
)
```

### LocationEntity
```kotlin
data class LocationEntity(
    val id: Int,
    val configId: Int,
    val locationName: String,
    val latitude: Double,
    val longitude: Double
)
```

### RequestLogEntity
```kotlin
data class RequestLogEntity(
    val id: Int,
    val configId: Int,
    val locationId: Int,
    val locationName: String,
    val usedLatitude: Double, // Randomized
    val usedLongitude: Double, // Randomized
    val httpMethod: String,
    val requestUrl: String,
    val requestBody: String,
    val statusCode: Int,
    val responseBody: String,
    val isSuccess: Boolean,
    val errorMessage: String,
    val isNetworkError: Boolean,
    val timestamp: Long
)
```

## Location Randomization

The app uses the Haversine formula to randomize location coordinates:

1. Generates a random bearing (0-360 degrees)
2. Generates a random distance (0-100 meters)
3. Calculates destination point from original coordinates
4. Returns the randomized lat/long for the request
5. Logs the actual used coordinates for audit trail

```kotlin
LocationUtils.getRandomizedLocation(latitude, longitude, 100)
```

## JSON Path Navigation

The app supports JSON path navigation using dot notation:

```
"response.status" → response["status"]
"data.user.email" → data["user"]["email"]
```

Example: If response is:
```json
{
  "response": {
    "status": "success"
  }
}
```

Setting success criteria:
- JSON Path: `response.status`
- Expected Value: `success`

## Network Implementation

- **Retrofit**: HTTP client for API requests
- **Moshi**: JSON serialization/deserialization
- **Dynamic Base URL**: Automatically extracted from endpoint URL
- **Error Handling**: Network errors are caught and logged

## Database

- **Room Database**: Local data persistence
- **Foreign Keys**: Relationships between entities
- **Automatic Migration**: Version 2 with new entities
- **Flow Support**: Real-time data updates

## Permissions

The app requires the following permissions:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

## Usage Flow

1. **First Launch**: User sees "No Configuration" message
2. **Configure**: Click gear icon to configure app
3. **Add Locations**: Add locations in configuration screen
4. **Save**: Save configuration
5. **Home Screen**: Locations appear as cards on home screen
6. **Send Request**: Click "Send Request" on a location card
7. **View Result**: Bottom sheet shows response with option for raw data
8. **View History**: Click log icon to see past 7 days of requests

## Building and Running

### Prerequisites

- **Android SDK 28+** - Minimum API level
- **Java Development Kit (JDK 11+)** - Required for compilation
- **Gradle 8.5.0+** - Build system (included via wrapper)
- **Android Studio** (optional) - IDE for development
- **Gradle Wrapper** - Already included in project

### Environment Setup

#### 1. Set JAVA_HOME (If not already set)

**Windows (PowerShell):**
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-11"
```

**Or create `local.properties` in project root:**
```properties
sdk.dir=C:\Users\<YourUsername>\AppData\Local\Android\Sdk
java.home=C:\Program Files\Java\jdk-11
```

#### 2. Verify Setup
```bash
./gradlew --version
java -version
```

### Build Variants

#### Debug APK (For Testing)

Build without installing:
```bash
./gradlew assembleDebug
```

**Output:** `app/build/outputs/apk/debug/app-debug.apk`

Build and install on connected device/emulator:
```bash
./gradlew installDebug
```

Build and run immediately:
```bash
./gradlew runDebug
```

#### Release APK (For Distribution)

##### Step 1: Create Signing Key (One-time)

```bash
# Generate keystore file
keytool -genkey -v -keystore my-release-key.keystore -keyalg RSA -keysize 2048 -validity 10000 -alias my-key-alias
```

**Prompts:**
- Keystore password: (Create a strong password)
- Key password: (Can be same as keystore password)
- First and last name, organization, etc.

##### Step 2: Configure Signing in app/build.gradle.kts

Add to `android` block:

```kotlin
signingConfigs {
    create("release") {
        storeFile = file("../my-release-key.keystore")
        storePassword = "your-keystore-password"
        keyAlias = "my-key-alias"
        keyPassword = "your-key-password"
    }
}

buildTypes {
    release {
        signingConfig = signingConfigs.getByName("release")
        isMinifyEnabled = true
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
}
```

##### Step 3: Build Release APK

```bash
./gradlew assembleRelease
```

**Output:** `app/build/outputs/apk/release/app-release.apk`

### Installation

#### Install Debug APK

```bash
# Via gradle
./gradlew installDebug

# Via adb (manual)
adb install app/build/outputs/apk/debug/app-debug.apk

# Reinstall (uninstall first)
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

#### Install Release APK

```bash
adb install app/build/outputs/apk/release/app-release.apk
```

#### Uninstall App

```bash
adb uninstall com.example.atten
```

### Run on Device/Emulator

#### Using Gradle

```bash
./gradlew runDebug
```

#### Using Android Studio

1. Press `Shift + F10` (or Cmd + R on Mac)
2. Select target device/emulator
3. Click "Run"

#### Using adb

```bash
# List connected devices
adb devices

# Run specific app
adb shell am start -n com.example.atten/.MainActivity
```

### Build Commands Reference

| Command | Purpose |
|---------|---------|
| `./gradlew build` | Build all variants (debug + release) |
| `./gradlew assembleDebug` | Build debug APK only |
| `./gradlew assembleRelease` | Build release APK only |
| `./gradlew installDebug` | Build and install debug APK |
| `./gradlew runDebug` | Build, install, and run debug APK |
| `./gradlew clean` | Clean build outputs |
| `./gradlew clean build` | Clean and rebuild |
| `./gradlew tasks` | List all available tasks |
| `./gradlew dependencies` | Show dependency tree |
| `./gradlew --version` | Show gradle version |

### Troubleshooting Build Issues

#### Issue: "SDK location not found"

**Solution:**
```powershell
# Create local.properties file in project root
@"
sdk.dir=C:\Users\$env:USERNAME\AppData\Local\Android\Sdk
"@ | Out-File local.properties -Encoding ASCII
```

#### Issue: "JAVA_HOME not set" or "Java not found"

**Solution:**
```bash
# Check Java installation
java -version

# Set JAVA_HOME permanently (Windows)
# Add to Environment Variables or local.properties
java.home=C:\Program Files\Java\jdk-11
```

#### Issue: "No connected devices"

**Solution:**
1. Enable USB debugging on Android device:
   - Settings → Developer Options → USB Debugging
2. Connect device via USB cable
3. Run `adb devices` to verify connection
4. Or launch Android emulator from Android Studio

#### Issue: "Build fails with 'resource not found'"

**Solution:**
```bash
./gradlew clean build
```

#### Issue: Gradle daemon issues

**Solution:**
```bash
./gradlew --stop
./gradlew build --no-daemon
```

#### Issue: Out of memory during build

**Solution:** Create `gradle.properties` in project root:
```properties
org.gradle.jvmargs=-Xmx4096m
org.gradle.daemon=true
```

### Build Output

After successful build, APK files are located at:

```
app/build/outputs/apk/
├── debug/
│   └── app-debug.apk          (Debug version, ~15-20 MB)
└── release/
    └── app-release.apk        (Release version, ~8-12 MB with minify)
```

### App Signing

- **Debug builds**: Auto-signed with default debug key
- **Release builds**: Must be manually signed with your keystore
- **Keystore**: Store securely, required for app updates
- **Never share**: Keep private key file and passwords secure

### Performance Optimization

For faster builds:

```gradle.properties
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.jvmargs=-Xmx2048m
```

### Release Checklist

Before building release APK:

- [ ] Update version code in `app/build.gradle.kts`
- [ ] Update version name (e.g., "1.0.0")
- [ ] Test on multiple devices
- [ ] Verify minification works correctly
- [ ] Check ProGuard rules in `proguard-rules.pro`
- [ ] Create signed keystore
- [ ] Build release APK
- [ ] Test release APK installation

## Dependencies

- **Jetpack Compose**: UI framework
- **Room**: Local database
- **Retrofit**: HTTP client
- **Moshi**: JSON parsing
- **Navigation Compose**: In-app navigation
- **Lifecycle**: ViewModel and LiveData
- **Coroutines**: Async operations
- **Material3**: UI components

## Future Enhancements

- [ ] Google Maps integration for visual location display
- [ ] Multiple configuration profiles
- [ ] Request templates and scheduling
- [ ] Response validation rules
- [ ] Data export/backup
- [ ] Request replay functionality
- [ ] Real-time location tracking
- [ ] Advanced filtering for logs
- [ ] Request analytics and metrics
