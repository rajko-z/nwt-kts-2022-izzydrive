importScripts("https://www.gstatic.com/firebasejs/7.16.1/firebase-app.js");
importScripts(
  "https://www.gstatic.com/firebasejs/7.16.1/firebase-messaging.js");
// For an optimal experience using Cloud Messaging, also add the Firebase SDK for Analytics.
importScripts(
  "https://www.gstatic.com/firebasejs/7.16.1/firebase-analytics.js");

// The object we pass as an argument is the same object we copied into the environment files
firebase.initializeApp({
  apiKey: "AIzaSyBqqqb6lb6RROvNXPz2Ei3muSp2x8mtf4Q",
  authDomain: "izzydrive-29e02.firebaseapp.com",
  projectId: "izzydrive-29e02",
  storageBucket: "izzydrive-29e02.appspot.com",
  messagingSenderId: "669448010022",
  appId: "1:669448010022:web:53294fe0cf5d12be8469e7"
})


const messaging = firebase.messaging();

messaging.setBackgroundMessageHandler(function(payload) {
  console.log(
    "[firebase-messaging-sw.js] Received background message ",
    payload,
  );
  // Customize notification here
  const notificationTitle = "Background Message Title";
  const notificationOptions = {
    body: "Background Message body.",
    icon: "/itwonders-web-logo.png",
  };

  return self.registration.showNotification(
    notificationTitle,
    notificationOptions,
  );
});
