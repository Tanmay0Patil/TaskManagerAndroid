# Android Task Manager

## What is a Task Manager in Android?

A **Task Manager** is a tool or feature that helps users monitor and manage the running processes, apps, and system resources such as CPU, memory, and battery usage. It provides insights into the active tasks, background processes, memory consumption, and the overall performance of the system. The Task Manager allows users to:

- View running apps and tasks.
- Switch between tasks easily.
- Force stop unresponsive apps.
- Monitor memory and CPU usage in real time.
- Track battery consumption for each app.
- and many more.

This functionality is useful for optimizing device performance, freeing up system resources, and improving battery life.

---

## What We Have Implemented

In this project, we have implemented a basic **Android Task Manager** that allows users to:

- View a list of currently recently used apps .
- Monitor real-time memory usage (Total, Used, Free RAM).
- Display a real-time chart of memory usage over time.
- Force stop apps (with restrictions to prevent killing system apps).
- Display usage stats of apps

The app provides a simple interface to interact with the system’s tasks and processes and helps monitor some resources effectively.

--- 

### Features Implemented:
1. **Real-Time Memory Usage**: Displays memory consumption (used, free, total) in real-time.
2. **Task List**: Displays a list of recently used apps with options to force stop them.
3. **Memory Chart**: A line chart showing memory usage over time.
4. **App Management**: Force stop apps (with restrictions on system apps and your own app).

--- 

## Limitations and Problems Faced

1. **System App Restrictions**: Unable to kill system apps or apps with special permissions due to Android's security model.
2. **Limited Access to System Resources**: Cannot monitor kernel-level resources or deeply manage background processes without root access.
3. **Real-Time Memory Usage**: Real-time monitoring can be resource-intensive, affecting performance on lower-end devices.
4. **Device Compatibility**: May not work as expected on older Android versions or specific devices due to API limitations.
5. **Permissions and Security Restrictions**: Limited app control due to Android's strict permission system and security features.
6. **Performance Issues**: Continuous real-time monitoring may cause performance degradation, especially with high usage or limited hardware.
7. **Inconsistent App States**: Memory and app states may change rapidly, causing discrepancies in displayed data.
8. **Limited Customization**: Lacks advanced customization options for users in the current version.


These limitations provide insights into areas that require improvement and further investigation in future updates.

---


## Minimum Requirements

To run this app, the following minimum requirements must be met:

- **Android Studio**: Version 4.0 or higher.
- **SDK**: Android API Level 21 (Lollipop) or higher.
- **Device/Emulator**: A physical Android device or an emulator running Android 5.0 (Lollipop) or higher.
- **Dependencies**: MPAndroidChart (for displaying the memory chart) and necessary Android SDK dependencies.

---

## How to Run the App

1. Clone the Repository

2. Open in Android Studio, select Open an Existing Project, navigate to this cloned repo directory and select this project

3. Android Studio will automatically sync and download the required dependencies once you open the project. Make sure that your internet connection is active.

4. Once dependencies are resolved, click on Build > Make Project to ensure everything is set up properly.

5. Connect your Android device via USB or start an emulator.  

6. Click on the Run button (green play icon) in Android Studio.  

7. Choose the target device and the app will be installed on your device/emulator, And see for yourself. 