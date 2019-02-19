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

  int getHigh() {
    return high.get();
  }

  int getLow() {
    return low.get();
  }

  void set(final int value) {
    high.set((value >> 8) & 0xff);
    low.set(value & 0xff);
  }

  void setHigh(final int value) {
    high.set(value);
  }

  void setLow(final int value) {
    low.set(value);
  }
}