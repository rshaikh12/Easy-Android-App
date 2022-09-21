/**
 * @Author Marius Funk
 * 
 * UNUSED
 */

 const LocalStrategy = require('passport-local').Strategy
const bcrypt = require('bcrypt')


function initialize(passport, getUserByEmail, getUserById) {
 
    passport.use(new LocalStrategy({ usernameField: 'email' }, async (email, password, done) => {
        const userQuery = getUserByEmail(email)


        userQuery.then(async user => {
            if (user == null) {
                return done(null, false, console.log('No user with that email!'))
            }

            console.log(password + " " + user.password)
            try {
                if (await bcrypt.compare(password, user.password)) {
                    return done(null, user, console.log(password + ", Input: " + user.password))
                }
                else {
                    return done(null, false, console.log('Password incorrect!'))
                }
            }
            catch (e) {
                done(e)
            }
        })
    }
    ))


    passport.serializeUser((user, done) => done(null, user.id))
    // passport.deserializeUser((id, done) => {
    //     return done(null, getUserById(id))
    // })

    passport.deserializeUser(
        (id, done) => {
            done(null, getUserById)
        }
    )

}

module.exports = initialize
// module.exports = passport
