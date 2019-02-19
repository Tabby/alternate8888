package alternate8888.machine;

class RegisterPair implements Register {
  private final Register high;
  private final Register low;

  RegisterPair(final Register high,
               final Register low) {
    this.high = high;
    this.low = low;
  }

  @Override
  public int get() {
    return (high.get() << 8) | low.get();
  }

  public int getHigh() {
    return high.get();
  }

  public int getLow() {
    return low.get();
  }

  @Override
  public void set(final int value) {
    high.set((value >> 8) & 0xff);
    low.set(value & 0xff);
  }

  public void setHigh(final int value) {
    high.set(value);
  }

  public void setLow(final int value) {
    low.set(value);
  }

  @Override
  public int getWidth() {
    return 16;
  }
}