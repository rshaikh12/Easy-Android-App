/**
 * @Author Marius Funk, Roxana Shaikh
 */

const mongoose = require('mongoose')
const ChatEntryData = require('../models/chatEntryData')


const chatDataSchema = new mongoose.Schema({
    userId1: {
        type: String,
        required: true,
        unique: true,
    },
    userId2: {
        type: String,
        required: true,
        unique: true,
    },
    chat: [{ type: mongoose.Schema.Types.ObjectId, ref: 'chatEntryData' }]

}, { timestamps: true })

module.exports = mongoose.model('chatData', chatDataSchema)