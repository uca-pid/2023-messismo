package com.messismo.bar.DTOsTests;

import com.messismo.bar.DTOs.UserIdDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserIdDTOTests {
    @Test
    public void testUserIdDTOGettersAndSetters() {

        UserIdDTO userIdDTO1 = new UserIdDTO();
        userIdDTO1.setUserId(1L);
        UserIdDTO userIdDTO2 = new UserIdDTO();
        userIdDTO2.setUserId(1L);
        UserIdDTO userIdDTO3 = new UserIdDTO();
        userIdDTO3.setUserId(2L);

        assertEquals(userIdDTO1.getUserId(), userIdDTO2.getUserId());
        assertNotEquals(userIdDTO1.getUserId(), userIdDTO3.getUserId());
    }

    @Test
    public void testUserIdDTOEquals() {

        UserIdDTO userIdDTO1 = new UserIdDTO(1L);
        UserIdDTO userIdDTO2 = new UserIdDTO(1L);
        UserIdDTO userIdDTO3 = new UserIdDTO(2L);

        assertEquals(userIdDTO1, userIdDTO2);
        assertNotEquals(userIdDTO1, userIdDTO3);
    }

    @Test
    public void testUserIdDTOHashCode() {

        UserIdDTO userIdDTO1 = new UserIdDTO(1L);
        UserIdDTO userIdDTO2 = new UserIdDTO(1L);
        UserIdDTO userIdDTO3 = new UserIdDTO(2L);

        assertEquals(userIdDTO1.hashCode(), userIdDTO2.hashCode());
        assertNotEquals(userIdDTO1.hashCode(), userIdDTO3.hashCode());
    }

    @Test
    public void testUserIdDTOWithBuilder() {
        UserIdDTO userIdDTO1 = UserIdDTO.builder().userId(1L).build();

        assertEquals(1L, userIdDTO1.getUserId());
    }

    @Test
    public void testUserIdDTOWithEmptyBuilder() {
        UserIdDTO userIdDTO1 = UserIdDTO.builder().build();

        assertNull(userIdDTO1.getUserId());
    }


}
