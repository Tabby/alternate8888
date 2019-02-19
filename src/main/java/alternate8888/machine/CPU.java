package alternate8888.machine;

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
  private final Memory ram = new Memory();

  private volatile Boolean interruptsEnabled = Boolean.FALSE;

  private void stackPush(final SpecialRegister register) {
    stackPush(register.high, register.low);
  }

  private void stackPush(final RegisterPair pair) {
    stackPush(pair.getHigh(), pair.getLow());
  }

  private void stackPush(final int high,
                         final int low) {
    stackPointer.decrement();
    ram.set(stackPointer, high);
    stackPointer.decrement();
    ram.set(stackPointer, low);
  }

  ///////////////////////////////////
  ///// COMMAND INSTRUCTIONS /////
  ///////////////////////////////////

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
    programCounter.advance();
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
    programCounter.advance();
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
    programCounter.advance();
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
    }
    programCounter.advance();
  }
}
