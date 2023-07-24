import Order from "../../modules/sales/model/Order.js";
import { v4 as uuid4 } from "uuid";

export async function createInitialData(){

    let existingData = await Order.findOne();
    if(existingData){
        console.info("Remove existing data...");
        await Order.collection.drop();
    }

    await Order.create({
        products: [{
            productId: 1001,
            quantity: 2,
        },{
            productId: 1002,
            quantity: 2,
        },{
            productId: 1003,
            quantity: 2,
        }],
        user:{
            id: '123poi123poi123poi',
            name: 'user teste',
            email:'usertest@mail.com'
        },
        status: 'APPROVED',
        createAt: new Date(),
        updateAt: new Date(),
        transactionid: uuid4(),
        serviceid: uuid4(),
    });
    await Order.create({
        products: [{
            productId: 1001,
            quantity: 1,
        },{
            productId: 1003,
            quantity: 3,
        }],
        user:{
            id: '123poi123poi123pttTT',
            name: 'user teste',
            email:'usertest@mail.com'
        },
        status: 'REJECTED',
        createAt: new Date(),
        updateAt: new Date(),
        transactionid: uuid4(),
        serviceid: uuid4(),
    });

    let initialData = await Order.find();
    console.info(`Initial data was create > ${JSON.stringify(initialData, undefined, 4)}`);
}