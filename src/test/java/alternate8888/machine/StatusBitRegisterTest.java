package alternate8888.machine;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Tabby Cromarty
 */
public class StatusBitRegisterTest {

  private final StatusBitRegister status = new StatusBitRegister();

  @Test
  public void testCarry() {
    assertFalse(status.isCarry());
    status.setCarry();
    assertTrue(status.isCarry());
    status.toggleCarry();
    assertFalse(status.isCarry());
    status.toggleCarry();
    assertTrue(status.isCarry());
  }

  @Test
  public void testSign() {
    assertFalse(status.isSign());
    status.setSign();
    assertTrue(status.isSign());
    status.toggleSign();
    assertFalse(status.isSign());
    status.toggleSign();
    assertTrue(status.isSign());
  }

  @Test
  public void testZero() {
    assertFalse(status.isZero());
    status.setZero();
    assertTrue(status.isZero());
    status.toggleZero();
    assertFalse(status.isZero());
    status.toggleZero();
    assertTrue(status.isZero());
  }

  @Test
  public void testParity() {
    assertFalse(status.isParity());
    status.setParity();
    assertTrue(status.isParity());
    status.toggleParity();
    assertFalse(status.isParity());
    status.toggleParity();
    assertTrue(status.isParity());
  }

  @Test
  public void testAuxCarry() {
    assertFalse(status.isAuxCarry());
    status.setAuxCarry();
    assertTrue(status.isAuxCarry());
    status.toggleAuxCarry();
    assertFalse(status.isAuxCarry());
    status.toggleAuxCarry();
    assertTrue(status.isAuxCarry());
  }

}
