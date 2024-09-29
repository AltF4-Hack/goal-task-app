const express = require("express");
const app = express();
require("dotenv").config();

const admin = require("firebase-admin");
const serviceAccount = require("./serviceAccountKey.json"); // Add the correct path to your JSON file

const {
  getFirestore,
  Timestamp,
  FieldValue,
  Filter,
} = require("firebase-admin/firestore");

app.use(express.json()); //

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

const db = getFirestore(); // Use Firestore

app.get("/", (req, res) => {
  res.json({
    message: "Welcome to our backend Server hosted on Eric's laptop hehe.",
  });
});

app.post("/api/login", async (req, res) => {
  const { email, hashedPassword } = req.body;
  if (!email || !hashedPassword) {
    return res.status(400).json({ error: "Missing email or password." });
  }
  try {
    const usersRef = db.collection("users");
    const querySnapshot = await usersRef.where("email", "==", email).get();
    if (querySnapshot.empty) {
      return res.status(400).json({ message: "User does not exist." });
    }

    const userDoc = querySnapshot.docs[0];
    const userData = userDoc.data();

    if (hashedPassword != userData.password) {
      return res.status(401).json({ message: "Invalid password." });
    }
    res.status(200).json({ message: "Login successful", userId: userDoc.id });
  } catch (error) {
    console.error("Error logging user:", error);
    res.status(500).json({ error: "An error occurred while logging user" });
  }
});

app.get("/api/getUserById/:userId", async (req, res) => {
  try {
    const userId = req.params.userId;
    const userDoc = await db.collection("users").doc(userId).get();
    if (!userDoc.exists) {
      return res.status(404).json({ error: "User not found" });
    }

    const userData = userDoc.data();
    console.log(userData);
    res.json(userData);
  } catch (error) {
    console.error("Error fetching user:", error);
    res
      .status(500)
      .json({ error: "An error occurred while fetching user data" });
  }
});

app.post("/api/addUser", async (req, res) => {
  const { userId, firstName, lastName, email, hashedPassword } = req.body;
  try {
    // Ensure required fields are present
    if (!firstName || !lastName || !email || !userId || !hashedPassword) {
      return res.status(400).json({ error: "Missing required fields" });
    }

    // Check for duplicate email in Firestore
    const usersRef = db.collection("users");
    const querySnapshot = await usersRef.where("email", "==", email).get();

    if (!querySnapshot.empty) {
      // Email already exists, return an error response
      return res.status(400).json({ message: "Email already exists" });
    }

    // Add the user data to Firestore using the provided userId
    const docRef = db.collection("users").doc(userId);
    await docRef.set({
      firstName,
      lastName,
      email,
      hashedPassword,
      goal: null,
    });

    // Return a success response
    res.status(201).json({ message: "User added successfully" });
  } catch (error) {
    console.error("Error adding user:", error);
    res.status(500).json({ error: "An error occurred" });
  }
});

app.post("/api/updateUser", async (req, res) => {
  const user = req.body;
  if (!user || !user.userId) {
    return res.status(400).json({ error: "User data or userId is missing" });
  }
  try {
    // Reference to the user document in Firestore
    const docRef = db.collection("users").doc(user.userId);

    // Check if the document exists
    const docSnapshot = await docRef.get();
    if (!docSnapshot.exists) {
      return res.status(404).json({ error: "User not found" });
    }

    await docRef.update({
      ...(user.firstName && { firstName: user.firstName }),
      ...(user.lastName && { lastName: user.lastName }),
      ...(user.email && { email: user.email }),
      ...(user.password && { password: user.password }),
      ...(user.goal !== undefined && { goal: user.goal }), // Allow goal to be updated to `null`
    });
  } catch (error) {
    console.error("Error updating data:", error);
    res.status(500).json({ error: "An error occurred" });
  }
});

const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});
