package refactula.introduction_to_algorithms.utils.array;

class IntArray extends AbstractArray<Integer> {
    private final int[] delegate;

    IntArray(int[] delegate) {
        this.delegate = delegate;
    }

    @Override
    public int length() {
        return delegate.length;
    }

    @Override
    public void set(int index, Integer value) {
        delegate[index] = value;
    }

    @Override
    public Integer get(int index) {
        return delegate[index];
    }
}
