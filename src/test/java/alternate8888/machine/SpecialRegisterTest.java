package alternate8888.machine;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SpecialRegisterTest {

  Register16Bit sr = new Register16Bit();

  private void increment(final Register register) {
    int data = register.get();
    if (data == 0xffff) {
      data = 0;
    } else {
      data++;
    }
    register.set(data);
  }

  private void decrement(final Register register) {
    int data = register.get();
    if (data == 0x00) {
      data = 0xffff;
    } else {
      data--;
    }
    register.set(data);
  }

  @Test
  public void testAdvance0() {
    assertEquals(0, sr.get());
    increment(sr);
    assertEquals(1, sr.get());
  }

  @Test
  public void testAdvance255() {
    sr.set(255);
    increment(sr);
    assertEquals(256, sr.get());
  }

  @Test
  public void testAdvance65535() {
    sr.set(65535);
    increment(sr);
    assertEquals(0, sr.get());
  }

  @Test
  public void testDecrement1() {
    sr.set(1);
    decrement(sr);
    assertEquals(0, sr.get());
  }

  @Test
  public void testDecrement256() {
    sr.set(256);
    decrement(sr);
    assertEquals(255, sr.get());
  }

  @Test
  public void testDecrement0() {
    sr.set(0);
    decrement(sr);
    assertEquals(65535, sr.get());
  }
}
