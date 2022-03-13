# Instagram :camera:

![CI for Android](https://github.com/hashimshafiq/Instagram/workflows/CI%20for%20Android/badge.svg)


**Instagram** Project is a mini version of real Instagram app for Android 📱 built with latest Android Development Tools. I have made this as my final project in **MindOrks Professional Android Developer** Course. Check the course [here](https://bootcamp.mindorks.com/). :heart:

## About the App
This app is a tailored version of real Intagram Android app with features like Check Post, Like/DisLike Posts, Profile, Upload Post through Camera and throuh Gallery and also supported by Dark Mode 🌗 and also have the ability of offline capability.

## Built by :hammer:
* Kotlin
* ~~RxAndroid/RxJava~~ Kotlin Coroutines
* Material Theme
* Room
* MVVM
* Dagger 2
* Retrofit
* LiveData
* Glide
* Android Architecture Components
* ~~Kotlin Synthetics~~ viewBindings


**Testing** is already in progress. Some tests are already written others are on their way.

## Project Structure

    com.hashim.instagram        # Root Package
    ├── data                    # For data handling.
    │   ├── local               # Local 
    |   │   ├── db              # Persistence Database. Room database
    |   |   |    ├── dao        # Dao related classes
    |   |   |    └── entity     # Local Database tables
    |   |   |
    |   |   └──prefs            # Shared Prefrences
    |   |   
    │   ├── remote              # Remote Data Handlers and Retrofit API for remote end point     
    |   │   ├── request         # Request Ojects required for Api
    |   |   └── response        # Response Objects for Api
    |   |
    │   ├── repository          # Single source of data.
    |   |
    |   └── model               # Model classes required through out the app
    |
    ├── di                      # Dagger 2 Dependency Injection             
    │   ├── component           # DI Components       
    │   └── module              # DI Modules
    |
    ├── ui                      # Activity/View layer
    │   ├── base                # Base Classes for Activity/Fragment/ViewHolder/Adapter/Dialog
    │   ├── main                # Main Activity & ViewModel   
    |   ├── home                # Home Fragment & ViewModel
    |   │   └── post            # ViewHolder & RecyclerView for Posts
    |   |       └── likeduser   # ViewHolder & RecyclerView for all users who liked specific post                     
    │   ├── login               # Login Activity & ViewModel
    │   ├── signup              # Signup Activity & ViewModel
    │   ├── splash              # Splash Activy & ViewModel
    |   ├── photo               # Photo Fragment & ViewModel
    |   │   └── gallery         # ViewHolder & RecyclerView for Gallery
    │   └── profile             # Profile Fragment & ViewModel     
    |       ├── editprofile     # Edit Profile Activity & ViewModel
    |       ├── userposts       # ViewHolder & RecyclerView for all images which user uploaded
    |       └── settings        # Setting Dialog & ViewModel
    |
    ├── utils                   # Utility Class for Utility Functions
    │   ├── common              # helper functions
    │   ├── display             # related to screen
    │   ├── log                 # related to Logging
    |   ├── network             # related to networking
    |   └── rx                  # related to RxAndroid
    |
    └── InstagramApplication.kt   # Application Class
    
## Architecture
This app uses [***MVVM (Model View View-Model)***](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) architecture.

![](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)


## Bugs? :computer:
If you want to tell me about bug, you're always welcome! :heart:

## Contact
If you need any help, you can connect with me. :heart: :heart: :heart:

Visit:- [hashimshafiq.github.io](https://hashimshafiq.github.io/)
    
## License
```MIT License

Copyright (c) 2020 Muhammad Hashim Shafiq

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

```
