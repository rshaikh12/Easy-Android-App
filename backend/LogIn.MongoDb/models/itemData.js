/**
 * @Author Marius Funk
 */

 const mongoose = require('mongoose')





const itemDataSchema = new mongoose.Schema({
  name: {
    type: String,
    required: true
  },
  description: {
    type: String,
  },
  latitude: {
    type: Number
  },
  userId:{
    type: String,
    required: true,
    unique: false
  },
  longditude: {
    type: Number
  },
  categories:[{
      type: String
  }],
  image:{
    data: Buffer,
    contentType: String
  },
  isSold:{
    type: Boolean,
    required: true
  },
  timestamp: {
    type: Number
  }

})

module.exports = mongoose.model('itemData', itemDataSchema)

