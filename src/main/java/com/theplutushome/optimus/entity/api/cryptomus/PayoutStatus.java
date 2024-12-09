package com.theplutushome.optimus.entity.api.cryptomus;

import lombok.Getter;

@Getter
public enum PayoutStatus {
    PROCESS("process", "Payout in process"),
    CHECK("check", "The payout is being verified"),
    PAID("paid", "The payout was successful"),
    FAIL("fail", "Payout failed"),
    CANCEL("cancel", "Payout cancelled"),
    SYSTEM_FAIL("system_fail", "A system error has occurred");

    private final String name;
    private final String definition;

    // Constructor to initialize the enum constants
    PayoutStatus(String name, String definition) {
        this.name = name;
        this.definition = definition;
    }

    // Static method to get the definition by name
    public static String getDefinitionByName(String name) {
        for (PayoutStatus status : PayoutStatus.values()) {
            if (status.name.equalsIgnoreCase(name)) {
                return status.definition;
            }
        }
        return "Unknown status";  // Default message if name not found
    }

    // Static method to get the enum by name
    public static PayoutStatus fromString(String name) {
        for (PayoutStatus status : PayoutStatus.values()) {
            if (status.name.equalsIgnoreCase(name)) {
                return status;
            }
        }
        return null;  // Or throw IllegalArgumentException if desired
    }
}

