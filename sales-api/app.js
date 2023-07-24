import express from "express";

import { connectMongoDb } from "./src/config/db/mongoDbConfig.js";
import { createInitialData } from "./src/config/db/initialData.js";
import { connectRabbitMq } from "./src/config/rabbitmq/rabbitConfig.js";

import checkToken from "./src/config/auth/checkToken.js";
import orderRoutes from "./src/modules/sales/routes/OrderRoutes.js";
// import { sendMessageToProductStockUpdateQueue } from "./src/modules/product/rabbitmq/productStockUpdateSender.js";
import tracing from "./src/config/tracing.js";

const app = express();
const env = process.env;
const PORT = env.PORT || 8082;
const CONTAINER_ENV = "container";
const THREE_MINUTES = 180000;

startApplication();

app.use(express.json());
createDataForTesting();
getStatus();
app.use(tracing);
app.use(checkToken);
app.use(orderRoutes);

// only for test
// app.get("/api/testesendproduct", async(req,res)=>{

//     try {
//         sendMessageToProductStockUpdateQueue([
//             {
//                 productId: 1001,
//                 quantity: 3,
//             },
//             {
//                 productId: 1003,
//                 quantity: 2,
//             }
//         ]);
//         return res.status(200).json({status: 200});
//     } catch (error) {
//         console.error(error);
//         return res.status(500).json({error: true});
//     }
// });



app.listen(PORT, () => {
  console.info(`Server started in port ${PORT}`);
});

function startApplication() {
  if (CONTAINER_ENV === env.NODE_ENV) {
    console.info("Waiting for RabbitMQ to start please...");
    setInterval(async () => {
      connectMongoDb();
      createInitialData();
    }, THREE_MINUTES);
  } else {
    connectMongoDb();
    createInitialData();
    connectRabbitMq();
  }
}

function createDataForTesting(){
  app.get("/api/initial-data", async (req, res) => {
    await createInitialData();
    return res.json({ message: "Data for testing created."});
  });
}

function getStatus(){
  app.get("/api/status", async (req, res) => {
    return res.status(200).json({
      service: "SALES API",
      httpStatus: 200,
      status: "UP",
    });
  });
}
