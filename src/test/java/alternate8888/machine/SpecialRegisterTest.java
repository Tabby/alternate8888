package alternate8888.machine;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SpecialRegisterTest {

  SpecialRegister sr = new SpecialRegister();

  @Test
  public void testAdvance0() {
    assertEquals(0, sr.get());
    sr.advance();
    assertEquals(1, sr.get());
  }

  @Test
  public void testAdvance255() {
    sr.set(255);
    sr.advance();
    assertEquals(256, sr.get());
  }

  @Test
  public void testAdvance65535() {
    sr.set(65535);
    sr.advance();
    assertEquals(0, sr.get());
  }
}
