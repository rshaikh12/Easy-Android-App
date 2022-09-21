/**
 * @Author Marius Funk (Alexander Kobler)
 */


if (process.env.NODE_ENV !== 'production') {
    require('dotenv').config()
}

const loginRoute = require('./routes/logInRoutes.js')
const profileRoute = require('./routes/profileRoutes.js')
const itemRoute = require('./routes/itemRoutes.js')
const chatRoute = require('./routes/chatRoutes.js')

const bodyParser = require('body-parser');
const fs = require('fs');

const jwt = require('jsonwebtoken')

const express = require('express')
const path = require("path")
const bcrypt = require('bcrypt')
const passport = require('passport') 
const GoogleStrategy =  require('passport-google-oauth20')
const flash = require('express-flash')
const session = require('express-session')
const methodOverride = require('method-override')
const mongoose = require('mongoose')
const LogInData = require('./models/logInData')

//Connect the database using enviroment file
mongoose.connect(process.env.DATABASE_URL, { useNewUrlParser: true })
const db = mongoose.connection
db.on('error', (error) => console.error(error))
db.once('open', () => console.log('Connected to Database'))

/**
 * UNUSED
 */
const initializePassport = require('./config/passport-config')
initializePassport(passport,
    email => LogInData.findOne({ email: email }, console.log("test  " + email)),
    id => LogInData.findOne({ id: id }, console.log("testid  " + id)),
)

// Defines the basis for the rest API
const app = express()
const port = 443

//Raises json limit for images
app.use(express.json({limit: '20mb'}))
app.use(express.static(__dirname + '/public'));
app.use(express.urlencoded({ extended: false }))
app.use(flash())

//Define a session.UNUSED
app.use(session({
    secret: process.env.SESSION_SECRET,
    resave: false,
    saveUninitialized: false
}))
app.use(passport.initialize())
app.use(passport.session())
app.use(methodOverride('_method'))

/**
 * Use all routes
 */
app.use(loginRoute)
app.use(profileRoute)
app.use(itemRoute)
app.use(chatRoute)

app.listen(port, () => {
    console.log(`Example app listening at http://localhost:${port}`)
})

