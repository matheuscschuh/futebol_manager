# ⚽ Futebol Manager

Jogo de tabuleiro estratégico para o terminal, inspirado no futebol e escrito em **Java puro**, com as estruturas de dados implementadas do zero.

Cada jogador é um gestor: compra clubes, cobra taxa de mando de campo de quem visita os seus estádios, paga folha salarial, tira cartas de Sorte/Revés e tenta não ir à falência. Vence quem terminar com o maior patrimônio.

## Destaques técnicos

- **Lista duplamente ligada circular** como tabuleiro: os jogadores andam nó a nó, para frente e para trás, sem índices.
- **Pilhas** para o baralho e o descarte, com embaralhamento pelo algoritmo de **Fisher-Yates**.
- **Fila com capacidade limitada** para o histórico de jogadas (descarta a entrada mais antiga quando lota).
- **Ordenação manual** (bubble sort) para a classificação final.
- Regras de economia completas: valorização dos clubes a cada visita, venda forçada de clubes e falência.
- Entrada de dados validada, modo de teste (dados e cartas escolhidos) e modo automático com semente configurável, para partidas reproduzíveis.
- Nenhuma estrutura pronta da biblioteca (`LinkedList`, `Stack`, `Queue`...) e nenhuma dependência externa.

## Como executar

Requisito: **Java 17** ou superior.

**Pelo terminal**, na pasta do projeto:

```bash
javac -d out $(find src -name "*.java")
java -cp out Main
```

No Windows (PowerShell):

```powershell
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out Main
```

**Pelo IntelliJ IDEA:** abra a pasta do projeto e execute a classe `Main`.

Para uma partida rápida: no menu principal, escolha **4** (carregar dados de teste) e depois **5** (iniciar partida).

## Estrutura do projeto

```
src/
├── estruturas/   # Nó, lista dupla circular, pilha e fila (implementação própria)
├── modelo/       # Clube, Jogador, Casa, Carta, entrada do histórico e enums
├── jogo/         # Regras: tabuleiro, baralho, turno, pagamentos, ranking, configurações
├── ui/           # Menus, leitura validada do teclado e formatação de valores
└── Main.java
```

## Documentação

A documentação completa, com as regras, as justificativas das estruturas e os screenshots, está em [`DOCUMENTACAO.md`](DOCUMENTACAO.md).

---

Desenvolvido por **Matheus Schuh** como projeto da disciplina de Algoritmos e Estruturas de Dados (ADS — ULBRA Torres).
