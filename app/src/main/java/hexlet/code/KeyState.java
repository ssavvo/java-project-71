package hexlet.code;

public final class KeyState {
    private String state;
    private Object[] values;

    public String getState() {
        return state;
    }

    public Object[] getValues() {
        return values;
    }

    public KeyState(String state, Object[] values) {
        this.state = state;
        this.values = values;
    }
}
