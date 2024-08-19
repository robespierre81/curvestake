package com.bodiva.curvestake.consensus;

import com.bodiva.curvestake.blockchain.Block;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProofOfStake {
    private List<Stakeholder> stakeholders;

    public ProofOfStake() {
        this.stakeholders = new ArrayList<>();
    }

    public void addStakeholder(Stakeholder stakeholder) {
        stakeholders.add(stakeholder);
    }

    public Stakeholder selectValidator() {
        int totalStake = stakeholders.stream().mapToInt(Stakeholder::getStake).sum();
        int stakeSum = 0;
        int selectedIndex = new Random().nextInt(totalStake);

        for (Stakeholder stakeholder : stakeholders) {
            stakeSum += stakeholder.getStake();
            if (selectedIndex < stakeSum) {
                return stakeholder;
            }
        }
        return null;
    }

    public boolean validateBlock(Block block) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
