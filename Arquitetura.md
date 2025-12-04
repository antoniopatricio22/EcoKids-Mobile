## Arquitetura do Banco de Dados

```
┌─────────────────────────────────────────────────────────┐
│                    DATABASE (SQLite)                    │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │   PLAYERS    │  │ GAME_SCORES  │  │ACHIEVEMENTS  │   │
│  ├──────────────┤  ├──────────────┤  ├──────────────┤   │
│  │ id (PK)      │  │ id (PK)      │  │ id (PK)      │   │
│  │ name (UNIQ)  │◄─┤ player_id(FK)│  │ player_id(FK)│◄- ┤
│  │ created_at   │  │ score        │  │ name         │   │
│  │              │  │ error_count  │  │ description  │   │
│  │              │  │ game_time    │  │ icon         │   │
│  │              │  │ created_at   │  │ unlocked_at  │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
│        │                   │                 │          │
│        └───────────────────┴─────────────────┘          │
│      Relacionamentos: 1 Player (N) Scores/Achievements  │
│                                                         │
└─────────────────────────────────────────────────────────┘
```
---

## Fluxo de Jogo Completo

```
┌─────────────────┐
│   MainActivity  │ (Menu Principal)
└────────┬────────┘
         │ Clica "Jogar"
         ▼
┌─────────────────────────────────────────┐
│   GameSelectionDialog                   │
├─────────────────────────────────────────┤
│ 1. Valida entrada do nome               │
│    - Não vazio                          │
│    - 2-50 caracteres                    │
│    - Sem caracteres especiais           │
│ 2. Verifica se existe no BD             │
│ 3. Se não existe → Cria novo Player     │
│ 4. Se existe → Reutiliza ID             │
└────────┬────────────────────────────────┘
         │ Inicia com player_id
         ▼
┌──────────────────┐
│   GameActivity   │ (Jogabilidade)
├──────────────────┤
│ - Recebe player_id
│ - Joga normalmente
│ - Ao fim: Cria GameScore
│           Salva no BD
└────────┬─────────┘
         │ Passa dados do score
         ▼
┌──────────────────────────────────┐
│   ScoreActivity                  │
├──────────────────────────────────┤
│ 1. Recebe score, error_count, time
│ 2. Processa conquistas:          │
│    - Verifica critérios          │
│    - Desbloqueia novas           │
│    - Salva no BD                 │
│ 3. Exibe histórico de conquistas │
└──────────┬───────────────────────┘
           │
           ▼
    [Voltar a MainActivity]
```

---

## Sistema de Conquistas

| Conquista | Critério | Ícone |
|-----------|----------|-------|
| Campeão da Reciclagem | 0 erros + 25 acertos | 
| Reciclador Nota 100 | 25 acertos (com erros) |
| Velocista Sustentável | Tempo < 60s |


---


## Schemas de Tabelas

### Tabela: players
```sql
CREATE TABLE players (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE,
    created_at LONG NOT NULL
)
```

### Tabela: game_scores
```sql
CREATE TABLE game_scores (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    player_id INTEGER NOT NULL,
    score INTEGER NOT NULL,
    error_count INTEGER NOT NULL,
    game_time LONG NOT NULL,
    created_at LONG NOT NULL,
    FOREIGN KEY(player_id) REFERENCES players(id) ON DELETE CASCADE
)
```

### Tabela: achievements
```sql
CREATE TABLE achievements (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    player_id INTEGER NOT NULL,
    name TEXT NOT NULL,
    description TEXT,
    icon TEXT,
    unlocked_at LONG NOT NULL,
    UNIQUE(player_id, name),
    FOREIGN KEY(player_id) REFERENCES players(id) ON DELETE CASCADE
)
```

