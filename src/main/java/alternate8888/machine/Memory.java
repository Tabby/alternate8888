package alternate8888.machine;

/**
 * @author Tabby Cromarty
 */
public class Memory {

  private final MemoryRegister[] bytes;

  public Memory() {
    this(8096);
  }

  public Memory(final int numBytes) {
    bytes = new MemoryRegister[numBytes];
    for (int i = 0; i < numBytes; i++) {
      bytes[i] = new MemoryRegister();
    }
  }

  public Register address(final Register register,
                          final int offset) {
    return bytes[register.get() + offset];
  }

  public Register address(final Register register) {
    return address(register, 0);
  }

  private Register address(final int high,
                           final int low) {
    return bytes[(high << 8) | low];
  }

  public int get(final int high,
                 final int low) {
    return address(high, low).get();
  }

  public int get(final Register register) {
    return address(register).get();
  }

  public void set(final int high,
                  final int low,
                  final int value) {
    address(high, low).set(value);
  }

  public void set(final Register register,
                  final int value) {
    address(register).set(value);
  }

  public static class MemoryRegister implements Register {

    int data;

    @Override
    public int get() {
      return data;
    }

    @Override
    public void set(final int data) {
      this.data = data;
    }

    @Override
    public int getWidth() {
      return 8;
    }

  }
}
