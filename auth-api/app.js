import express from "express";

import userRouter from "./src/modules/user/routes/UserRoutes.js";
import * as db from "./src/config/db/initialData.js";
import trancing from "./src/config/trancing.js";

const app = express();
const env = process.env;
const PORT = env.PORT || 8080;
const CONTAINER_ENV = "container";

app.use(express.json());
startApplication();
createDataForTesting();
getStatusApi();
app.use(trancing);
app.use(userRouter);

app.listen(PORT, () => {
  console.info(`Server started in port ${PORT}`);
});

function getStatusApi() {
  //help check - only know if our service is up
  app.get("/", (req, res) => {
    return res.status(200).json(getOkResponse());
  });
  // root check - for AWS checking to healft or service.
  app.get("/api/status", (req, res) => {
    return res.status(200).json(getOkResponse());
  });
}

function getOkResponse() {
  return {
    service: "AUTH API",
    httpStatus: 200,
    status: "UP",
  };
}

function startApplication() {
  if (env.NODE_ENV !== CONTAINER_ENV) {
    db.createInitialData();
  }
}

function createDataForTesting() {
  app.get("/api/initial-data", (req, res) => {
    db.createInitialData();
    return res.json({ message: "Data for testing created." });
  });
}
