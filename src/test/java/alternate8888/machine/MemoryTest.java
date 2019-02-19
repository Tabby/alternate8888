package alternate8888.machine;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MemoryTest {

  private final Memory memory = new Memory();

  @Test
  public void testMemorySetGetH00L00() {
    assertEquals(0, memory.get(0, 0));
    final byte data = 0x12;
    memory.set(0, 0, data);
    assertEquals(data, memory.get(0, 0));
  }

  @Test
  public void testMemorySetGetH00L01() {
    assertEquals(0, memory.get(0, 1));
    final byte data = 0x12;
    memory.set(0, 1, data);
    assertEquals(data, memory.get(0, 1));
  }

  @Test
  public void testMemorySetGetH00LFE() {
    assertEquals(0, memory.get(0, 0xfe));
    final byte data = 0x12;
    memory.set(0, 0xfe, data);
    assertEquals(data, memory.get(0, 0xfe));
  }

  @Test
  public void testMemorySetGetH00LFF() {
    assertEquals(0, memory.get(0, 0xff));
    final byte data = 0x12;
    memory.set(0, 0xff, data);
    assertEquals(data, memory.get(0, 0xff));
  }

  @Test
  public void testMemorySetGetH01L00() {
    assertEquals(0, memory.get(1, 0));
    final byte data = 0x12;
    memory.set(1, 0, data);
    assertEquals(data, memory.get(1, 0));
  }

  @Test
  public void testMemorySetGetH01L01() {
    assertEquals(0, memory.get(1, 1));
    final byte data = 0x12;
    memory.set(1, 1, data);
    assertEquals(data, memory.get(1, 1));
  }

  @Test
  public void testMemorySetGetH01LFE() {
    assertEquals(0, memory.get(1, 0xfe));
    final byte data = 0x12;
    memory.set(1, 0xfe, data);
    assertEquals(data, memory.get(1, 0xfe));
  }

  @Test
  public void testMemorySetGetH01LFF() {
    assertEquals(0, memory.get(1, 0xff));
    final byte data = 0x12;
    memory.set(1, 0xff, data);
    assertEquals(data, memory.get(1, 0xff));
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testMemoryHighByteOutOfRange() {
    memory.get(0x20, 0);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testMemoryHighByteInRangeLowByteOutOfRange() {
    memory.get(0x1f, 0xa0);
  }

  @Test
  public void testMemoryLastByte() {
    assertEquals(0, memory.get(0x1f, 0x9f));
  }

  @Test
  public void testMemorySetGetH01LFESpecialRegister() {
    final SpecialRegister sr = new SpecialRegister();
    sr.set(0x01fe);
    assertEquals(0, memory.get(sr));
    final byte data = 0x12;
    memory.set(sr, data);
    assertEquals(data, memory.get(sr));
  }

}
