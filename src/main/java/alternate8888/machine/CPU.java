package alternate8888.machine;

/**
 * @author Tabby Cromarty
 */
public class CPU {

  private static final int WORD_BITS = 16;

  private final Register instructionRegister = new Register8Bit();
  private final Register accumulator = new Register8Bit();
  private final Register regB = new Register8Bit();
  private final Register regC = new Register8Bit();
  private final RegisterPair regPairB = new RegisterPair(regB, regC);

  private final Register regD = new Register8Bit();
  private final Register regE = new Register8Bit();
  private final RegisterPair regPairD = new RegisterPair(regD, regE);

  private final Register regH = new Register8Bit();
  private final Register regL = new Register8Bit();
  private final RegisterPair regPairH = new RegisterPair(regH, regL);

  private final StatusBitRegister statusBits = new StatusBitRegister();
  private final RegisterPair regPairPSW = new RegisterPair(statusBits, accumulator);

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

  private void stackPop(final RegisterPair pair) {
    pair.setLow(ram.get(stackPointer));
    increment(stackPointer);
    pair.setHigh(ram.get(stackPointer));
    increment(stackPointer);
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
      statusBits.setAuxCarry();
      lsn &= 0x0f;
    }
    if ((msn > 9) || statusBits.isCarry()) {
      msn += 6;
      statusBits.setCarry();
      msn &= 0x0f;
    }
    accumulator.set((msn << 4) | lsn);
    checkAndSetStatusBits(accumulator);
  }

  //////////////////////////////////////
  ///// REGISTER PAIR INSTRUCTIONS /////
  //////////////////////////////////////

  private RegisterPair getRegisterPair(final int regPair) {
    switch (regPair) {
      case 0:
        return regPairB;
      case 1:
        return regPairD;
      case 2:
        return regPairH;
      case 3:
        return regPairPSW;
      default:
        throw new RuntimeException("Invalid register pair number");
    }
  }

  /**
   * PUSH (PUSH DATA ONTO STACK) - The contents of the specified register pair
   * (rp) are stored in the two bytes of memory at an address indicated by the
   * Stack Pointer. The contents of the first register are PUSHed into the address
   * one less than the address in the Stack Pointer. The contents of the second
   * register are PUSHed into the address two less than the address in the Stack
   * Pointer.
   *
   * If the Status Bit Register and Accumulator (register pair PSW) pair is
   * specified, the first byte PUSHed into memory is the Status Bit Register.
   *
   * After the PUSH instruction is implemented, the Stack Pointer is automatically
   * decremented by two.
   *
   * Status Bits: Unaffected
   */
  private void pushDataOntoStack(final int regPair) {
    stackPush(getRegisterPair(regPair));
  }

  /**
   * POP (POP DATA OFF STACK) - The contents of the specified register pair (rp)
   * are retrieved from the two bytes of memory at an address indicated by the
   * Stack Pointer. The contents of the memory byte at the Stack Pointer address
   * are loaded into the second register of the pair, and the contents of the byte
   * at the Stack Pointer address plus one are loaded into the first register of
   * the pair.
   *
   * If the Status Bit Register and Accumulator (register pair PSW) is specified,
   * the contents of the byte at the stack pointer address plus one are used to
   * set or reset the status bits.
   *
   * After the POP instruction is implemented, the Stack Pointer is automatically
   * incremented by two.
   *
   * Status Bits Affected: None unless register pair PSW is specified.
   */
  private void popDataOffStack(final int regPair) {
    stackPop(getRegisterPair(regPair));
  }

  /**
   * DAD (DOUBLE ADD) - The 16-bit number formed by the two bytes in the specified
   * register pair (rp) is added to the 16-bit number formed by the two bytes in
   * the H and L register pair.
   *
   * Status Bits Affected: Carry
   */
  private void doubleAdd(final int regPair) {
    int result = getRegisterPair(regPair).get() + regPairH.get();
    statusBits.assignCarry(result > 0xffff);
    result &= 0xffff;
    regPairH.set(result);
  }

  /**
   * INX (INCREMENT REGISTER PAIR) - The 16-bit number formed by the two bytes in
   * the specified register pair (rp) is incremented by one.
   *
   * Status Bits: Unaffected
   */
  private void incrementRegisterPair(final int regPair) {
    increment(getRegisterPair(regPair));
  }

  /**
   * DCX (DECREMENT REGISTER PAIR) - The 16-bit number formed by the two bytes in
   * the specified register pair is decremented by one
   *
   * Status Bits: Unaffected
   */
  private void decrementRegisterPair(final int regPair) {
    decrement(getRegisterPair(regPair));
  }

  /**
   * XCHG (EXCHANGE REGISTERS) - The 16-bit number formed by the contents of the H
   * and L registers is exchanged with the 16-bit number formed by the contents of
   * the D and E registers.
   *
   * Status Bits: Unaffected
   */
  private void exchangeRegisters() {
    final int temp = regPairH.get();
    regPairH.set(regPairD.get());
    regPairD.set(temp);
  }

  /**
   * XTHL (EXCHANGE STACK) - The byte stored in the L register is exchanged with
   * the memory byte addressed by the Stack Pointer. The byte stored in the H
   * register is exchanged with the memory byte at the address one greater than
   * that addressed by the stack pointer.
   *
   * Status Bits: Unaffected
   */
  private void exchangeStack() {
    int temp = regL.get();
    Register memByte = ram.address(stackPointer);
    regL.set(memByte.get());
    memByte.set(temp);

    temp = regH.get();
    memByte = ram.address(stackPointer, 1);
    regH.set(memByte.get());
    memByte.set(temp);
  }

  /**
   * SPHL (LOAD SP FROM H AND L) - The 16-bit contents of the H and L registers
   * replace the contents of the Stack Pointer without affecting the contents of
   * the H and L registers.
   *
   * Status Bits: Unaffected
   */
  private void loadSPFromHAndL() {
    stackPointer.set(regPairH.get());
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
        restartInstruction(get3BitParam(instruction));
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
        incrementRegisterOrMemory(get3BitParam(instruction));
        break;
      case 0005:
      case 0015:
      case 0025:
      case 0035:
      case 0045:
      case 0055:
      case 0065:
      case 0075:
        decrementRegisterOrMemory(get3BitParam(instruction));
        break;
      case 0057:
        complementAccumulator();
        break;
      case 0047:
        decimalAdjustAccumulator();
        break;
      case 0305:
      case 0325:
      case 0345:
      case 0365:
        pushDataOntoStack(get2BitParam(instruction));
        break;
      case 0301:
      case 0321:
      case 0341:
      case 0361:
        popDataOffStack(get2BitParam(instruction));
        break;
      case 0011:
      case 0031:
      case 0051:
      case 0071:
        doubleAdd(get2BitParam(instruction));
        break;
      case 0003:
      case 0023:
      case 0043:
      case 0063:
        incrementRegisterPair(get2BitParam(instruction));
        break;
      case 0013:
      case 0033:
      case 0053:
      case 0073:
        decrementRegisterPair(get2BitParam(instruction));
        break;
      case 0353:
        exchangeRegisters();
        break;
      case 0343:
        exchangeStack();
        break;
      case 0371:
        loadSPFromHAndL();
        break;
    }
    increment(programCounter);
  }

  private int get3BitParam(final int num) {
    return (num & 0070) >> 3;
  }

  private int get2BitParam(final int num) {
    return (num & 0060) >> 4;
  }
}
