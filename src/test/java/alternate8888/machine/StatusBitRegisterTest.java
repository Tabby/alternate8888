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
    status.assignCarry(false);
    assertFalse(status.isCarry());
    status.assignCarry(true);
    assertTrue(status.isCarry());
    status.clearCarry();
    assertFalse(status.isCarry());
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
    status.assignSign(false);
    assertFalse(status.isSign());
    status.assignSign(true);
    assertTrue(status.isSign());
    status.clearSign();
    assertFalse(status.isSign());
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
    status.assignZero(false);
    assertFalse(status.isZero());
    status.assignZero(true);
    assertTrue(status.isZero());
    status.clearZero();
    assertFalse(status.isZero());
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
    status.assignParity(false);
    assertFalse(status.isParity());
    status.assignParity(true);
    assertTrue(status.isParity());
    status.clearParity();
    assertFalse(status.isParity());
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
    status.assignAuxCarry(false);
    assertFalse(status.isAuxCarry());
    status.assignAuxCarry(true);
    assertTrue(status.isAuxCarry());
    status.clearAuxCarry();
    assertFalse(status.isAuxCarry());
  }

}
