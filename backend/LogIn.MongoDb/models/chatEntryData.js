/**
 * @Author Marius Funk
 */

const mongoose = require('mongoose')

const chatEntryDataSchema = new mongoose.Schema({
    message: {
        type: String,
        required: true
    },
    userId: {
        type: String,
        required: true
    },

}, { timestamps: true });

module.exports = mongoose.model('chatEntryData', chatEntryDataSchema)