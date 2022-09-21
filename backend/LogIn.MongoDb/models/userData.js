/**
 * @Author Marius Funk
 */

 const mongoose = require('mongoose')

const userDataSchema = new mongoose.Schema({
  firstName: {
    type: String,
    required: true
  },
  secondName: {
    type: String,
    required: true
  },
  email: {
    type: String,
    required: true,
    unique: true
  },
  latitude: {
    type: Number,
    required: false
  },
  longditude: {
    type: Number,
    required: false
  },
  loginId:{
    type: String,
    required: true,
    unique: true
    },
    displayName:{
    type: String,
    required: true,
    unique: true
    }

})

module.exports = mongoose.model('userData', userDataSchema)