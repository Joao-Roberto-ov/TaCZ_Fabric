package com.tacz.guns.api.entity;

public class ReloadState {
    /**
     * Sem recarga: -1
     */
    public static final int NOT_RELOADING_COUNTDOWN = -1;

    protected ReloadState.StateType stateType;
    protected long countDown;

    public ReloadState() {
        stateType = StateType.NOT_RELOADING;
        countDown = NOT_RELOADING_COUNTDOWN;
    }

    public ReloadState(ReloadState src) {
        stateType = src.stateType;
        countDown = src.countDown;
    }

    public StateType getStateType() {
        return stateType;
    }

    public void setStateType(StateType stateType) {
        this.stateType = stateType;
    }

    public long getCountDown() {
        if (stateType == StateType.NOT_RELOADING) {
            return NOT_RELOADING_COUNTDOWN;
        }
        return countDown;
    }

    public void setCountDown(long countDown) {
        this.countDown = countDown;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ReloadState reloadState) {
            return reloadState.stateType.equals(stateType) && reloadState.countDown == countDown;
        } else {
            return false;
        }
    }

    public enum StateType {
        NOT_RELOADING,
        EMPTY_RELOAD_FEEDING,
        EMPTY_RELOAD_FINISHING,
        TACTICAL_RELOAD_FEEDING,
        TACTICAL_RELOAD_FINISHING;

        public boolean isReloadingEmpty() {
            return this == EMPTY_RELOAD_FEEDING || this == EMPTY_RELOAD_FINISHING;
        }

        public boolean isReloadingTactical() {
            return this == TACTICAL_RELOAD_FEEDING || this == TACTICAL_RELOAD_FINISHING;
        }

        public boolean isReloading() {
            return isReloadingEmpty() || isReloadingTactical();
        }

        public boolean isReloadFinishing() {
            return this == StateType.EMPTY_RELOAD_FINISHING || this == StateType.TACTICAL_RELOAD_FINISHING;
        }
    }
}