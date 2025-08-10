# ZoExpenseTracker 📱💰

A modern Android expense tracking application built with Jetpack Compose, Room database, and Material Design 3. Track your daily expenses, categorize spending, generate detailed reports, and export data in PDF/CSV formats.

## App Overview

ZoExpenseTracker is a comprehensive personal finance management app that helps users monitor their spending habits through an intuitive interface. The app features real-time expense tracking, smart categorization, detailed analytics, and seamless data export capabilities. Built with modern Android development practices, it provides a smooth user experience while maintaining data privacy and security.

## AI Usage Summary

This project was developed using AI assistance tools to streamline the development process and ensure best practices. ChatGPT was used for initial project structure planning, Jetpack Compose UI component design, and database schema optimization. GitHub Copilot assisted with code completion, error handling patterns, and Android-specific implementations. The AI tools helped accelerate development while maintaining code quality and following Android development guidelines.

## Index 📑

1. **Key Features Implemented** – Overview of core app functionalities, UI, data handling, and technical stack.
2. **How to Use the App** – Step-by-step guide for Home Screen, Adding Expenses, and Reports.
3. **Screenshots 📸** – Visual preview of main screens.
4. **APK Download 📥** – Direct link to the app’s APK file.
5. **Prerequisites & Build Instructions** – Requirements and steps to run the project locally.
6. **Dependencies** – Libraries and frameworks used in the project.
7. **Architecture 🏗️** – MVVM with Clean Architecture layers explained.
8. **Contributing 🤝** – How to contribute to the project.
9. **License 📄** – Licensing information.
10. **Contact 📧** – Developer details.
11. **Zobaze Assignment Details – Prompt Details (ChatGPT)** – Complete breakdown of assignment requirements and expectations.
12. **Cursor Prompt** – Detailed development prompts and change requests used during the build.

## Key Features Implemented ✅

### Core Functionality
- **Expense Management**: Add, edit, and delete expenses with detailed information
- **Smart Categorization**: Pre-defined expense categories with custom support
- **Date-based Tracking**: Filter expenses by date ranges (Today, Last 7 days, Last 30 days, etc.)
- **Real-time Calculations**: Instant total amount and count updates

### Data Management
- **Local Database**: Room database with SQLite for offline data storage
- **Data Persistence**: Automatic data saving and retrieval
- **Search & Filter**: Find expenses by category, date, or amount
- **Data Export**: Generate PDF and CSV reports for external use

### User Interface
- **Material Design 3**: Modern, intuitive UI following Google's design guidelines
- **Jetpack Compose**: Declarative UI framework for smooth animations
- **Dark/Light Theme**: Adaptive theming system
- **Responsive Design**: Optimized for various screen sizes

### Reporting & Analytics
- **Visual Reports**: Category breakdown charts and daily spending trends
- **Period Selection**: Customizable report timeframes
- **Export Options**: PDF generation and CSV export
- **Share Functionality**: Easy sharing of reports via other apps

### Technical Features
- **MVVM Architecture**: Clean separation of concerns
- **Dependency Injection**: Hilt for efficient dependency management
- **Coroutines**: Asynchronous operations for smooth performance
- **Navigation**: Type-safe navigation with Compose Navigation

##  How to Use the App

### 🏠 Home Screen
View all your recorded expenses.

Sort expenses in two ways:
- **By Time** – most recent first.
- **By Category** – grouped by category.

Change the displayed date:
- Tap the calendar icon.
- Or tap the today/date label on the home screen.
- Select any date to view expenses for that day.

See the total amount spent for the selected date.

### ➕ Adding Expenses
- Tap the + (plus) button to open the Add Expense screen.
- Fill in expense details:
  - Title
  - Amount
  - Category
  - Notes (optional)
- **Important**: You can only add expenses for today's date — past and future dates are not allowed.

