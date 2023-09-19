//package com.messismo.bar.EntitiesTests;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotEquals;
//
//
//public class AuthenticationResponseTests {
//    @Test
//    public void testAuthenticationResponseGettersAndSetters() {
//        AuthenticationResponse authenticationResponse1 = new AuthenticationResponse();
//        authenticationResponse1.setToken("TOKEN1");
//        AuthenticationResponse authenticationResponse2 = new AuthenticationResponse();
//        authenticationResponse2.setToken("TOKEN1");
//        AuthenticationResponse authenticationResponse3 = new AuthenticationResponse();
//        authenticationResponse3.setToken("TOKEN3");
//
//        assertEquals(authenticationResponse1.getToken(), authenticationResponse2.getToken());
//        assertNotEquals(authenticationResponse1.getToken(), authenticationResponse3.getToken());
//    }
//
//
//    @Test
//    public void testTokenEquals() {
//
//        AuthenticationResponse authenticationResponse1 = new AuthenticationResponse();
//        authenticationResponse1.setToken("TOKEN1");
//        AuthenticationResponse authenticationResponse2 = new AuthenticationResponse();
//        authenticationResponse2.setToken("TOKEN1");
//        AuthenticationResponse authenticationResponse3 = new AuthenticationResponse();
//        authenticationResponse3.setToken("TOKEN3");
//
//        assertEquals(authenticationResponse1, authenticationResponse2);
//        assertNotEquals(authenticationResponse1, authenticationResponse3);
//    }
//
//    @Test
//    public void testMenuHashCode() {
//
//        AuthenticationResponse authenticationResponse1 = new AuthenticationResponse();
//        authenticationResponse1.setToken("TOKEN1");
//        AuthenticationResponse authenticationResponse2 = new AuthenticationResponse();
//        authenticationResponse2.setToken("TOKEN1");
//        AuthenticationResponse authenticationResponse3 = new AuthenticationResponse();
//        authenticationResponse3.setToken("TOKEN3");
//
//        assertEquals(authenticationResponse1.hashCode(), authenticationResponse2.hashCode());
//        assertNotEquals(authenticationResponse1.hashCode(), authenticationResponse3.hashCode());
//    }
//}
