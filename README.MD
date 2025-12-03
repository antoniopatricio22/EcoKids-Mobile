# EcoKids v2

##  Sobre o Projeto

EcoKids Mobile é um jogo educativo para Android que ensina crianças sobre reciclagem através de uma jogabilidade simples e recompensadora: o jogador classifica itens nas lixeiras corretas dentro de um tempo limitado.

Este repositório contém a versão reorganizada do app com persistência local (SQLite), gerenciamento de jogadores, pontuações e um sistema de conquistas.

---

##  Funcionalidades Principais

- Gameplay intuitivo: seleciona-se um item e escolhe a lixeira correta.
- Cronômetro (60s) por partida.
- Feedback visual e sonoro em tempo real.
- Armazenamento local de jogadores, pontuações e conquistas (SQLite).
- Diálogo de seleção de jogador com validações (nome único, tamanho e caracteres).
- Tela de resultado que processa e salva conquistas.
- Interface organizada com RecyclerView e GridLayout.

---

##  Arquitetura (resumo)

- Linguagem: Java
- Padrão: separação por pacotes (activities, dialogs, database, models, adapters, utils)
- Persistência: SQLite via `DatabaseHelper` (tabelas `players`, `game_scores`, `achievements`)

### Banco de Dados (resumo)

- `players`: id (PK), name (UNIQUE), created_at
- `game_scores`: id (PK), player_id (FK), score, error_count, game_time, created_at
- `achievements`: id (PK), player_id (FK), name, description, icon, unlocked_at, UNIQUE(player_id, name)

Arquivo físico no dispositivo/emulador: `/data/data/com.example.ecokids_v2/databases/ecokids_v2.db`

---

##  Estrutura de Pacotes (atual)

```
com.example.ecokids_v2/
├── activities/      # Telas do app (Main, Game, Score, etc.)
├── adapters/        # Adapters (RecyclerView)
├── dialogs/         # Dialogs (GameSelectionDialog)
├── models/          # Modelos (Player, GameScore, Achievement)
├── database/        # DatabaseHelper (SQLite)
└── utils/           # Utilitários e helpers
```

---

##  Fluxo de Jogo (resumido)

1. `MainActivity` → jogador inicia jogo.
2. `GameSelectionDialog` valida nome e obtém/ cria `Player` no DB.
3. `GameActivity` recebe `player_id` e executa a partida.
4. Ao final, `GameActivity` salva `GameScore` e chama `ScoreActivity`.
5. `ScoreActivity` processa critérios e grava conquistas correspondentes.

---

## Validações Implementadas

- Nome do jogador: não vazio, 2–50 caracteres, caracteres permitidos (letras, números, espaço, hífen).
- Verifica duplicidade no DB (nome único) e reutiliza o `player_id` se já existir.
- Salva pontuação e evita duplicação de conquista via `UNIQUE(player_id, name)`.

Conquistas ativas (correspondem aos 3 cards atuais):
- `Campeão da Reciclagem`: 0 erros + 25 acertos
- `Reciclador Nota 100`: 25 acertos
- `Velocista Sustentável`: tempo < 60s

---

## Como Executar (rápido)

1. Clone o repositório:

```bash
git clone https://github.com/antoniopatricio22/EcoKids-mobile.git
```

2. Abra no Android Studio e sincronize dependências.
3. Execute em um emulador ou dispositivo (Run 'app').

Para inspecionar o banco de dados no emulador, use o Device File Explorer do Android Studio ou `adb`:

```pwsh
adb exec-out run-as com.example.ecokids_v2 cat databases/ecokids_v2.db > ecokids_v2.db
```


---

## Equipe

- Antônio Patrício Macena de Barros
- Brunno Barbosa de Lima Assunção
- João Ricardo Queiroz Calado
- Lucas de Araújo dos Prazeres
- Nayane Maria Lopes dos Santos
- Paulo Thiago Santos da Silva

UNIT-PE
Análise e Desenvolvimento de Sistemas
Desenvolvimento Mobile

Recife, Dezembro de 2025
---
