//  -------------------------------------------- programas aa disposicao para copiar na memoria (vide aux.carga)
public class Programs {

        public static Program fibonacci10 = new Program("Fibonacci10", new Word[] { // mesmo que prog exemplo, so que
                                                                                    // usa r0
                                                                                    // no lugar de r8
                        new Word(Opcode.LDI, 1, -1, 0), new Word(Opcode.STD, 1, -1, 20), // 20 posicao de memoria onde
                                                                                         // inicia a
                                                                                         // serie de fibonacci gerada
                        new Word(Opcode.LDI, 2, -1, 1), new Word(Opcode.STD, 2, -1, 21),
                        new Word(Opcode.LDI, 0, -1, 22),
                        new Word(Opcode.LDI, 6, -1, 6), new Word(Opcode.LDI, 7, -1, 30), new Word(Opcode.LDI, 3, -1, 0),
                        new Word(Opcode.ADD, 3, 1, -1), new Word(Opcode.LDI, 1, -1, 0), new Word(Opcode.ADD, 1, 2, -1),
                        new Word(Opcode.ADD, 2, 3, -1), new Word(Opcode.STX, 0, 2, -1), new Word(Opcode.ADDI, 0, -1, 1),
                        new Word(Opcode.SUB, 7, 0, -1), new Word(Opcode.JMPIG, 6, 7, -1),
                        new Word(Opcode.STOP, -1, -1, -1), // POS
                                                           // 16
                        new Word(Opcode.DATA, -1, -1, -1), new Word(Opcode.DATA, -1, -1, -1),
                        new Word(Opcode.DATA, -1, -1, -1),
                        new Word(Opcode.DATA, -1, -1, -1), // POS 20
                        new Word(Opcode.DATA, -1, -1, -1), new Word(Opcode.DATA, -1, -1, -1),
                        new Word(Opcode.DATA, -1, -1, -1),
                        new Word(Opcode.DATA, -1, -1, -1), new Word(Opcode.DATA, -1, -1, -1),
                        new Word(Opcode.DATA, -1, -1, -1),
                        new Word(Opcode.DATA, -1, -1, -1), new Word(Opcode.DATA, -1, -1, -1),
                        new Word(Opcode.DATA, -1, -1, -1) // ate
                                                          // aqui
                                                          // -
                                                          // serie
                                                          // de
                                                          // fibonacci
                                                          // ficara
                                                          // armazenada
        });

        public static Program fatorial = new Program("Fatorial", new Word[] { // este fatorial so aceita valores
                                                                              // positivos.
                                                                              // nao pode ser zero
                        // linha coment
                        new Word(Opcode.LDI, 0, -1, 6), // 0 r0 é valor a calcular fatorial
                        new Word(Opcode.LDI, 1, -1, 1), // 1 r1 é 1 para multiplicar (por r0)
                        new Word(Opcode.LDI, 6, -1, 1), // 2 r6 é 1 para ser o decremento
                        new Word(Opcode.LDI, 7, -1, 8), // 3 r7 tem posicao de stop do programa = 8
                        new Word(Opcode.JMPIE, 7, 0, 0), // 4 se r0=0 pula para r7(=8)
                        new Word(Opcode.MULT, 1, 0, -1), // 5 r1 = r1 * r0
                        new Word(Opcode.SUB, 0, 6, -1), // 6 decrementa r0 1
                        new Word(Opcode.JMP, -1, -1, 4), // 7 vai p posicao 4
                        new Word(Opcode.STD, 1, -1, 10), // 8 coloca valor de r1 na posição 10
                        new Word(Opcode.STOP, -1, -1, -1), // 9 stop
                        new Word(Opcode.DATA, -1, -1, -1) } // 10 ao final o valor do fatorial estará na posição 10 da
                                                            // memória
        );

