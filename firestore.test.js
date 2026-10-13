const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_member";
const BOB_ADMIN_UID = "bob_admin";
const DATABASE_ID = "ai-studio-android-acdacomm-cc612282-b878-4b4b-b31a-d54f3a78d500";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user: cannot read projects or bulletins", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("projects").get());
  await assertFails(unauthDb.collection("bulletins").get());
});

test("Authenticated member: can read projects", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("projects").doc("proj1").set({
      title: "Udege Water Project",
      budget: 10000000,
      status: "IN_PROGRESS",
      category: "INFRASTRUCTURE"
    });
  });

  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(aliceDb.collection("projects").doc("proj1").get());
});

test("Regular member: cannot create projects", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(aliceDb.collection("projects").doc("proj2").set({
    title: "Unauthorized Project",
    budget: 500000,
    status: "PLANNING",
    category: "HEALTH"
  }));
});

test("Administrator: can create and update projects", async () => {
  const adminDb = testEnv.authenticatedContext(BOB_ADMIN_UID, { admin: true }).firestore();
  await assertSucceeds(adminDb.collection("projects").doc("proj_admin").set({
    title: "National Secretariat Solar Power",
    budget: 15000000,
    status: "IN_PROGRESS",
    category: "POWER"
  }));
});

test("Regular member: can post in forum with matching authorId", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(aliceDb.collection("forum_posts").doc("post1").set({
    title: "Youth Farming Initiative Proposal",
    content: "Let us organize cooperative farming in Afo ancestral lands.",
    author: "Alice",
    authorId: ALICE_UID,
    createdAt: new Date().toISOString()
  }));
});

test("Regular member: cannot spoof authorId in forum post", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(aliceDb.collection("forum_posts").doc("post2").set({
    title: "Spoofed Post",
    content: "Testing impersonation",
    author: "Bob",
    authorId: BOB_ADMIN_UID,
    createdAt: new Date().toISOString()
  }));
});
