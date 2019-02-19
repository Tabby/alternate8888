package alternate8888.machine;

/**
 * @author Tabby Cromarty
 */
public class Memory {

  private final int[] words;

  public Memory() {
    this(8096);
  }

  public Memory(final int bytes) {
    words = new int[bytes];
  }

  private int address(final int high,
                      final int low) {
    return (high << 8) | low;
  }

  public int get(final int high,
                 final int low) {
    return words[address(high, low)];
  }

  public int get(final SpecialRegister register) {
    return words[register.get()];
  }

  public void set(final int high,
                  final int low,
                  final int value) {
    words[address(high, low)] = value;
  }

  public void set(final SpecialRegister register,
                  final int value) {
    words[register.get()] = value;
  }
}
