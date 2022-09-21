/**
 * @Author Marius Funk
 */

const express = require('express')
const router = express.Router()
const LogInData = require('../models/logInData')
const UserData = require('../models/userData')
const LoginResponseData = require('../models/loginResponseData')
const bcrypt = require('bcrypt')
const jwt = require('jsonwebtoken')

/**
 * Login for the easy application
 * Hashed the password using bcryipt 
 * Creates a JWT token based on the SECRET and returns said token and user
 * 
 * Post is used for security reasons
 */
router.post('/login', async (req, res) => {
    try {
        const hashedPassword = await bcrypt.hash(req.query.password, 10)
        const logInData = await LogInData.find({email: req.query.email})
        if(logInData.length != 1){
            res.status(403).json("No matching user found.")
        } else{
            const userData = await UserData.find({loginId:logInData[0].id})
            //Correct Login is verified
            bcrypt.compare(req.query.password,logInData[0].password, function(err,bcryptRes){
                if(err){
                    return res.status(403).json({err})
                }
                if(bcryptRes){
                    const token = jwt.sign({user:userData[0]}, process.env.JWT_SECRET)
                    return res.status(201).json(new LoginResponseData(token,userData[0]))
                } else{
                    return res.status(403).json({message: 'passwords do not match'})
                }
            })
        }    
    }
    catch (err) {
        res.status(500).json({ message: err.message })
    }
})
/**
 * Register a new account
 * Saves hashed password in the database
 */
router.post('/registration', async (req, res) => {

    //Should be checked on the front end
    if (req.body.password === req.body.password2) {

        try {
            const hashedPassword = await bcrypt.hash(req.body.password, 10)

            //create login
            const logInData = new LogInData({
                firstName: req.body.firstName,
                secondName: req.body.secondName,
                email: req.body.email,
                password: hashedPassword
            })
            await logInData.save()

            //Immediatly create user profile 
            const userData = new UserData({
                firstName: req.body.firstName,
                secondName: req.body.secondName,
                email: req.body.email,
                loginId: logInData.id,
                displayName: req.body.firstName + " " + req.body.secondName
            })
            await userData.save()

            res.status(201).json(logInData)

        } catch (err) {
            res.status(400).json({ message: err.message })
        }
    }
    else {
        console.log("Passwords don't match")
    }

})

module.exports = router