package alternate8888.machine;

final class Register16Bit implements Register {
  int high;
  int low;

  public int getHigh() {
    return high;
  }

  public int getLow() {
    return low;
  }

  @Override
  public int get() {
    return (high << 8) | low;
  }

  public void setHigh(final int val) {
    high = val & 0xff;
  }

  public void setLow(final int val) {
    low = val & 0xff;
  }

  @Override
  public void set(final int val) {
    setLow(val & 0xff);
    setHigh((val >> 8) & 0xff);
  }

  @Override
  public int getWidth() {
    return 16;
  }
}