###  Reports
- Navigate to the Reports section to:
  - Generate expense reports.
  - Download reports (saved in your device's Downloads folder).
  - Share reports directly with others.

### 📂 File Storage
- Downloaded reports are saved to:
  ```
  /storage/emulated/0/Download/
  ```
  (visible in your device's Downloads folder).

## Screenshots 📸

To add screenshots to your README:

1. **Take screenshots** of your app's main screens (Home, Add Expense, Reports, etc.)
2. **Save them** in a `screenshots/` folder in your project root
3. **Add them to README** using this format:

```markdown
## Screenshots 📸

| Home Screen | Add Expense | Reports |
|-------------|-------------|---------|
```
<div style="display: flex; justify-content: center; flex-wrap: wrap; gap: 10px;">
  <img src="app/src/main/res/drawable/home_ss.png" alt="Home" height="300">
  <img src="app/src/main/res/drawable/add_exp_ss.png" alt="Add Expense" height="300">
  <img src="app/src/main/res/drawable/report_ss.png" alt="Reports" height="300">
  <img src="app/src/main/res/drawable/date_ss.png" alt="Date Selection" height="300">
</div>


## APK Download 📥

[Download APK](https://drive.google.com/file/d/1PE-e6hNWGBKb3oqRZZIKE62LxGI0I7tc/view?usp=sharing)

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 24+ (API level 24)
- Kotlin 1.9.0+
- JDK 11

### Build Instructions
1. Clone the repository
```bash
git clone https://github.com/malik0011/ExpenseTracker
```

2. Open in Android Studio
3. Sync Gradle files
4. Build and run on device/emulator

### Dependencies
- **Jetpack Compose**: Modern UI toolkit
- **Room**: Local database
- **Hilt**: Dependency injection
- **Navigation Compose**: Navigation framework
- **Material 3**: Design system

## Architecture 🏗️

The app follows MVVM (Model-View-ViewModel) architecture with Clean Architecture principles:

- **Presentation Layer**: Compose UI components and ViewModels
- **Domain Layer**: Use cases and business logic
- **Data Layer**: Repository pattern with Room database
- **DI Layer**: Hilt for dependency management

## Contributing 🤝

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

## License 📄

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact 📧

- **Developer**: Ayan Malik
- **Email**: ayanmalik.8334@gmail.com
- **GitHub**: [@malik0011]

---

## Zobaze Assignment Details – Prompt Details(ChatGPT)

- Add the document also. Make a to-do list of what I have to do. Analyze the whole assignment properly.
- ---------- Let’s start step by step with explanations — why we need this, all the basic things: HLD, what screens we’ll have, architecture, file structure, MVVM pattern. Make things clearly understandable. Include utils classes we might need, mappers, and make sure testing ability can be an advantage here. I need it to be logical. Add some manual test cases to double-check your steps so I can test.
- Step 1 — The UI should be minimal and user-friendly, like every important app (based on the use case), because it will be used by all age groups. Functional app, good colors, toast messages, errors, and other UI states. Make sure expenses show credit in green, debit in red, and maybe money in a gold kind of color.
- Also think about fonts — for this kind of app, what type of font will be better? Maybe not for the whole app, but for specific text? Suggest the best choice.
- Now let’s start — we have done the basic setup. Let’s start with the data layer: DB, modules, application DI setup, repositories, use cases, flows, ViewModel. Follow our checklist from earlier. Where needed, add extra context and think like a pro engineer — what else could be added that will improve the app beyond my advice? Will this help? If yes, include it.
- Use Dagger or Hilt for all dependency injections. Got it? Make sure to update the code based on that. Will it help us, or is there a better alternative?
- Let’s start with the UI design of the home activity — requirements are already mentioned in the doc. The goal is already there.
- How to do it — stick to our custom approach you already know: a good product that any user can use, just like a micro-edit world — good but simple to use. Color codes are already defined; add some creativity from any old product related to this.
- Make sure UI is stateless and flows in UDF. Only data will pass, and callbacks will handle actions.
- The design should follow all design principles for this domain and follow Android Material Design guidelines — like app bar or other components if needed.

---

## Cursor Prompt

- First, get the context of the app and understand the whole project.
- Let me give you the full assignment context — I have most of the basic setup done.
- Let’s start with what’s not done yet. Also make sure to follow good design/UI/UX practices for this kind of app — money-related color tones should match the app’s theme.
- Can you add support for viewing previous dates via calendar or filter on the home screen without breaking anything? Maybe add it on top — load a date picker, and after selecting a date, reload the whole UI with the result (check the date format we’re using so it’s compatible). Also update the selected date in the UI. Add a “Today” chip anywhere, and show it only when we are on any other date.
- Thanks, it’s working now. Just wanted to mention the bar graph UI is too big — I have to scroll more than 15 times to reach the bottom. Can you fix that?
- Working. Can you now do it for the download report as PDF and the share thing, as mentioned?
- Now the PDF download toast is showing, but the file is not visible. Can you do something like using DownloadManager or similar so it uses a notification that I can click to open the PDF? It should be visible in my device’s Downloads folder. Got it?

---
⭐ **Star this repository if you find it helpful!**