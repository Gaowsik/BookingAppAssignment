# BookingAppAssignment

BookingAppAssignment is an Android application built using **MVVM** architecture and modern Android development practices. The app allows users to see their current location on Google Maps, view driver locations, and book a service through a customer information screen.

---

## Features

- **Google Maps Integration**:  
  - Displays the user's current location with a Blue marker.
  - Optionally shows drivers' locations on the map with Red marker.
  
- **Booking Flow**:  
  - The "Book Now" button on the map screen allows the user to navigate to a second screen for collecting customer information only if at least one driver is within 1 km.
  - The second screen is fully customizable, letting you include any form fields based on your design preferences.

- **Validation and User Input**:  
  - Proper handling of user input and validation on the customer information screen.

---

## Architecture

- **MVVM (Model-View-ViewModel)** for a clean separation of concerns.
- Uses modern Android components like **LiveData**, **ViewModel**, and **Data Binding**.

---

## Getting Started

1. Clone the repository:
    ```bash
    git clone <repository-url>
    ```
2. Open the project in **Android Studio**.
3. Add your **Google Maps API key** in the `local.properties` file:
    ```properties
    MAPS_API_KEY="YOUR_API_KEY_HERE"
    ```
4. Update driver locations in `DriverDataSource` if needed.
5. Build and run the app on an Android device or emulator.
