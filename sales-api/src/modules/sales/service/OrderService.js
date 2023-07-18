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
      const { transactionid, serviceid } = req.headers;
      console.info(
        `Request to POST create order with data ${JSON.stringify(
          orderData
        )} | [ transactionID: ${transactionid} } | serviceID: ${serviceid} ]`
      );

      this.validateOrderData(orderData);
      const { authUser } = req;
      const { authorization } = req.headers;

      let order = this.createInitialOrderData(orderData, authUser, transactionid, serviceid);
      await this.validateProductStock(order, authorization, transactionid);
      const createOrder = await OrderRepository.save(order);
      this.sendMessage(createOrder, transactionid);

      let response = {
        status: httpStatus.SUCCESS,
        createOrder,
      };

      console.info(
        `Request to POST create order with data ${JSON.stringify(
          response
        )} | [ transactionID: ${transactionid} } | serviceID: ${serviceid} ]`
      );

      return response;
    } catch (error) {
      return {
        status: error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
        message: error.message,
      };
    }
  }

  createInitialOrderData(orderData, authUser, transactionid, serviceid) {
    return {
      status: orderStatus.PENDING,
      user: authUser,
      createAt: new Date(),
      updateAt: new Date(),
      transactionid,
      serviceid,
      products: orderData.products,
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
        console.warn(`The order message was not complete. TransactionIDÇ ${orderMessage.transactionid}`);
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

  async validateProductStock(order, token, transactionid) {
    let stockisOk = await ProductClient.checkProductStock(
      order,
      token,
      transactionid
    );
    if (!stockisOk) {
      throw new OrderException(
        httpStatus.BAD_REQUEST,
        "The stock is out for the products."
      );
    }
  }

  sendMessage(createOrder, transactionid) {
    const message = {
      salesId: createOrder.id,
      products: createOrder.products,
      transactionid,
    };

    sendMessageToProductStockUpdateQueue(message);
  }

  async findById(req) {
    try {
      const { id } = req.params;
      const { transactionid, serviceid } = req.headers;
      console.info(
        `Request to GET order by ID ${id} | [ transactionID: ${transactionid} } | serviceID: ${serviceid} ]`
      );

      this.validateInformedId(id);
      const orderFromId = await OrderRepository.findById(id);

      if (!orderFromId) {
        throw new OrderException(
          httpStatus.BAD_REQUEST,
          "The order was not found."
        );
      }

      let response = {
        status: httpStatus.SUCCESS,
        orderFromId,
      };

      console.info(
        `Request to GET order by ID ${id}: ${JSON.stringify(
          response
        )} | [ transactionID: ${transactionid} } | serviceID: ${serviceid} ]`
      );

      return response;
    } catch (error) {
      return {
        status: error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
        message: error.message,
      };
    }
  }

  validateInformedId(id) {
    if (!id) {
      throw new OrderException(
        httpStatus.BAD_REQUEST,
        "The order ID must be informed."
      );
    }
  }

  validateInformedProductId(id) {
    if (!id) {
      throw new OrderException(
        httpStatus.BAD_REQUEST,
        "The product ID must be informed."
      );
    }
  }

  async findAll(req) {
    try {
      const orders = await OrderRepository.findAll();
      const { transactionid, serviceid } = req.headers;
      console.info(
        `Request to GET all orders | [ transactionID: ${transactionid} } | serviceID: ${serviceid} ]`
      );

      if (!orders) {
        throw new OrderException(
          httpStatus.BAD_REQUEST,
          "No orders were found."
        );
      }

      let response = {
        status: httpStatus.SUCCESS,
        orders,
      };

      console.info(
        `Request to GET all orders: ${JSON.stringify(
          response
        )} | [ transactionID: ${transactionid} } | serviceID: ${serviceid} ]`
      );

      return response;
    } catch (error) {
      return {
        status: error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
        message: error.message,
      };
    }
  }

  async findByProductId(req) {
    try {
      const { productId } = req.params;
      const { transactionid, serviceid } = req.headers;
      console.info(
        `Request to GET all orders by ProductID ${productId} | [ transactionID: ${transactionid} } | serviceID: ${serviceid} ]`
      );

      this.validateInformedProductId(productId);
      const orders = await OrderRepository.findByProductId(productId);

      if (!orders) {
        throw new OrderException(
          httpStatus.BAD_REQUEST,
          "No orders were found."
        );
      }

      console.log(orders.map((order) => order.id));
      console.log(
        orders.map((order) => {
          return order.id;
        })
      );

      let response = {
        status: httpStatus.SUCCESS,
        salesIds: orders.map((order) => order.id),
      };

      console.info(
        `Request to GET all orders by ProductID ${productId}: ${JSON.stringify(
          response
        )} | [ transactionID: ${transactionid} } | serviceID: ${serviceid} ]`
      );

      return response;
    } catch (error) {
      return {
        status: error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
        message: error.message,
      };
    }
  }
}

export default new OrderService();
