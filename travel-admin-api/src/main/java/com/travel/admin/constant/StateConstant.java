package com.travel.admin.constant;

/**
 * Status constants
 */
public interface StateConstant {
    /**
     * Normal state
     */
    int NORMAL = 0;

    /**
     * Ban status
     */
    int NOT_NORMAL = 1;

    /**
     * CONFIRMED
     */
    int CONFIRMED = 0;

    /**
     * ONHOLD
     */
    int ON_HOLD = 1;

    /**
     * CANCELLED
     */
    int CANCELLED = 2;

    /**
     * Unreviewed status
     */
    int UNAUDITED = 0;

    /**
     * Review status
     */
    int APPROVED_AUDITED = 1;
}
