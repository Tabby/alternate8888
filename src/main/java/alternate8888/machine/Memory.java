package alternate8888.machine;

/**
 * @author Tabby Cromarty
 */
public class Memory {

  private final byte[] words;

  public Memory() {
    this(8096);
  }

  public Memory(final int bytes) {
    words = new byte[bytes];
  }

  private int abs(final byte b) {
    return ((b < 0) ? b + 256 : b);
  }

  private int address(final byte high,
                      final byte low) {
    return (abs(high) << 8) | abs(low);
  }

  public byte get(final int high,
                  final int low) {
    return get((byte) high, (byte) low);
  }

  public byte get(final byte high,
                  final byte low) {
    return words[address(high, low)];
  }

  public void set(final int high,
                  final int low,
                  final byte value) {
    set((byte) high, (byte) low, value);
  }

  public void set(final byte high,
                  final byte low,
                  final byte value) {
    words[address(high, low)] = value;
  }
}
