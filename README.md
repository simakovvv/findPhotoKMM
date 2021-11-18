# findPhotoKMM
pet project for interview

**findPhoto**
kmmSample project for interview

**Application uses folllowing features:**

- Jetpack Compose
- Standart Kotlin-Conpose viewModel library
- Factory template for dependency injection
- Kotlin Coroutines
- Kotlin Multiplatform for Data Access and Data source layers
- Material Design guidelines were used to create the views
- MVI design pattern
- Flickr Api as remote pictures server
- Coil using for downloading and handing images

**Screen Content:**

- Search bar. Allows sending requests to remote server
- App shows loading indicator while the image is loading
- Screen building implemented using composition pattern
- Search request returns 21 photo as a response
- User can interact with app while it’s fetching the photos
- All of the photos has a local cashe to prevent multiple rest request sending
- The photo displayed right after it has been downloaded, without waiting for processing the others

**Not implemented:**

- Images downloading
- Tapping on the image will highlight the cell and the user should be able to save the image
- Unit Tests
