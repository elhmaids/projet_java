package simulation.environment;

public enum CaseState {
    LIBRE,
    OCCUPIED,

    // free but another robot is moving to it
    // used to prevent two robots moving to the same case
    TARGETED
}