        // PA, um programa que le um valor de uma determinada posição (carregada no
        // inicio),
        // se o número for menor que zero coloca -1 no início da posição de memória para
        // saída;
        // se for maior que zero este é o número de valores da sequencia de fibonacci a
        // serem escritos em sequencia a partir de uma posição de memória;
        public static Program pa = new Program("PA", new Word[] { new Word(Opcode.LDI, 2, -1, 1), // r2 = 1
                        new Word(Opcode.LDI, 3, -1, 1), // r3 = 1
                        new Word(Opcode.LDI, 4, -1, 21), // r4 ira percorrer a memoria
                        new Word(Opcode.LDD, 1, -1, 20), // le da posicao 20 e armazena em r1
                        new Word(Opcode.SUBI, 1, -1, 2), // r1 = r1 - 2
                        new Word(Opcode.LDD, 5, -1, 20), // le da posicao 20 e armazena em r5
                        new Word(Opcode.LDI, 7, -1, 19), // armazena em r7 o endereco do fim do programa
                        new Word(Opcode.LDI, 6, -1, 17), // armazena em r6 o endereco do fim do programa caso o numero
                                                         // (n) seja
                                                         // invalido
                        new Word(Opcode.JMPIL, 6, 5, -1), // se r5 < 0 vai para o endereco do fim do programa caso o
                                                          // numero (n) seja
                                                          // invalido, armazenado em r6

                        // Laco Fibonacci comeca aqui
                        new Word(Opcode.JMPIL, 7, 1, -1), // se r1 < 0 vai para o endereco do fim do programa,
                                                          // armazenado em r7
                        new Word(Opcode.STX, 4, 2, -1), // armazena na posicao guardada em r4 o valor de r2
                        new Word(Opcode.ADDI, 4, -1, 1), // incrementa em 1 unidade r4
                        new Word(Opcode.STX, 4, 3, -1), // armazena na posicao guardada em r4 o valor de r3
                        new Word(Opcode.ADD, 3, 2, -1), // r3 = r3 + r2
                        new Word(Opcode.LDX, 2, 4, -1), // r2 = antigo r3
                        new Word(Opcode.SUBI, 1, -1, 1), // decrementa em 1 unidade r1
                        new Word(Opcode.JMP, -1, -1, 9), // volta para o inicio do laco de repeticao

                        new Word(Opcode.LDI, 1, -1, -1), // r1 = -1
                        new Word(Opcode.STD, 1, -1, 20), // salva o valor de r1 no início da posição de memoria para
                                                         // saida
                        new Word(Opcode.STOP, -1, -1, -1), // fim do programa

                        new Word(Opcode.DATA, -1, -1, 10), // 10 termos de fibonacci

                        new Word(Opcode.DATA, -1, -1, -1), // Onde serao armazenados os resultados
                        new Word(Opcode.DATA, -1, -1, -1), new Word(Opcode.DATA, -1, -1, -1),
                        new Word(Opcode.DATA, -1, -1, -1),
                        new Word(Opcode.DATA, -1, -1, -1), new Word(Opcode.DATA, -1, -1, -1),
                        new Word(Opcode.DATA, -1, -1, -1),
                        new Word(Opcode.DATA, -1, -1, -1), new Word(Opcode.DATA, -1, -1, -1),
                        new Word(Opcode.DATA, -1, -1, -1) });

