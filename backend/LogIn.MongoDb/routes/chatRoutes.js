/**
 * @Author Marius Funk, Roxana Shaikh
 */

const express = require('express')
const router = express.Router()
const ChatData = require('../models/chatData')
const ChatEntryData = require('../models/chatEntryData')
const verification = require("../Verification/verification")
const jwt = require('jsonwebtoken')


//get my and other person's chat
router.get('/chat', verification.verifyTokenExists, (req, res) => {

    try {
        var id1 = req.query.userId1
        var id2 = req.query.userId2
        //Verify token by comparing it to the authData (The logged in user)
        jwt.verify(req.token, process.env.JWT_SECRET, async (err, authData) => {

            if (err || (authData.user._id !== id1 && authData.user._id !== id2)) {

                return res.status(400).json({ message: "Authentification failed" })
            }
            if (id1 > id2) {
                [id1, id2] = [id2, id1]
            }
            const chatQuery = ChatData.find({ userId1: id1, userId2: id2 }).populate('chat');


            chatQuery.exec(function (err, chat) {
                if (err) {
                    return res.status(401).json(err)
                }
                else {
                    const chatEntriesQuery = ChatEntryData.find({
                        '_id': { $in: chat[0].chat }
                    }).exec(function (err, chatContent) {
                        if (err) {
                            return res.status(401).json(err)
                        }
                        else {
                            return res.status(201).json(chatContent)
                        }
                    }
                    )
                }
            })
        })
    } catch (err) {
        console.log(err)
        res.status(400).json({ message: err.message })
    }
})


//get all my chats
// router.get('/chat/byUserId', (req, res) => {
//     try {
//         var id = req.query.userId

//         jwt.verify(req.token, process.env.JWT_SECRET, async (err, authData) => {
//             if(err || authData.user._id !== id){
//                 return res.status(400).json({ message: "Authentification failed" })
//             }
//             const chatQuery = ChatData.find({$or: [{userId1: id},{ userId2: id}]})

//             chatQuery.exec(function(err, chats){
//                 if(err){
//                     return res.status(401).json(err)
//                 }
//                 else{
//                     return res.status(201).json(chats)
//                 }
//              })
//         })
//     } catch (err) {
//         console.log(err)
//         res.status(400).json({ message: err.message })
//     }
// })
router.get('/chat/byUserId', (req, res) => {
    try {
        var id = req.query.userId

        const chatQuery = ChatData.find({ $or: [{ userId1: id }, { userId2: id }] }).populate('chat')

        chatQuery.exec(function (err, chats) {
            if (err) {
                return res.status(401).json(err)
            }
            else {
                return res.status(201).json(chats)
            }
        })

    } catch (err) {
        console.log(err)
        res.status(400).json({ message: err.message })
    }
})


//create or find chat session
router.post('/chat', verification.verifyTokenExists, async (req, res) => {

    try {
        var id1 = req.body.userId1
        var id2 = req.body.userId2
        //Verify token by comparing it to the authData (The logged in user)
        jwt.verify(req.token, process.env.JWT_SECRET, async (err, authData) => {

            if (err || (authData.user._id !== id1 && authData.user._id !== id2)) {
                return res.status(400).json({ message: "Authentification failed" })
            }
            if (id1 > id2) {
                [id1, id2] = [id2, id1]
            }

            var chatData = ChatData.find({ userId1: id1, userId2: id2 });
            //if chat exists return it, otherwise create new
            if (!chatData) {
                chatData = new ChatData({
                    userId1: id1,
                    userId2: id2
                })
                await chatData.save()
            }

            res.status(201).json(chatData)
        })
    } catch (err) {
        console.log(err)
        res.status(400).json({ message: err.message })
    }
})


//send message to other person
router.post('/chat/message', async (req, res) => {
    try {
        var id = req.body.userId

 
        const chatEntryData = new ChatEntryData({
            message: req.body.message,
            userId: req.body.userId,

        })
        await chatEntryData.save()

        var id1 = req.query.userId1
        var id2 = req.query.userId2

        if (id1 > id2) {
            [id1, id2] = [id2, id1]
        }

        const chatQuery = ChatData.findOneAndUpdate({ userId1: id1, userId2: id2 },
            { $push: { chat: chatEntryData._id } },
            { new: true },
            async function (err, chat) {
                if (err) {
                    return res.status(401).json(err)
                }
                else {
                    await chat.save()
                    return res.status(201).json(chat)
                }
            })

    } catch (err) {
        console.log(err)
        res.status(400).json({ message: err.message })
    }
})







module.exports = router