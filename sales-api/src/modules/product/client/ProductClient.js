import axios from "axios";

import { PRODUCT_API_URL } from "../../../config/constants/secrets.js";

class ProductClient {
  async checkProductStock(products, token) {
    try {
        const headers = {
            Authorization: `Bearer ${token}`ç
        }
        axios.post(`${PRODUCT_API_URL}/check-stock`, {headers}, products)
        .then(res =>{
            return true;
        })
        .catch((error) => {
            console.error(error.response.message);
            return false;
        });
    } catch (error) {
        return  false;
    }
  }
}

export default new ProductClient();
