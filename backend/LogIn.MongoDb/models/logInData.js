/**
 * @Author Marius Funk
 */

 const mongoose = require('mongoose')

const logInDataSchema = new mongoose.Schema({
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
  password: {
    type: String,
    required: true
  },

})

module.exports = mongoose.model('logInData', logInDataSchema)