package com.alex.bagofwords;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;



public class ValidationTest {


    @Test
    public void passwordShouldBeFalse() {
        assertEquals(false, Validation.validPassword(""));
        assertEquals(false, Validation.validPassword("12345"));
        assertEquals(false, Validation.validPassword("1wed"));
        assertEquals(false, Validation.validPassword("passw"));
        assertEquals(false, Validation.validPassword("pas2"));
        assertEquals(false, Validation.validPassword(" dfdf dfdf"));
        assertEquals(false, Validation.validPassword("password _ sd"));
        assertEquals(false, Validation.validPassword("password _ sd"));
        assertEquals(false, Validation.validPassword(" qwerty "));
    }

    @Test
    public void passwordShouldBeTrue() {
        assertEquals(true, Validation.validPassword("qwerty"));
        assertEquals(true, Validation.validPassword("123456"));
        assertEquals(true, Validation.validPassword("1wedfdfsdfd"));
        assertEquals(true, Validation.validPassword("passwfdfsddff"));
        assertEquals(true, Validation.validPassword("passwordBlah"));
        assertEquals(true, Validation.validPassword("password55?@h"));
        assertEquals(true, Validation.validPassword("_passwor_5?@h"));
        assertEquals(true, Validation.validPassword("@!_:p@ssword,."));
    }


    @Test
    public void matchingPasswordsShouldBeTrue() {
        assertEquals(true, Validation.matchingPassword("qwerty", "qwerty"));
        assertEquals(true, Validation.matchingPassword("123456", "123456"));
        assertEquals(true, Validation.matchingPassword("1wedfdfsdfd", "1wedfdfsdfd"));
        assertEquals(true, Validation.matchingPassword("passwfdfsddff", "passwfdfsddff"));
        assertEquals(true, Validation.matchingPassword("testPassWoRD", "testPassWoRD"));
    }

    @Test
    public void matchingPasswordsShouldBeFalse() {
        assertEquals(false, Validation.matchingPassword("qwerty", "qwert"));
        assertEquals(false, Validation.matchingPassword("123456", "1234"));
        assertEquals(false, Validation.matchingPassword("1wedfdfsdfd", "1wedf"));
        assertEquals(false, Validation.matchingPassword("passwfdfsddff", "passdfff"));
        assertEquals(false, Validation.matchingPassword("testPassWoRD", "testPassWod"));
    }

    @Test
    public void emailShouldBeTrue() {
        assertEquals(true, Validation.validEmail("email@gmail.com"));
        assertEquals(true, Validation.validEmail("alex.conroy8@mail.dcu.ie"));
        assertEquals(true, Validation.validEmail("testEmail@mail.com"));
        assertEquals(true, Validation.validEmail("alexdconroy@hotmail.com"));
    }

    @Test
    public void emailShouldBeFalse() {
        assertEquals(false, Validation.validEmail("mail@.com"));
        assertEquals(false, Validation.validEmail("alexdconroy@.com"));
        assertEquals(false, Validation.validEmail("tst_email.com"));
        assertEquals(false, Validation.validEmail("123 2121 @gmail.com"));
        assertEquals(false, Validation.validEmail("hotmail.com@alex"));
    }

    @Test
    public void fieldNotEmptyShouldBeTrue() {
        assertEquals(true, Validation.fieldNotEmpty("not empty"));
        assertEquals(true, Validation.fieldNotEmpty("First Second"));
        assertEquals(true, Validation.fieldNotEmpty("Alex Conroy"));
        assertEquals(true, Validation.fieldNotEmpty("TestIng"));
    }

    @Test
    public void fieldNotEmptyShouldBeFalse() {
        assertEquals(false, Validation.fieldNotEmpty(""));
    }

    @Test
    public void usernameShouldBeTrue() {
        assertEquals(true, Validation.validUsername("conroya8"));
        assertEquals(true, Validation.validUsername("binders94"));
        assertEquals(true, Validation.validUsername("markyMark"));
        assertEquals(true, Validation.validUsername("m1992"));
        assertEquals(true, Validation.validUsername("joe_bloggs@"));
    }

    @Test
    public void usernameShouldBeFalse() {
        assertEquals(false, Validation.validUsername(""));
        assertEquals(false, Validation.validUsername("92conroy"));
        assertEquals(false, Validation.validUsername("conroy 92"));
    }

    @Test
    public void nameShouldBeTrue() {
        assertEquals(true, Validation.validName("Alex Conroy"));
        assertEquals(true, Validation.validName("Qun Liu"));
        assertEquals(true, Validation.validName("Mark Nannery"));
        assertEquals(true, Validation.validName("Joe Bloggs"));
    }

    @Test
    public void nameShouldBeFalse() {
        assertEquals(false, Validation.validName(""));
        assertEquals(false, Validation.validName("A"));
        assertEquals(false, Validation.validName("AC"));
        assertEquals(false, Validation.validName("Alex Conroyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy"));
    }


}