        // PB: dado um inteiro em alguma posição de memória,
        // se for negativo armazena -1 na saída; se for positivo responde o fatorial do
        // número na saída
        public static Program pb = new Program("PB", new Word[] { new Word(Opcode.LDI, 2, -1, 1), // r2 = 1
                        new Word(Opcode.LDI, 3, -1, 13), // armazena em r3 o endereco para o laco de repeticao
                        new Word(Opcode.LDD, 1, -1, 16), // le da posicao 16 e armazena em r1
                        new Word(Opcode.LDD, 5, -1, 16), // le da posicao 16 e armazena em r5
                        new Word(Opcode.LDI, 7, -1, 9), // armazena em r7 o endereco do fim do programa
                        new Word(Opcode.LDI, 6, -1, 10), // armazena em r6 o endereco do fim do programa caso o numero
                                                         // (n) seja
                                                         // invalido
                        new Word(Opcode.JMPIL, 6, 5, -1), // se r5 < 0 vai para o endereco do fim do programa caso o
                                                          // numero (n) seja
                                                          // invalido, armazenado em r6

                        new Word(Opcode.JMPIG, 3, 1, -1), // se r1 > 0 vai para o endereco do laco de repeticao,
                                                          // armazenado em r3
                        new Word(Opcode.STD, 2, -1, 17), // salva o valor de r2 no no início da posição de memoria para
                                                         // saida
                        new Word(Opcode.STOP, -1, -1, -1), // fim do programa

                        new Word(Opcode.LDI, 1, -1, -1), // r1 = -1
                        new Word(Opcode.STD, 1, -1, 17), // salva o valor de r1 no início da posição de memoria para
                                                         // saida
                        new Word(Opcode.STOP, -1, -1, -1), // fim do programa

                        // Laco fatorial comeca aqui
                        new Word(Opcode.MULT, 2, 1, -1), // r2 = r2 * r1
                        new Word(Opcode.SUBI, 1, -1, 1), // decrementa r1 em 1 unidade
                        new Word(Opcode.JMP, -1, -1, 7), // volta para a verificacao de r1 > 0

                        new Word(Opcode.DATA, -1, -1, 7), // fatorial de 7

                        new Word(Opcode.DATA, -1, -1, -1) // Onde sera armazenados o resultado
        });

