# Dev Push Notification

[![Release](https://jitpack.io/v/DeveshRx/Dev-Push-Notification.svg)](https://jitpack.io/#DeveshRx/Dev-Push-Notification)

A Front UI & Backend base for push notification feature in Android App.

## Features

- Simple UI for Notification Stack
- Fully Customizable
- Supports FCM (Firebase Cloud Messaging)

## How to Use ?

- Setup Firebase Cloud Messaging (FCM) according to these instructions
  [https://firebase.google.com/docs/cloud-messaging/android/client](https://firebase.google.com/docs/cloud-messaging/android/client)

-  Add the JitPack repository to your project build file
   ```
   allprojects {
      repositories {
      maven { url 'https://jitpack.io' }
      }
    }

- Add the dependency
  ```
  dependencies {
	        implementation 'com.github.DeveshRx:Dev-Push-Notification:+'
	}

- Create a `Service` named `MyFirebaseMessagingService.java` and extend it with `FirebaseMessagingService`.

- Create `onMessageReceived` function and insert following code in it:
  
  ```
  NotiDataManager notiDataManager=new NotiDataManager(this);
  notiDataManager.NotificationCapture(remoteMessage);

- Subscribe to Topic in FCM with following code:
  ```
  void SubscribeToTopic(){
      NotiDataManager notiDataManager=new NotiDataManager(this);
      notiDataManager.NotificationSubscribe("general");
      // For Unsubscribe
      // notiDataManager.NotificationUnSubscribe("general");
      }

- Start `NotificationActivity` whenever user want to view notifications
  ```
  Intent intent=new Intent(this, NotificationActivity.class);
  startActivity(intent);
