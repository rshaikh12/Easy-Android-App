/**
 * @Author Marius Funk
 */

const express = require('express')
const router = express.Router()
const ItemData = require('../models/itemData')
const fs = require('fs');
const verification = require("../Verification/verification")
const jwt = require('jsonwebtoken')

/**
 * Get items by the position of the user and a range.
 * Works in principal but is not used in the application for time constraints.
 * 
 * The Software was in later stages changed to show users on the map instead of items. Therefore this and "findAll" are unused.
 */
router.get('/item', (req, res) => {
    try {
        const posLat = Number(req.query.latitude)
        const posLong = Number(req.query.longditude)
        const range = Number(req.query.range)

        const upperBound = posLong + range
        const lowerBound = posLong - range
        const leftBound = posLat - range
        const rightBound = posLat + range
        //Find by position and range
        const itemQuery = ItemData.find({longditude: {$gt: lowerBound, $lt: upperBound}, latitude: {$gt: leftBound, $lt: rightBound}  })
       
        itemQuery.exec(function(err, items){
            if(err){
                return res.status(401).json(err)
            }
            else{
                return res.status(201).json(items)
            }
         })
    } catch (err) {
        console.log(err)
        res.status(400).json({ message: err.message })
    }
})

/**
 * returns all item entries.
 * Works in principal but is not used in the application for time constraints.
 * 
 * The Software was in later stages changed to show users on the map instead of items. Therefore this and "get" are unused.
 */
router.get('/item/findAll', (req, res) => {
    try {

        const itemQuery = ItemData.find()
       
        itemQuery.exec(function(err, items){
            if(err){
                return res.status(401).json(err)
            }
            else{
                return res.status(201).json(items)
            }
         })
    } catch (err) {
        console.log(err)
        res.status(400).json({ message: err.message })
    }
})

/**
 * find and return one specific item by its id
 */
router.get('/item/findById', (req, res) => {
    try {
        
        const itemQuery = ItemData.findById(req.query.id)

         itemQuery.exec(function(err, item){
            if(err){
                return res.status(401).json(err)
            }
            else{
                return res.status(201).json(item)
            }
         })
        
        

    } catch (err) {
        console.log(err)
        res.status(400).json({ message: err.message })
    }
})

/**
 * Returns all user items
 * 
 * This is used to show the users own items as well as the items of a specified user
 */
router.get('/item/findByUserId', (req, res) => {
    try {
        
        const itemQuery = ItemData.find({userId: req.query.userId})

         itemQuery.exec(function(err, items){
            if(err){
                return res.status(401).json(err)
            }
            else{
                return res.status(201).json(items)
            }
         })
        
        

    } catch (err) {
        console.log(err)
        res.status(400).json({ message: err.message })
    }
})


router.get('/item/findAll', (req, res) => {
    try {
        
        const itemQuery = ItemData.find({})

         itemQuery.exec(function(err, items){
            if(err){
                return res.status(401).json(err)
            }
            else{
                return res.status(201).json(items)
            }
         })
        
        

    } catch (err) {
        console.log(err)
        res.status(400).json({ message: err.message })
    }
})

router.post('/item', verification.verifyTokenExists,  async (req, res) => {
    
    try {
        const user = req.body.userId
        
        //Verify token by comparing it to the authData (The logged in user)
        jwt.verify(req.token, process.env.JWT_SECRET, async (err, authData) => {
            if(err || authData.user._id !== user){
                return res.status(400).json({ message: "Authentification failed" })
            }
            const itemData = new ItemData({
                userId: user,
                name: req.body.name,
                description: req.body.description,
                image: req.body.image,
                longditude: req.body.longditude,
                latitude: req.body.latitude,
                isSold: false,
                timestamp: new Date().getTime()
            })
            await itemData.save()
            
            //Update the user to include the image (This should not be necessary but a bug used to occur regarding this)
            const itemQuery = ItemData.findByIdAndUpdate(itemData._id,
                {image: req.body.image}, {new: true},
                async function(err, item){
                    if(err){
                        return res.status(401)
                    }
                    else{
                        await item.save()
                        return res.status(201).json(item)
                    }
            })
        })
        
    } catch (err) {
        console.log(err)
        res.status(400).json({ message: err.message })
    }
})

/**
 * Adds a specific image to a item
 * This was meant to be used to add an image to the image array of the item but the array structure was changed to single images.
 * Again, because of time constraints
 */
router.post('/item/image', verification.verifyTokenExists,  async (req, res) => {
    try {

        const user = req.query.userId
        
        //Verify token by comparing it to the authData (The logged in user)
        jwt.verify(req.token, process.env.JWT_SECRET, async (err, authData) => {
            if(err || authData.user._id !== user){
                return res.status(400).json({ message: "Authentification failed" })
            }
            const newImage = req.body 

            //Updates the item
            const itemQuery = ItemData.findByIdAndUpdate(req.body.id,
                {image: newImage}, {new: true},
                async function(err, item){
                    if(err){
                        return res.status(401)
                    }
                    else{
                        await item.save()
                        return res.status(201).json(item)
                    }
            })
        })
    } catch (err) {
        console.log(err)
        res.status(400).json({ message: err.message })
    }
})

/**
 * Updates an item
 */
router.put('/item', verification.verifyTokenExists,  async (req, res) => {
    try {
        const user = req.body.userId
        
        //Verify token by comparing it to the authData (The logged in user)
        jwt.verify(req.token, process.env.JWT_SECRET, async (err, authData) => {
            if(err || authData.user._id !== user){
                return res.status(400).json({ message: "Authentification failed" })
            }

            const itemQuery = ItemData.findByIdAndUpdate(req.body.id,
                {userId: req.body.userId,
                name: req.body.name,
                description: req.body.description,
                image: req.body.image,
                longditude: req.body.longditude,
                latitude: req.body.latitude,
                isSold: req.body.isSold}, {new: true},
                async function(err, item){
                    if(err){
                        return res.status(401)
                    }
                    else{
                        await item.save()
                        return res.status(201).json(item)
                    }
            })
        })
    } catch (err) {
        console.log(err)
        res.status(400).json({ message: err.message })
    }
})

/**
 * Deletes the by user id specified item if the request is made by the user him/herself
 */
router.delete('/item', verification.verifyTokenExists,  async (req, res) => {
    try {
        const user = req.query.userId
        
        //Verify token by comparing it to the authData (The logged in user)
        jwt.verify(req.token, process.env.JWT_SECRET, async (err, authData) => {
            if(err || authData.user._id !== user){
                return res.status(400).json({ message: "Authentification failed" })
            }

            const itemQuery = ItemData.findByIdAndDelete(req.query.itemId,
                async function(err, item){
                    if(err){
                        return res.status(401)
                    }
                    else{
                        return res.status(201).json(item)
                    }
            })
        })
    } catch (err) {
        console.log(err)
        res.status(400).json({ message: err.message })
    }
})








module.exports = router