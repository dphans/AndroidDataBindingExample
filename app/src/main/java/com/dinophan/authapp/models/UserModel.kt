package com.dinophan.authapp.models

import com.dinophan.authapp.bases.BaseModel

class UserModel: BaseModel() {

    var username: String    = String()
    var password: String    = String()

    override fun validates(): Boolean {
        return try {
            this@UserModel.errorMessage = null

            if (this@UserModel.username.length < 6) {
                if (this@UserModel.username.isNotEmpty()) {
                    throw Exception("Username must be minimum 6 characters long!")
                }
                return false
            }
            this@UserModel.errorMessage = null

            if (this@UserModel.password.length < 6) {
                if (this@UserModel.password.isNotEmpty()) {
                    throw Exception("Password must be minimum 6 characters long!")
                }
                return false
            }
            this@UserModel.errorMessage = null

            true
        } catch (validationException: Exception) {
            this@UserModel.errorMessage = validationException.localizedMessage
            false
        }
    }

    fun validates(confirmationPassword: String?): Boolean {
        return try {
            if (confirmationPassword != null && this@UserModel.password != confirmationPassword) {
                throw Exception("Confirmation password does not match!")
            }
            this@UserModel.errorMessage = null

            this@UserModel.validates()
        } catch (validationException: Exception) {
            this@UserModel.errorMessage = validationException.localizedMessage
            false
        }
    }

}