# Instagram :camera:

![CI for Android](https://github.com/hashimshafiq/Instagram/workflows/CI%20for%20Android/badge.svg)


**Instagram** Project is a mini version of real Instagram app for Android 📱 built with latest Android Development Tools. I have made this as my final project in **MindOrks Professional Android Developer** Course. Check the course [here](https://bootcamp.mindorks.com/). :heart:

## About the App
This app is a tailored version of real Intagram Android app with features like Check Post, Like/DisLike Posts, Profile, Upload Post through Camera and throuh Gallery and also supported by Dark Mode 🌗 and also have the ability of offline capability.

## Built by :hammer:
* Kotlin
* RxAndroid/RxJava
* Material Theme
* Room
* MVVM
* Dagger 2
* Retrofit
* LiveData
* Glide
* Android Architecture Components


**Testing** is already in progress. Some tests are already written others are on their way.

## Project Structure

com.hashim.instagram    # Root Package
    .
    ├── data                # For data handling.
    │   ├── local           # Local 
    |   │   ├── db          # Persistence Database. Room (SQLite) database
    |   |   |    ├── dao    # Dao related classes
    |   |   |    ├── entity # Local Database tables
    |   |   |     
    |   |   ├── prefs       # 
                dao         # Data Access Object for Room   
    │   ├── remote          # Remote Data Handlers     
    |   │   ├── api         # Retrofit API for remote end point.
    │   ├── repository      # Single source of data.
    |   |
    |   ├── model
    |
    ├── model               # Model classes
    |
    ├── di                  # Dependency Injection             
    │   ├── builder         # Activity Builder
    │   ├── component       # DI Components       
    │   └── module          # DI Modules
    |
    ├── ui                  # Activity/View layer
    │   ├── base            # Base View
    │   ├── main            # Main Screen Activity & ViewModel
    |   │   ├── adapter     # Adapter for RecyclerView
    |   │   └── viewmodel   # ViewHolder for RecyclerView   
    │   └── details         # Detail Screen Activity and ViewModel
    |
    └── utils               # Utility Classes / Kotlin extensions
