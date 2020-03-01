package com.mindorks.bootcamp.instagram.utils

import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.utils.common.Resource
import com.mindorks.bootcamp.instagram.utils.common.Validation
import com.mindorks.bootcamp.instagram.utils.common.Validator
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.hasSize
import org.junit.Test

class ValidatorTest {

    @Test
    fun givenValidEmailAndValidPwd_whenValidate_shouldReturnSuccess(){
        val email = "test@gmail.com"
        val password = "123456"
        val validations = Validator.validateLoginFields(email,password)
        assertThat(validations,hasSize(2))
        assertThat(
            validations,
            contains(Validation(Validation.Field.EMAIL, Resource.success()),
                    Validation(Validation.Field.PASSWORD, Resource.success())
                )
            )
    }

    @Test
    fun givenInvalidEmailAndValidPwd_whenValidate_shouldReturnFailed(){
        val email = "abc"
        val password = "123456"
        val validations = Validator.validateLoginFields(email,password)
        assertThat(validations,hasSize(2))
        assertThat(validations,
            contains(Validation(Validation.Field.EMAIL, Resource.error(R.string.email_field_invalid)),
                Validation(Validation.Field.PASSWORD, Resource.success())
            ))
    }

    @Test
    fun givenValidEmailAndInvalidPwd_whenValidate_shouldReturnFailed(){
        val email = "test@gmail.com"
        val password = "1234"
        val validations = Validator.validateLoginFields(email,password)
        assertThat(validations,hasSize(2))
        assertThat(validations,
            contains(Validation(Validation.Field.EMAIL, Resource.success()),
                Validation(Validation.Field.PASSWORD, Resource.error(R.string.password_field_small_length))
            ))
    }



}