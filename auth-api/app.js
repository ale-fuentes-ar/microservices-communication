import express  from "express";

import userRouter from './src/modules/user/routes/UserRoutes.js';
import * as db from "./src/config/db/initialData.js";

const app = express();
const env = process.env;
const PORT = env.PORT || 8080;

db.createInitialData();

//help check - only know if our service is up
app.get('/api/status', (req, res) => {
    return res
        .status(200)
        .json({
            service: 'AUTH API',
            httpStatus: 200,
            status: 'UP',
        })
})

app.use(express.json());
app.use(userRouter);

app.listen(PORT, () => {
    console.info(`Server started in port ${PORT}`);
})