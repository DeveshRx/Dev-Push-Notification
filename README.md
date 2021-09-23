# Dev Push Notification

A Front & Backend UI base for push notification feature in Android App.

## Features

- Simple UI fot Notification Stack
- Fully Customizable
- Supports FCM (Firebase Cloud Messaging)

## How to Use ?

- Setup Firebase Cloud Messaging (FCM) according to these instructions
  [https://firebase.google.com/docs/cloud-messaging/android/client](https://firebase.google.com/docs/cloud-messaging/android/client)

- git clone this repo & copy `Notifications` module into your Android Project.

- Create a `Service` named `MyFirebaseMessagingService.java` and extend it with `FirebaseMessagingService`.

- Create `onMessageReceived` function and insert following code in it:
  
  ```
  NotiDataManager notiDataManager=new NotiDataManager(this);
  notiDataManager.NotificationCapture(remoteMessage);`

- Subscribe to Topic in FCM with following code:
  ```
  void SubscribeToTopic(){
      NotiDataManager notiDataManager=new NotiDataManager(this);
      notiDataManager.NotificationSubscribe("general");
      // For Unsubscribe
      // notiDataManager.NotificationUnSubscribe("general");
      }```

- Start `NotificationActivity` whenever user want to view notifications
```Intent intent=new Intent(this, NotificationActivity.class);
   startActivity(intent);```


