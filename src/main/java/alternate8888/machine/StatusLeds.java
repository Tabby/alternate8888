package alternate8888.machine;

/**
 * @author Tabby Cromarty
 */
public enum StatusLeds {
  /**
   * The memory bus will be used for memory read data.
   */
  MEMR,
  /**
   * The address bus containing the address of an input device. The input data
   * should be placed on the data bus when the data bus is in the input mode.
   */
  INP,
  /**
   * The CPU is processing the first machine cycle of an instruction.
   */
  M1,
  /**
   * The address contains the address of an output device and the data bus will
   * contain the output data when the CPU is ready.
   */
  OUT,
  /**
   * A HALT instruction has been executed and acknowledged.
   */
  HLTA,
  /**
   * The address bus holds the stack pointer's pushdown stack address.
   */
  STACK,
  /**
   * Operation in the current machine cycle will be a WRITE memory or OUTPUT
   * function. Otherwise, a READ memory or INPUT operation will occur.
   */
  WO,
  /**
   * An interrupt request has been acknowledged.
   */
  INT
}
