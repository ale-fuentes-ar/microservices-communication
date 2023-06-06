import Sequelize from "sequelize";

const sequelize = new Sequelize("auth-db", "admin", "root", {
    host: 'localhost',
    dialect: 'postgres',
    quoteIdentifiers: false,
    define: {
        syncOnAssociation: true,
        timestamps: false,
        underscored: true,
        underscoredAll: true,
        freezeTableName: true
    }
});

sequelize
.authenticate()
.then( () => {
    console.info('Connection has been stablished')
})
.catch((error) => {
    console.error("Unable to connect to the DB");
    console.error(error.message);
});

export default sequelize;

