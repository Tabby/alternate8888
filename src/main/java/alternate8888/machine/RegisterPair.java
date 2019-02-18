package alternate8888.machine;

class RegisterPair {
  private final Register high;
  private final Register low;

  RegisterPair(final Register high,
               final Register low) {
    this.high = high;
    this.low = low;
  }

  int get() {
    return (high.get() << 8) | low.get();
  }

  void set(final int value) {
    high.set((byte) ((value >> 8) & 0xff));
    low.set((byte) (value & 0xff));
  }
}