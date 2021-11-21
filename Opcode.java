// -------------------------------------------------------------------------------------------------------
// --------------------- M E M O R I A -  definicoes de opcode e palavra de memoria ---------------------- 
public enum Opcode {
    DATA, ___, // se memoria nesta posicao tem um dado, usa DATA, se nao usa NULO ___
    JMP, JMPI, JMPIG, JMPIL, JMPIE, JMPIM, JMPIGM, JMPILM, JMPIEM, STOP, // desvios e parada
    ADDI, SUBI, ADD, SUB, MULT, // matematicos
    LDI, LDD, STD, LDX, STX, SWAP, // movimentacao
    TRAP; // chamada de sistema
}