package alternate8888.machine;

/**
 * @author Tabby Cromarty
 */
public class CPU {

  private static final int WORD_BITS = 16;

  private final Register instructionRegister = new Register8Bit();
  private final Register accumulator = new Register8Bit();
  private final Register regH = new Register8Bit();
  private final Register regL = new Register8Bit();
  private final Register regD = new Register8Bit();
  private final Register regE = new Register8Bit();
  private final Register regB = new Register8Bit();
  private final Register regC = new Register8Bit();
  private final Register regPairB = new RegisterPair(regB, regC);
  private final Register regPairD = new RegisterPair(regD, regE);
  private final Register regPairH = new RegisterPair(regH, regL);

  private final StatusBitRegister statusBits = new StatusBitRegister();
  private final Register16Bit programCounter = new Register16Bit();
  private final Register stackPointer = new Register16Bit();
  private final Memory ram = new Memory();

  private volatile Boolean interruptsEnabled = Boolean.FALSE;

  private void stackPush(final Register16Bit register) {
    stackPush(register.high, register.low);
  }

  private void stackPush(final RegisterPair pair) {
    stackPush(pair.getHigh(), pair.getLow());
  }

  private void stackPush(final int high,
                         final int low) {
    decrement(stackPointer);
    ram.set(stackPointer, high);
    decrement(stackPointer);
    ram.set(stackPointer, low);
  }

  private void checkAndSetStatusBits(final Register register) {
    statusBits.assignSign((register.get() & 0x80) > 0);
    statusBits.assignZero(register.get() == 0);
    statusBits.assignParity(getParity(register.get(), register.getWidth()));
  }

  private void increment(final Register register) {
    register.set(increment(register.get(), register.getWidth()));
  }

  private int increment(int data,
                        final int bits) {
    if (data == (bits == 8 ? 0xff : 0xffff)) {
      data = 0;
    } else {
      statusBits.assignAuxCarry((bits == 8) && ((data & 0x0F) == 0x09));
      data++;
    }
    return data;
  }

  private void decrement(final Register register) {
    register.set(decrement(register.get(), register.getWidth()));
  }

  private int decrement(int data,
                        final int bits) {
    if (data == 0x00) {
      data = (bits == 8 ? 0xff : 0xffff);
    } else {
      statusBits.assignAuxCarry((bits == 8) && ((data & 0x0F) == 0x10));
      data--;
    }
    return data;
  }

  private boolean getParity(final int data,
                            final int bits) {
    int parityCount = 0;
    for (int i = bits - 1; i >= 0; i--) {
      if ((data & i) > 0) {
        parityCount++;
      }
      if ((parityCount % 2) != 0) {
        return true;
      }
    }
    return false;
  }

  ////////////////////////////////
  ///// COMMAND INSTRUCTIONS /////
  ////////////////////////////////

  //////////////////////
  // I/O Instructions //
  //////////////////////

  /**
   * IN (INPUT) - An 8-bit data byte is loaded from the specified external device
   * into the Accumulator.
   *
   * Subsequent instruction byte is Device No.
   *
   * Status Bits: Unaffected
   */
  private void input() {
    // TODO: Implement simulated external devices
    increment(programCounter);
    // int device = ram.get(programCounter);
    // accumulator.set(0);
  }

  /**
   * OUT (OUTPUT) - An 8-bit data byte is loaded from the Accumulator into the
   * specified output device.
   *
   * Subsequent instruction byte is Device No.
   *
   * Status Bits: Unaffected
   */
  private void output() {
    // TODO: Implement simulated external devices
    increment(programCounter);
  }

  ////////////////////////////
  // Interrupt Instructions //
  ////////////////////////////

  /**
   * EI (ENABLE INTERRUPTS) - Sets the interrupt flip-flop. This alerts the
   * computer to the presence of interrupts and causes it to respond accordingly.
   *
   * Status Bits: Unaffected
   */
  private void enableInterrupts() {
    interruptsEnabled = true;
  }

