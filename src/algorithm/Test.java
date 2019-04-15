package algorithm;

public enum Test {
    FIRS(AvailableMoves.class), SECO;

    private Class value;

    Test(Class clazz) { this.value = clazz; }
}
