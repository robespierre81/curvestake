package com.bodiva.curvestake.test;

import com.bodiva.curvestake.ProofOfStake;
import com.bodiva.curvestake.ProofOfStake;
import com.bodiva.curvestake.Stakeholder;
import com.bodiva.curvestake.Stakeholder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProofOfStakeTest {

    private ProofOfStake proofOfStake;

    @BeforeEach
    public void setUp() {
        proofOfStake = new ProofOfStake();
    }

    @Test
    public void testAddStakeholder() {
        Stakeholder stakeholder = new Stakeholder(100);
        proofOfStake.addStakeholder(stakeholder);

        // Validate that the stakeholder was added
        assertNotNull(proofOfStake.selectValidator(), "Validator should be selected from the added stakeholders.");
    }

    @Test
    public void testSelectValidatorWithSingleStakeholder() {
        Stakeholder stakeholder = new Stakeholder(100);
        proofOfStake.addStakeholder(stakeholder);

        // Since there's only one stakeholder, it should always be selected as the validator
        assertEquals(stakeholder, proofOfStake.selectValidator(), "The single stakeholder should be selected as the validator.");
    }

    @Test
    public void testSelectValidatorWithMultipleStakeholders() {
        Stakeholder stakeholder1 = new Stakeholder(100);
        Stakeholder stakeholder2 = new Stakeholder(200);
        Stakeholder stakeholder3 = new Stakeholder(300);

        proofOfStake.addStakeholder(stakeholder1);
        proofOfStake.addStakeholder(stakeholder2);
        proofOfStake.addStakeholder(stakeholder3);

        Stakeholder selectedValidator = proofOfStake.selectValidator();
        assertNotNull(selectedValidator, "A validator should be selected.");

        // Since the selection is random, we can't predict the exact outcome, but we can verify that the selected validator is one of the stakeholders
        assertTrue(selectedValidator == stakeholder1 || selectedValidator == stakeholder2 || selectedValidator == stakeholder3,
                "The selected validator should be one of the added stakeholders.");
    }

    @Test
    public void testSelectValidatorWithMockedRandom() {
        Stakeholder stakeholder1 = new Stakeholder(100);
        Stakeholder stakeholder2 = new Stakeholder(200);
        Stakeholder stakeholder3 = new Stakeholder(300);

        proofOfStake.addStakeholder(stakeholder1);
        proofOfStake.addStakeholder(stakeholder2);
        proofOfStake.addStakeholder(stakeholder3);

        // Mock the Random class to control the random selection
        Random mockRandom = mock(Random.class);
        when(mockRandom.nextInt(600)).thenReturn(250); // Forces selection within the second stakeholder's range

        ProofOfStake proofOfStakeWithMockedRandom = new ProofOfStake() {
            protected Random createRandom() {
                return mockRandom;
            }
        };
        proofOfStakeWithMockedRandom.addStakeholder(stakeholder1);
        proofOfStakeWithMockedRandom.addStakeholder(stakeholder2);
        proofOfStakeWithMockedRandom.addStakeholder(stakeholder3);

        Stakeholder selectedValidator = proofOfStakeWithMockedRandom.selectValidator();
        assertEquals(stakeholder2, selectedValidator, "The second stakeholder should be selected based on the mocked random value.");
    }

    @Test
    public void testSelectValidatorWithNoStakeholders() {
        Stakeholder selectedValidator = proofOfStake.selectValidator();
        assertNull(selectedValidator, "No validator should be selected when there are no stakeholders.");
    }
}
