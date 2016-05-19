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
    public void differentPasswordShouldBeFalse() {
        assertEquals(false, Validation.differentPasswords("123456", "123456"));
        assertEquals(false, Validation.differentPasswords("1wed@3D", "1wed@3D"));
        assertEquals(false, Validation.differentPasswords("p@$$word", "p@$$word"));
        assertEquals(false, Validation.differentPasswords("myn@mei$", "myn@mei$"));
        assertEquals(false, Validation.differentPasswords("p@$$word", "p@$$word"));

    }

    @Test
    public void differentPasswordShouldBeTrue() {
        assertEquals(true, Validation.differentPasswords("qwerty", "password1234"));
        assertEquals(true, Validation.differentPasswords("123456", "929292"));
        assertEquals(true, Validation.differentPasswords("1wedfdfsdfd", "qwerty"));
        assertEquals(true, Validation.differentPasswords("passwfdfsddff", "1992_08"));
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
    public void matchingEmailShouldBeTrue() {
        assertEquals(true, Validation.matchingEmail("email@gmail.com", "email@gmail.com"));
        assertEquals(true, Validation.matchingEmail("testEmail@mail.com", "testEmail@mail.com"));
        assertEquals(true, Validation.matchingEmail("alexdconroy@hotmail.com", "alexdconroy@hotmail.com"));
        assertEquals(true, Validation.matchingEmail("joebloggs@gmail.com", "joebloggs@gmail.com"));
    }

    @Test
    public void matchingEmailShouldBeFalse() {
        assertEquals(false, Validation.matchingEmail("email@gmail.com", "email@mail.com"));
        assertEquals(false, Validation.matchingEmail("testEmail@mail.com", "tesmail@mail.com"));
        assertEquals(false, Validation.matchingEmail("alexdconroy@hotmail.com", "alexdconroy@mail.com"));
        assertEquals(false, Validation.matchingEmail("joebloggs@gmail.com", "joeloggs@gmail.com"));
    }



    @Test
    public void emailShouldBeTrue() {
        assertEquals(true, Validation.validEmail("email@gmail.com"));
        assertEquals(true, Validation.validEmail("alex.conroy8@mail.dcu.ie"));
        assertEquals(true, Validation.validEmail("testEmail@mail.com"));
        assertEquals(true, Validation.validEmail("alexdconroy@hotmail.com"));
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
        assertEquals(true, Validation.validUsername("joe_bloggs@_$?"));
    }

    @Test
    public void usernameShouldBeFalse() {
        assertEquals(false, Validation.validUsername(""));
        assertEquals(false, Validation.validUsername("92conroy"));
        assertEquals(false, Validation.validUsername("conroy 92"));
        assertEquals(false, Validation.validUsername("_conroy92"));
    }

    @Test
    public void nameShouldBeTrue() {
        assertEquals(true, Validation.validName("Alex Conroy"));
        assertEquals(true, Validation.validName("Qun Liu"));
        assertEquals(true, Validation.validName("Mark"));
        assertEquals(true, Validation.validName("Joe Bloggs"));
        assertEquals(true, Validation.validName("Peter Müller"));
        assertEquals(true, Validation.validName("Patrick O'Brian"));
        assertEquals(true, Validation.validName("François Hollande"));
    }

    @Test
    public void nameShouldBeFalse() {
        assertEquals(false, Validation.validName(""));
        assertEquals(false, Validation.validName("A"));
        assertEquals(false, Validation.validName("AC"));
        assertEquals(false, Validation.validName("Alex Conroyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy"));
        assertEquals(false, Validation.validName("Se@n M@acken"));
        assertEquals(false, Validation.validName(""));
    }


}