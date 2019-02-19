package alternate8888.machine;

/**
 * @author Tabby Cromarty
 */
public class Register8Bit implements Register {

  private int data;

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