  /**
   * DI (DISABLE INTERRUPTS) - Unsets the interrupt flip-flop. This causes the
   * computer to ignore any subsequent interrupt signals.
   *
   * Status Bits: Unaffected
   */
  private void disableInterrupts() {
    interruptsEnabled = false;
  }

  /**
   * HLT (HALT INSTRUCTION) - Implementation of the HLT instruction steps the
   * Program Counter to the next instruction address and stops the computer until
   * an interrupt occurs.
   *
   * Status Bits: Unaffected
   */
  private void haltInstruction() {
    increment(programCounter);
    synchronized (interruptsEnabled) {
      try {
        interruptsEnabled.wait();
      } catch (final InterruptedException ignored) {
      }
    }
  }

  /**
   * RST (RESTART INSTRUCTION) - The data byte in the Program Counter is pushed
   * onto the stack. This provides an address for subsequent use by a RETURN
   * instruction. Program execution then continues at memory address: 00 000 000
   * 00 (exp) 000 where exp ranges from 000 to 111.
   *
   * The RST instruction is normally used to service interrupts. The external
   * device may cause a RST instruction to be executed during an interrupt.
   * Implementation of RST then calls a special purpose subroutine which is stored
   * in up to eight 8-bit bytes in the lower 64 words of memory. A RETURN
   * instruction is included to return the computer to the original program.
   *
   * Status Bits: Unaffected
   *
   * Example: Assume the following RST instruction is present: 11 001 111.
   * Implementation of the instruction will cause the Program Counter data byte to
   * be pushed onto the stack. The program will then continue execution at the
   * subroutine located at memory address: 00 000 000 00 001 000. Upon completion
   * of the subroutine, a RETURN instruction will return the computer to the next
   * step in the main program.
   */
  private void restartInstruction(final int exp) {
    stackPush(programCounter);
    programCounter.set(exp);
  }

  ////////////////////////////
  // Carry Bit Instructions //
  ////////////////////////////

  /**
   * CMC (COMPLEMENT CARRY) - The Carry Bit is complemented. If it is initially 0,
   * it is set to 1. If it is initially 1, it is reset to 0.
   *
   * Status Bit Affected: Carry
   */
  private void complementCarry() {
    statusBits.toggleCarry();
  }

  /**
   * STC (SET CARRY) - The Carry Bit is set to 1.
   *
   * Status Bit Affected: Carry
   */
  private void setCarry() {
    statusBits.setCarry();
  }

  //////////////////////////////
  // No Operation Instruction //
  //////////////////////////////

  /**
   * NOP (NO OPERATION) - No operation occurs, and the Program Counter proceeds to
   * the next sequential instruction. Program execution then continues.
   *
   * Status Bits: Unaffected.
   */
  private void noOperation() {
  }

  ////////////////////////////////////////
  ///// SINGLE REGISTER INSTRUCTIONS /////
  ////////////////////////////////////////

  private Register getSingleRegister(final int reg) {
    switch (reg) {
      case 0:
        return regB;
      case 1:
        return regC;
      case 2:
        return regD;
      case 3:
        return regE;
      case 4:
        return regH;
      case 5:
        return regL;
      case 6:
        return ram.address(regPairH);
      case 7:
        return accumulator;
      default:
        throw new RuntimeException("Invalid register number");
    }
  }

  /**
   * INR (INCREMENT REGISTER OR MEMORY) - The specified byte is incremented by
   * one.
   *
   * Status Bits Affected: Zero, Sign, Parity, and Aux Carry
   *
   * Example: Assume the following instruction is present: 00 000 100. According
   * to the table of register bit patterns given above, the byte in register B is
   * to be incremented by 1. If the initial byte is 00 000 000, the incremented
   * byte will be 00 000 01.
   */
  private void incrementRegisterOrMemory(final int reg) {
    final Register register = getSingleRegister(reg);
    increment(register);
    checkAndSetStatusBits(register);
  }

