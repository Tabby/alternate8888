package alternate8888.machine;

final class SpecialRegister implements Register {
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

  void advance() {
    if (low == 0xff) {
      low = 0x00;
      if (high == 0xff) {
        high = 0x00;
      } else {
        high++;
      }
    } else {
      low++;
    }
  }

  void decrement() {
    if (low == 0) {
      low = 0xff;
      if (high == 0) {
        high = 0xff;
      } else {
        high--;
      }
    } else {
      low--;
    }
  }

  @Override
  public int getWidth() {
    return 16;
  }
}