        // PC: para um N definido (10 por exemplo)
        // o programa ordena um vetor de N números em alguma posição de memória;
        // ordena usando bubble sort
        // loop ate que não swap nada
        // passando pelos N valores
        // faz swap de vizinhos se da esquerda maior que da direita
        public static Program pc = new Program("PC", new Word[] {
                        // populando na memoria
                        // se quiser alterar os 5 valores, basta mudar o k das linhas de LDI, caso
                        // adicione alguma linha
                        // precisara refatorar o codigo, alterando alguns k's (das linhas onde passamos
                        // os pc's) para que o programa nao quebre
                        new Word(Opcode.LDI, 1, -1, 17), // colocando o valor 5 na posicao 60 da memoria, porem vamos
                                                         // considerar
                                                         // como [0]
                        new Word(Opcode.STD, 1, -1, 48), // [0] = 17
                        new Word(Opcode.LDI, 1, -1, 21), new Word(Opcode.STD, 1, -1, 49), // [1] = 21
                        new Word(Opcode.LDI, 1, -1, 4), new Word(Opcode.STD, 1, -1, 50), // [2] = 4
                        new Word(Opcode.LDI, 1, -1, 3), new Word(Opcode.STD, 1, -1, 51), // [3] = 3
                        new Word(Opcode.LDI, 1, -1, 7), new Word(Opcode.STD, 1, -1, 52), // [4] = 7

                        new Word(Opcode.LDI, 1, -1, 48), // passando a posicao [0] p/ o registrador 1 o inicio do vetor
                        new Word(Opcode.STD, 1, -1, 43), // marcando o incio do vetor em memoria
                        new Word(Opcode.STD, 1, -1, 44), // i = incio do vetor em memoria
                        new Word(Opcode.STD, 1, -1, 45), // j = incio do vetor em memoria
                        new Word(Opcode.LDI, 1, -1, 49), // j+1 no r1
                        new Word(Opcode.STD, 1, -1, 46), // j+1 no r1
                        new Word(Opcode.LDI, 1, -1, 53), // fim do vetor no r1
                        new Word(Opcode.STD, 1, -1, 47), // marcando o final do vetor em memoria
                        // inicio for i =0 i<5 i++
                        new Word(Opcode.LDD, 1, -1, 44), // i = posicao do vet
                        // inicio for j =0 j<4 i++
                        new Word(Opcode.LDD, 2, -1, 45), // passando a posicao [0] p/ o registrador 2 j=0 --20
                        new Word(Opcode.LDD, 3, -1, 46), // passando a posicao [1] p/ o registrador 3 j+1
                        // condicional
                        new Word(Opcode.LDX, 4, 2, -1), // passando dado de j para variaver r4
                        new Word(Opcode.LDX, 5, 3, -1), // passando dado de j+1 para variaver r5
                        new Word(Opcode.SUB, 4, 5, -1), // sendo r4 j e r5 j+1, se o resultado for positivo, deve ser
                                                        // trocado (3-2=1
                                                        // (swap), 1-3=-2)
                        new Word(Opcode.LDI, 5, -1, 31), // passa para r5 posicao de onde o vetor e incrementa j
                        new Word(Opcode.JMPIL, 5, 4, -1), // se r4 < 0 entao, pulamos para o passo r5 pulando a troca
                        // troca
                        new Word(Opcode.LDX, 4, 2, -1), // pegando da memoria o valor vet[j]
                        new Word(Opcode.LDX, 5, 3, -1), // pegando da memoria o valor vet[j + 1]
                        new Word(Opcode.SWAP, 4, 5, -1), // trocando os dados de r5 com os de r4
                        new Word(Opcode.STX, 2, 4, -1), // armazenando valor novo em r2
                        new Word(Opcode.STX, 3, 5, -1), // armazenando valor novo e r3
                        // incrementa do j
                        new Word(Opcode.ADDI, 2, -1, 1), // j++ -- pc 31
                        new Word(Opcode.ADDI, 3, -1, 1), // (j+1)++
                        new Word(Opcode.LDD, 4, -1, 47), new Word(Opcode.SUB, 4, 3, -1),
                        new Word(Opcode.LDI, 5, -1, 21), // passa
                                                         // para r5
                                                         // a
                                                         // posicao
                                                         // do
                                                         // inicio
                                                         // do
                                                         // condicional
                        new Word(Opcode.JMPIG, 5, 4, -1),
                        // incrementa do i
                        new Word(Opcode.ADDI, 1, -1, 1), // i++
                        new Word(Opcode.LDD, 4, -1, 47), // passa ultima posicao do vetor p/ r4
                        new Word(Opcode.SUB, 4, 1, -1), // subtrai o valor de r4 pelo de r1
                        new Word(Opcode.LDI, 5, -1, 19), // passa para r5 a posicao do inicio do loop de i
                        new Word(Opcode.JMPIG, 5, 4, -1), // se r4>0 entao vetor ainta possui numeros, entao voltar p/
                                                          // incio do for
                                                          // de I na var r5
                        new Word(Opcode.STOP, -1, -1, -1),

                        new Word(Opcode.DATA, -1, -1, -1), // Onde serao armazenados os resultados
                        new Word(Opcode.DATA, -1, -1, -1), new Word(Opcode.DATA, -1, -1, -1),
                        new Word(Opcode.DATA, -1, -1, -1),
                        new Word(Opcode.DATA, -1, -1, -1), new Word(Opcode.DATA, -1, -1, -1),
                        new Word(Opcode.DATA, -1, -1, -1),
                        new Word(Opcode.DATA, -1, -1, -1), new Word(Opcode.DATA, -1, -1, -1),
                        new Word(Opcode.DATA, -1, -1, -1) });

        // Programa para testar a instrucao TRAP com entrada.
        public static Program testIn = new Program("Test In",
                        new Word[] { new Word(Opcode.LDI, 7, -1, 1), new Word(Opcode.LDI, 8, -1, 4),
                                        new Word(Opcode.TRAP, -1, -1, -1), new Word(Opcode.STOP, -1, -1, -1),

                                        new Word(Opcode.DATA, -1, -1, -1) // Onde sera armazenado o resultado
                        });

        // Programa para testar a instrucao TRAP com saida.
        public static Program testOut = new Program("Test Out",
                        new Word[] { new Word(Opcode.LDI, 7, -1, 2), new Word(Opcode.LDI, 8, -1, 6),
                                        new Word(Opcode.LDI, 1, -1, 800), new Word(Opcode.STD, 1, -1, 6),
                                        new Word(Opcode.TRAP, -1, -1, -1),
                                        new Word(Opcode.STOP, -1, -1, -1),

                                        new Word(Opcode.DATA, -1, -1, -1) // Onde sera armazenado o resultado
                        });
}