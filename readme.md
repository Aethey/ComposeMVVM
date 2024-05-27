# Jetpack Compose Simple GitHub App

## Description

This application is a simple GitHub client built using Jetpack Compose and follows the MVVM
architecture pattern. It's designed to showcase how to use Compose in real-world applications.

## Architecture

- MVVM (Model-View-ViewModel)

## Feature

- list user.
- search user.
- list user repositories without fork.
- save search history.

## Incomplete

- unit test.
- Refine the scrolling behavior of the LargeTopAppBar and LazyColumn in the user details page.
- Configure the GitHub Actions job to trigger when merging into the master branch.

## Third-Party Libraries

This project uses a number of third-party libraries:

### Google and Jetpack Compose

- Android Gradle Plugin (AGP): `8.4.0`
- Compose Compiler: `2023.08.00`
- Activity Compose: `1.9.0`
- Compose Material: `1.3.1`
- Compose WebView: `0.33.6`
- Navigation Compose: `2.7.7`
- Lifecycle Runtime KTX: `2.8.0`
- Core KTX: `1.13.1`
- Material Design: `1.6.7`
- Room Runtime: `2.6.1`

### Other Libraries

- Kotlin: `1.9.0`
- Coil Compose for image loading: `2.6.0`
- Retrofit for networking: `2.9.0`
- Jackson for JSON parsing:
    - Converter Jackson: `2.11.0`
    - Jackson Annotations: `2.17.1`
- Logging Interceptor: `4.9.3`
- Testing libraries:
    - JUnit: `4.13.2`
    - JUnit Extensions for AndroidX: `1.1.5`
    - Espresso Core: `3.5.1`
    - Mockito Kotlin: `4.1.0`
    - Kotlinx Coroutines Test: `1.8.0`
    - Core Testing: `2.2.0`
- KSP (Kotlin Symbol Processing): `1.9.10-1.0.13`

## External Resources

- [Language Color Resource](https://github.com/github-linguist/linguist/blob/master/lib/linguist/languages.yml)

## Screenshots

Screenshots of the application are stored under the `simpleImage` folder at the same level as this
README. Below are the images arranged in a 3x3 grid style:

|                               |                               |                               |
|:-----------------------------:|:-----------------------------:|:-----------------------------:|
| ![Image 1](simpleImage/1.png) | ![Image 2](simpleImage/2.png) | ![Image 3](simpleImage/3.png) |
| ![Image 4](simpleImage/4.png) | ![Image 5](simpleImage/5.png) | ![Image 6](simpleImage/6.png) |
| ![Image 7](simpleImage/7.png) | ![Image 8](simpleImage/8.png) | ![Image 9](simpleImage/9.png) |

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

### MIT License


