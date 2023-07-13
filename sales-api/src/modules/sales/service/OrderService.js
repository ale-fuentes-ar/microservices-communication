import OrderRepository from "../repository/OrderRepository.js";
import { sendMessageToProductStockUpdateQueue } from "../../product/rabbitmq/productStockUpdateSender.js";
import * as httpStatus from "../../../config/constants/httpStatus.js";
import * as orderStatus from "../status/OrderStatus.js";
import OrderException from "../exception/OrderException.js";
import ProductClient from "../../product/client/ProductClient.js";

class OrderService {
  async createOrder(req) {
    try {
      const orderData = req.body;
      this.validateOrderData(orderData);
      const { authUser } = req;
      const { authorization } = req.headers;

      let order = this.createInitialOrderData(orderData, authUser);
      await this.validateProductStock(order, authorization);
      const createOrder = await OrderRepository.save(orderData);
      this.sendMessage(createOrder);

      return {
        status: httpStatus.SUCCESS,
        createOrder,
      };
    } catch (error) {
      return {
        status: error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
        message: error.message,
      };
    }
  }

  createInitialOrderData(orderData, authUser) {
    return {
      status: orderStatus.PENDING,
      user: authUser,
      createAt: new Date(),
      updateAt: new Date(),
      products: orderData,
    };
  }

  async updateOrder(orderMessage) {
    try {
      const order = JSON.parse(orderMessage);
      if (order.salesId && order.status) {
        let existingOrder = await OrderRepository.findById(order.salesId);
        if (existingOrder && order.status !== existingOrder.status) {
          existingOrder.status = order.status;
          existingOrder.updateAt = new Date();
          await OrderRepository.save(existingOrder);
        }
      } else {
        console.warn("The order message was not complete.");
      }
    } catch (error) {
      console.error("Could not parse order message from queue");
      console.error(error.message);
    }
  }

  validateOrderData(data) {
    if (!data || !data.products) {
      throw new OrderException(
        httpStatus.INTERNAL_SERVER_ERROR,
        "The products must be informed."
      );
    }
  }

  async validateProductStock(order, token) {
    let stockisOut = await ProductClient.checkProductStock(order.products, token);
    if (stockisOut) {
      throw new OrderException(
        httpStatus.BAD_REQUEST,
        "The stock is out for the products."
      );
    }
  }

  sendMessage(createOrder){
    const message = {
      salesId: createOrder.id,
      products: createOrder.products,
    }

    sendMessageToProductStockUpdateQueue(message);
  }
}

export default new OrderService();
