import jwt from "jsonwebtoken";
import { promisify } from "util";

import AuthException from "./AuthException.js";

import { API_SECRET } from "../constants/secrets.js";
import { UNAUTHORIZED, INTERNAL_SERVER_ERROR } from "../constants/httpStatus.js";

const TXT_BLANK = " ";

export default async (req, res, next) => {

    try {
        
        const { authorization } = req.headers;
    
        if(!authorization) {
            throw new AuthException(
                httpStatus.UNAUTHORIZED,
                'Access token was not informed.'
            );
        }

        let accessToken = authorization;

        if(accessToken.includes(TXT_BLANK)){
            accessToken = accessToken.split(TXT_BLANK)[1];
        }

        const decoded = await promisify(jwt.verify)(
            accessToken, 
            API_SECRET
            );

  
        req.authUser = decoded.authUser;
        return next();
        
    } catch (error) {
        
        const status = error.status ?? INTERNAL_SERVER_ERROR;

        return res.status(status).json({
            status,
            message: error.message,

        });
    }

}