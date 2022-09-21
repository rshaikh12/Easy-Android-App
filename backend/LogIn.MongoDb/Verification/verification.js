/**
 * @Author Marius Funk
 */

const jwt = require('jsonwebtoken')

//Authorization: Bearer <token>

//Verify the token

var  verifyTokenExists = function(req, res, next){
    const bearerHeader = req.headers['authorization']

    if(typeof bearerHeader !== 'undefined'){
        //Split at the space
        const bearer = bearerHeader.split(' ')

        const bearerToken = bearer[1]

        req.token = bearerToken;

        return next()

    } else{
        return res.sendStatus(403)
    }

}


module.exports.verifyTokenExists = verifyTokenExists