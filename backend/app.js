const express = require("express");
const app = express();
require("dotenv").config();

const admin = require("firebase-admin");
const serviceAccount = require("./serviceAccountKey.json");

// Google GeminiAPI
const { GoogleGenerativeAI } = require("@google/generative-ai");

const { getFirestore } = require("firebase-admin/firestore");

app.use(express.json());

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

const db = getFirestore(); // Use Firestore

app.get("/", (req, res) => {
  res.json({
    message: "Welcome to our backend Server hosted on Eric's laptop hehe.",
  });
});

app.post("/api/login/:loginInfo", async (req, res) => {
  const loginInfoJSONSTRING = req.params.loginInfo;
  const loginInfo = JSON.parse(loginInfoJSONSTRING);
  const { email, password } = loginInfo;

  if (!email || !password) {
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

    if (password != userData.password) {
      return res.status(401).json({ message: "Invalid password." });
    }
    res.status(200).json(userData);
  } catch (error) {
    console.error("Error logging user:", error);
    res.status(500).json({ error: "An error occurred while logging user" });
  }
});

app.get("/api/getUserByEmail/:email", async (req, res) => {
  try {
    const email = req.params.email;
    const querySnapshot = await db
      .collection("users")
      .where("email", "==", email)
      .get();
    if (querySnapshot.empty) {
      return res.status(400).json({ message: "User does not exist." });
    }
    const userData = querySnapshot.docs[0].data();
    res.status(200).json(userData);
  } catch (error) {
    console.error("Error finding user by email:", error);
    res.status(500).json({ error: "An error occurred while getting user" });
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
  const { json } = req.body;
  console.log(json);
  const userData = JSON.parse(json);
  try {
    // Ensure required fields are present
    if (
      !userData.firstName ||
      !userData.lastName ||
      !userData.email ||
      !userData.id ||
      !userData.password
    ) {
      return res.status(400).json({ error: "Missing required fields" });
    }

    // Check for duplicate email in Firestore
    const usersRef = db.collection("users");
    const querySnapshot = await usersRef
      .where("email", "==", userData.email)
      .get();

    if (!querySnapshot.empty) {
      // Email already exists, return an error response
      return res.status(400).json({ message: "Email already exists" });
    }

    // Add the user data to Firestore using the provided userId
    const docRef = db.collection("users").doc(userData.id);
    await docRef.set({
      ...userData,
      goal: null,
    });

    // Return a success response
    res.status(201).json({ message: "User added successfully" });
  } catch (error) {
    console.error("Error adding user:", error);
    res.status(500).json({ error: "An error occurred" });
  }
});

app.post("/api/updateUser/:userRepresentation", async (req, res) => {
  // user is a JSON formatted string
  // const { user } = req.body;
  const userJSONSTRING = req.params.userRepresentation;

  // This will convert this JSON formatted string into a JS Object that we can use
  const userData = JSON.parse(userJSONSTRING);

  if (!userData || !userData.id) {
    return res.status(400).json({ error: "User data or userId is missing" });
  }
  try {
    // Reference to the user document in Firestore
    const docRef = db.collection("users").doc(user.id);

    // Check if the document exists
    const docSnapshot = await docRef.get();
    if (!docSnapshot.exists) {
      return res.status(404).json({ error: "User not found" });
    }

    // Updates each field in the Firestore for the specific user being updated
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

// Server AI call

app.get("/api/prompt/:goal", async (req, res) => {
  const genAI = new GoogleGenerativeAI(process.env.API_KEY);
  const model = genAI.getGenerativeModel({ model: "gemini-1.5-flash" });
  const goal = req.params.goal;

  const prompt = `
  You are a highly intelligent assistant helping the user achieve their personal goal. The user has provided a goal, and you are to break it down into smaller, actionable tasks (quests). These tasks should be detailed, specific, and ordered logically, from the first step to the last, to help the user accomplish their goal efficiently.

### User Goal: ${goal}

1. **Breakdown the Goal:**
   - Split the goal into a sequence of specific tasks that the user can complete in order. 
   - Ensure that each task is clear, actionable, and aimed at progressively achieving the final goal. 

2. **Task Specificity:**
   - For each task, provide a step-by-step explanation of what needs to be done.
   - If any task requires learning a new skill or gathering specific resources, include that information and suggest sources for learning (e.g., books, online courses, tutorials).
   - Each task should include a clear outcome that shows the user they have completed that task successfully.

3. **Resource Suggestions:**
   - For each major task, suggest useful resources such as articles, videos, books, or websites that will help the user.
   - Where appropriate, include hyperlinks to relevant resources or platforms (e.g., YouTube videos, Udemy courses, etc.).

4. **Time Frame Recommendation:**
   - Based on the complexity of the tasks, recommend a reasonable time frame for completing each individual task.
   - After calculating individual time frames, suggest an overall time frame for completing the entire project.
   - Consider the user’s skill level and experience when recommending the time.

### Example Output:
- Goal: [User goal example]
- Tasks:
  1. Task 1: [Detailed step-by-step action]
     - Outcome: [What success looks like]
     - Suggested resources: [Links or resource titles]
     - Recommended time: [Time frame for this task]
  2. Task 2: [Next steps…]
     - Outcome: …
     - Suggested resources: …
     - Recommended time: …
  …
- Overall recommended time frame: [Total time frame for completion]

### Important Considerations:
- Ensure the tasks are challenging yet achievable for the user.
- If the goal requires ongoing maintenance (e.g., learning a new skill), include recommendations for continued practice or improvement.
- For longer-term goals, suggest checkpoints or milestones where the user can evaluate their progress.

### Formatting
- Please generate your response as a JSON.
- Here is an example JSON with the correct template:
You are a highly intelligent assistant helping the user achieve their personal goal. The user has provided a goal, and you are to break it down into smaller, actionable tasks (quests). These tasks should be detailed, specific, and ordered logically, from the first step to the last, to help the user accomplish their goal efficiently.

### User Goal:
[Insert user goal]

1. **Breakdown the Goal:**
   - Split the goal into a sequence of specific tasks that the user can complete in order. 
   - Ensure that each task is clear, actionable, and aimed at progressively achieving the final goal. 

2. **Task Specificity:**
   - For each task, provide a step-by-step explanation of what needs to be done.
   - If any task requires learning a new skill or gathering specific resources, include that information and suggest sources for learning (e.g., books, online courses, tutorials).
   - Each task should include a clear outcome that shows the user they have completed that task successfully.

3. **Resource Suggestions:**
   - For each major task, suggest useful resources such as articles, videos, books, or websites that will help the user.
   - Where appropriate, include hyperlinks to relevant resources or platforms (e.g., YouTube videos, Udemy courses, etc.).

4. **Time Frame Recommendation:**
   - Based on the complexity of the tasks, recommend a reasonable time frame for completing each individual task.
   - After calculating individual time frames, suggest an overall time frame for completing the entire project.
   - Consider the user’s skill level and experience when recommending the time.

### Example Output:
- Goal: [User goal example]
- Tasks:
  1. Task 1: [Detailed step-by-step action]
     - Outcome: [What success looks like]
     - Suggested resources: [Links or resource titles]
     - Recommended time: [Time frame for this task]
  2. Task 2: [Next steps…]
     - Outcome: …
     - Suggested resources: …
     - Recommended time: …
  …
- Overall recommended time frame: [Total time frame for completion]

### Important Considerations:
- Ensure the tasks are challenging yet achievable for the user.
- If the goal requires ongoing maintenance (e.g., learning a new skill), include recommendations for continued practice or improvement.
- For longer-term goals, suggest checkpoints or milestones where the user can evaluate their progress.

### Formatting
- Please generate your response as a JSON.
- Here is an example JSON with the correct template:
{
  title: 'Goal title',
  Tasks: {
  endDate: [Timestamp],
  description: 'task description',
  title: 'task title',
  } 
}
- There can be more than one task per goal.
- Please estimate how long each task should take when generating the end date, relative to today.
  
  `;

  const result = await model.generateContent(prompt);
  res.json(result.response.candidates[0].content.parts[0]);
});

const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});
