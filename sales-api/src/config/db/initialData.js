import Order from "../../modules/sales/model/Order.js";

export async function createInitialData(){

    await Order.collection.drop();

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
    });

    let initialData = await Order.find();
    console.info(`Initial data was create > ${JSON.stringify(initialData, undefined, 4)}`);
}