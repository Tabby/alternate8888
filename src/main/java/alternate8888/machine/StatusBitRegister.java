package alternate8888.machine;

/**
 * Stores status bits in binary, but allows easy access to the state with getter
 * methods
 *
 * @author Tabby Cromarty
 */
public class StatusBitRegister {

  /**
   * This bit is set to 1 if a carry has occurred. The Carry Bit is usually
   * affected by such operations as addition, subtraction, rotation, and some
   * logical decisions. The bit is set to 0 if no carry occurs.
   */
  private static final byte CARRY_BIT = 0x01;
  /**
   * This bit is set to show the sign of a result. If set to 1, the result is
   * minus; if set to 0 the result is plus. The Sign Bit reflects the condition of
   * the most significant bit in the result (bit 7). This is because an 8-bit byte
   * can contain up to the decimal equivalent of from -128 to +127 if the most
   * significant bit is used to indicate the polarity of the result.
   */
  private static final byte SIGN_BIT = 0x02;
  /**
   * This bit is set to 1 if the result of certain instructions is zero and reset
   * to 0 if the result is greater than zero.
   */
  private static final byte ZERO_BIT = 0x04;
  /**
   * Certain operations check the parity of the result. Parity indicates the odd
   * or even status of the 1 bits in the result. The if there is an even number of
   * 1 bits, the Parity Bit is set to 1, and if there is an odd number of 1 bits,
   * the Parity Bit is set to 0.
   */
  private static final byte PARITY_BIT = 0x08;
  /**
   * If set to 1, this bit indicates a carry out of bit 3 of a result. 0 indicates
   * no carry. This status bit is affected by only one instruction (DAA).
   */
  private static final byte AUX_CARRY_BIT = 0x10;

  private byte status = 0x00;

  private boolean isSet(final byte bit) {
    return (status & bit) != 0;
  }

  private void assign(final byte bit,
                      final boolean set) {
    if (set) {
      set(bit);
    } else {
      clear(bit);
    }
  }

  private void set(final byte bit) {
    status |= bit;
  }

  private void clear(final byte bit) {
    status = (byte) (status & (0x1F ^ bit));
  }

  private void toggle(final byte bit) {
    status ^= bit;
  }

  public boolean isCarry() {
    return isSet(CARRY_BIT);
  }

  public boolean isSign() {
    return isSet(SIGN_BIT);
  }

  public boolean isZero() {
    return isSet(ZERO_BIT);
  }

  public boolean isParity() {
    return isSet(PARITY_BIT);
  }

  public boolean isAuxCarry() {
    return isSet(AUX_CARRY_BIT);
  }

  public void assignCarry(final boolean set) {
    assign(CARRY_BIT, set);
  }

  public void assignSign(final boolean set) {
    assign(SIGN_BIT, set);
  }

  public void assignZero(final boolean set) {
    assign(ZERO_BIT, set);
  }

  public void assignParity(final boolean set) {
    assign(PARITY_BIT, set);
  }

  public void assignAuxCarry(final boolean set) {
    assign(AUX_CARRY_BIT, set);
  }

  public void setCarry() {
    set(CARRY_BIT);
  }

  public void setSign() {
    set(SIGN_BIT);
  }

  public void setZero() {
    set(ZERO_BIT);
  }

  public void setParity() {
    set(PARITY_BIT);
  }

  public void setAuxCarry() {
    set(AUX_CARRY_BIT);
  }

  public void clearCarry() {
    clear(CARRY_BIT);
  }

  public void clearSign() {
    clear(SIGN_BIT);
  }

  public void clearZero() {
    clear(ZERO_BIT);
  }

  public void clearParity() {
    clear(PARITY_BIT);
  }

  public void clearAuxCarry() {
    clear(AUX_CARRY_BIT);
  }

  public void toggleCarry() {
    toggle(CARRY_BIT);
  }

  public void toggleSign() {
    toggle(SIGN_BIT);
  }

  public void toggleZero() {
    toggle(ZERO_BIT);
  }

  public void toggleParity() {
    toggle(PARITY_BIT);
  }

  public void toggleAuxCarry() {
    toggle(AUX_CARRY_BIT);
  }
}
