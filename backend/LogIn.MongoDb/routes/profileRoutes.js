/**
 * @Author Marius Funk
 */

const express = require('express')
const router = express.Router()
const UserData = require('../models/userData')
const verification = require("../Verification/verification")
const jwt = require('jsonwebtoken')



/**
 * Return a profile specified by its id
 */
router.get('/profile', (req, res) => {
    try {
        const userId = req.query.userId

        const userQuery = UserData.findById(userId)
       
        userQuery.exec(function(err, user){
            if(err){
                return res.status(401)
            }
            else{
                return res.status(201).json(user)
            }
         })
    } catch (err) {
        console.log(err)
        res.status(400).json({ message: err.message })
    }
})
/**
 * Returns all profiles / users
 * 
 * This is used for the map in the application
 * 
 */
router.get('/profile/all', (req, res) => {
    try {

        const userQuery = UserData.find()
       
        userQuery.exec(function(err, user){
            if(err){
                return res.status(401)
            }
            else{
                return res.status(201).json(user)
            }
         })
    } catch (err) {
        console.log(err)
        res.status(400).json({ message: err.message })
    }
})
/**
 * Returns the Logged in user specified by the login data
 */
router.get('/profile/byLoginId', (req, res) => {
    try {
        const userQuery = UserData.find({loginId: req.query.loginId})
       
        userQuery.exec(function(err, user){
            if(err){
                return res.status(401)
            }
            else{
                return res.status(201).json(user[0])
            }
         })
    } catch (err) {
        console.log(err)
        res.status(400).json({ message: err.message })
    }
})

/**
 * Updates the logged in users profile, mostly by location each login
 */
router.put('/profile',  verification.verifyTokenExists, async (req, res) => {
    try {
        const user = req.body.id 
        //Verify token by comparing it to the authData (The logged in user)
        jwt.verify(req.token, process.env.JWT_SECRET, async (err, authData) => {
            if(err || authData.user._id !== user){
                return res.status(400).json({ message: "Authentification failed" })
            }
            const userQuery = UserData.findByIdAndUpdate(req.body.id,
                {userId: req.body.id,
                firstName: req.body.firstName,
                secondName: req.body.secondName,
                email: req.body.email,
                longditude: req.body.longditude,
                latitude: req.body.latitude,
                loginId: req.body.loginId,
                displayName: req.body.displayName}, 
                {new: true},
                async function(err, user){
                    if(err){
                        return res.status(401).json(err)
                    }
                    else{
                        await user.save()
                        return res.status(201).json(user)
                    }
            })
        })   

    } catch (err) {
        console.log(err)
        res.status(400).json({ message: err.message })
    }
})




module.exports = router