import bcrypt from "bcrypt";
import User from "../../modules/user/model/User.js";

export async function createInitialData() {
    try {
        
        await User.sync({ force: true });
    
        let password = await bcrypt.hash('a12345678', 10);
        
        let firstUser = await User.create({
            name: 'Use Testing One',
            email: 'usetesting.one@gmail.com',
            password: password,
        });
        
        let secondUser = await User.create({
            name: 'Use Testing Two',
            email: 'usetesting.two@gmail.com',
            password: password,
        });

    } catch (error) {
        console.log(error);
    }

}
