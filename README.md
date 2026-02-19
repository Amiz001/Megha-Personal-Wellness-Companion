# 🌿 Megha – Personal Wellness Companion

**Megha** is a lightweight Android application that helps users build healthy habits, track their daily moods, and stay hydrated with smart reminders.  
The app stores all user progress locally using **SharedPreferences**, ensuring a fast, smooth, and fully offline experience.

## Features I Worked On
- **Mood Tracking**: Log daily moods and monitor emotional patterns over time.
- **Hydration Reminder**: Set water intake reminders to maintain daily hydration goals.
- **Habit Builder**: Create and track healthy habits with simple daily progress updates.
- **Local Data Persistence**: Store and retrieve user data using SharedPreferences for offline functionality.
- **Clean UI**: Simple and user-friendly interface built with XML layouts.

> Note: This app works completely offline and does not use cloud storage.

## Tech Stack
- **Language**: Kotlin  
- **UI Design**: XML  
- **IDE**: Android Studio  
- **Data Storage**: SharedPreferences  
- **Build System**: Gradle (KTS)  

## Highlights
- Fully offline wellness tracking application.
- Lightweight and fast performance with local storage.
- Beginner-friendly Android architecture and clean project structure.
- Demonstrates core Android concepts: activities, layouts, lifecycle, and state persistence.

## Project Structure
```
Megha-Personal-Wellness-Companion/
│
├── app/                     # Main Android source code
├── gradle/                  # Gradle configuration
├── .idea/                   # Android Studio settings
├── build.gradle.kts         # Project-level build file
├── settings.gradle.kts      # Project settings
├── gradle.properties        # Gradle properties
├── gradlew / gradlew.bat    # Gradle wrapper
└── .gitignore               # Ignored files
```

## Screenshots
<div align="center">
<img width="30%" alt="Launch screen" src="https://github.com/user-attachments/assets/e5b95973-2fd4-4ee4-b423-3d648e04335f" />
<img width="30%" alt="Habits" src="https://github.com/user-attachments/assets/c95815b3-0576-4555-8b0b-d451c910eccb" />
<img width="30%" alt="Add habit" src="https://github.com/user-attachments/assets/1d8afef7-c1eb-4b38-85de-0a4e074c7e48" />
<img width="30%" alt="Update habit" src="https://github.com/user-attachments/assets/06f27abc-a896-4e8f-a735-60527137cff6" />
<img width="30%" alt="Mood" src="https://github.com/user-attachments/assets/34a7414e-0497-40d8-b168-a075da321492" />
<img width="30%" alt="Log Mood" src="https://github.com/user-attachments/assets/ac9501fb-f395-4f9b-8089-ebec51a1fc01" />
<img width="30%" alt="Mood chart" src="https://github.com/user-attachments/assets/bd1e4dc1-ee15-4b40-bcb4-1f3012cc972b" />
<img width="30%" alt="Settings" src="https://github.com/user-attachments/assets/dbeeb870-51d0-4936-b80c-b9108d9c9d9e" />
<img width="30%" alt="Hydration reminder" src="https://github.com/user-attachments/assets/6643e1c4-4d73-4bf0-a61c-61e1974ff328" />
</div>


## Installation
1. Clone the repository:
   ```bash
   git clone <your-repo-url>
   ```
2. Open the project in **Android Studio**  
3. Sync Gradle files  
4. Run the app on an emulator or physical Android device  

## Future Improvements
- Firebase authentication and cloud sync  
- Mood and hydration analytics with charts  
- Dark mode support  
- Room Database integration  
- Push notification enhancements  

## Author
Developed as an Android wellness project using Kotlin and modern Android development practices.