  /**
   * DCR (DECREMENT REGISTER OR MEMORY) - The specified byte is decremented by
   * one.
   *
   * Status Bits Affected: Zero, Sign, Parity, and Aux Carry
   *
   * Example: Assume the following instruction is present: 00 001 101. According
   * to the table of register bit patterns given above, the byte in register C is
   * to be decremented by 1. If the initial byte is 00 000 001, the decremented
   * byte will be 00 000 000.
   */
  private void decrementRegisterOrMemory(final int reg) {
    final Register register = getSingleRegister(reg);
    decrement(register);
    checkAndSetStatusBits(register);
  }

  /**
   * CMA (COMPLEMENT ACCUMULATOR) - Each bit in the accumulator is complemented
   * (1s become 0s and 0s become 1s).
   *
   * Status Bits: Unaffected
   */
  private void complementAccumulator() {
    accumulator.set(accumulator.get() ^ 0xFF);
  }

  /**
   * DAA (DECIMAL ADJUST ACCUMULATOR) - The 8-bit accumulator byte is converted
   * into two 4-bit BCD (binary-coded-decimal) numbers. The instruction is
   * affected by the Aux Carry bit.
   *
   * The DAA instruction performs two operations:
   *
   * 1. If the least significant 4 bits in the accumulator byte (bits 0-3)
   * represent a BCD digit greater than 9 or if the Auxiliary Carry Bit is set to
   * 1, the four bits are automatically incremented by 6. If not the accumulator
   * is unaffected.
   *
   * 2. If the most significant 4 bits in the accumulator byte (bits 4-7)
   * represent a BCD digit greater than 9 or if the Carry Bit is set to 1, the
   * four bits are automatically incremented by 6. If not the accumulator is
   * unaffected.
   *
   * Status Bits Affected: Zero, Sign, Parity, Carry, and Auxiliary Carry
   *
   * Example: Assume the accumulator byte is 10 100 100. The DAA instruction will
   * automatically consider the byte as two 4-bit bytes: 1010 0100. Since the
   * value of the least significant bits is less than 9, the accumulator is
   * initially unaffected. The value of the most significant 4 bits is greater
   * than 9, however, so the 4 bits are incremented by 6 to give 1 0000. The most
   * significant bit sets the Carry Bit to 1, and the accumulator now contains 00
   * 000 100.
   */
  private void decimalAdjustAccumulator() {
    int msn = (accumulator.get() >> 4) & 0x0f;
    int lsn = accumulator.get() & 0x0f;
    if ((lsn > 9) || statusBits.isAuxCarry()) {
      lsn += 6;
      statusBits.assignAuxCarry(lsn > 9);
      lsn &= 0x0f;
    }
    if ((msn > 9) || statusBits.isCarry()) {
      msn += 6;
      statusBits.assignCarry(msn > 9);
      msn &= 0x0f;
    }
    accumulator.set((msn << 4) | lsn);
    checkAndSetStatusBits(accumulator);
  }

  private void executeInstruction() {
    instructionRegister.set(ram.get(programCounter));
    final int instruction = instructionRegister.get();
    switch (instruction) {
      case 0333:
        input();
        break;
      case 0323:
        output();
        break;
      case 0373:
        enableInterrupts();
        break;
      case 0363:
        disableInterrupts();
        break;
      case 0166:
        haltInstruction();
        break;
      case 0307:
      case 0317:
      case 0327:
      case 0337:
      case 0347:
      case 0357:
      case 0367:
      case 0377:
        restartInstruction(instruction & 0070);
        break;
      case 0077:
        complementCarry();
        break;
      case 0067:
        setCarry();
        break;
      case 0000:
        noOperation();
        break;
      case 0004:
      case 0014:
      case 0024:
      case 0034:
      case 0044:
      case 0054:
      case 0064:
      case 0074:
        incrementRegisterOrMemory(instruction & 0070);
        break;
      case 0005:
      case 0015:
      case 0025:
      case 0035:
      case 0045:
      case 0055:
      case 0065:
      case 0075:
        decrementRegisterOrMemory(instruction & 0070);
        break;
      case 0057:
        complementAccumulator();
        break;
      case 0047:
        decimalAdjustAccumulator();
        break;
    }
    increment(programCounter);
  }
}
