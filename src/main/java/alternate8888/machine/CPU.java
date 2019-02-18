package alternate8888.machine;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tabby Cromarty
 */
public class CPU {
  private final Register instructionRegister = new Register();
  private final Register accumulator = new Register();
  private final Register regH = new Register();
  private final Register regL = new Register();
  private final Register regD = new Register();
  private final Register regE = new Register();
  private final Register regB = new Register();
  private final Register regC = new Register();
  private final RegisterPair regPairB = new RegisterPair(regB, regC);
  private final RegisterPair regPairD = new RegisterPair(regD, regE);
  private final RegisterPair regPairH = new RegisterPair(regH, regL);

  private final StatusBitRegister statusBits = new StatusBitRegister();
  private final SpecialRegister programCounter = new SpecialRegister();
  private final SpecialRegister stackPointer = new SpecialRegister();
  private final List<Byte> stack = new ArrayList<>();
}
