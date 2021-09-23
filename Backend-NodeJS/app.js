const express = require("express");

var path = require("path");
var logger = require("morgan");
var cookieParser = require("cookie-parser");
var bodyParser = require("body-parser");
var admin = require("firebase-admin");

const app = express();

require("dotenv").config();

const port = process.env.PORT || 3636;
app.use(express.static("public"));

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
//app.use(express.json());
app.use(cookieParser());

//Firebase Admin
var serviceAccount;

if (process.env.ENV == "dev") {
  serviceAccount = require("./"+ process.env.FIREBASE_DEBUG_JSON);
  console.log("Running in Development Mode");
} else {
  serviceAccount = require("./"+ process.env.FIREBASE_PRODUCTION_JSON);
  console.log("Running in Production Mode");
}

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: process.env.FIREBASE_DATABASE,
  storageBucket: process.env.FIREBASE_STORAGE_BUCKET,
});


app.get("/", (req, res) => {
  res.send("vchat NodeJS Server!");
});

app.post("/SendNotification", (req, res) => {
  var postBody = req.body;

  console.log(postBody);

  var NID = postBody.NID;
  var Title = postBody.Title;
  var ShortSummary = postBody.ShortSummary;
  var LongSummary = postBody.LongSummary;
  var IconURL = postBody.IconURL;
  var ImageURL = postBody.ImageURL;
  var nURL = postBody.nURL;

  var topic = "general";
  //FCM
/*
  android: {
      notification: {
        clickAction: "devesh_message",
      },
    },
  */
  var message = {
    topic: topic,  
    data: {
      type: "notification",
      title: Title,
      short_summary: ShortSummary,
      long_summary: LongSummary,
      icon: IconURL,
      img: ImageURL,
      url: nURL,
      id: NID,
    },
  };
  console.log(message);

  admin
    .messaging()
    .send(message)
    .then((response) => {
      // Response is a message ID string.
      console.log("Successfully sent FCM message:", response);
    })
    .catch((error) => {
      console.log("Error sending FCM message:", error);
    });

  res.send("DONE");
});

app.listen(port, () => {
  console.log(`Dashboard app listening at http://localhost:${port}`);
});

