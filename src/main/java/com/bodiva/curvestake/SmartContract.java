package com.bodiva.curvestake;

import com.bodiva.curvestake.blockchain.Hooker;

public interface SmartContract {
    void execute(Hooker transaction);
}
