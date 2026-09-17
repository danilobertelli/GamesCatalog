# Além do "Vibe Coding": Como o Conductor e o Context-Driven Development Transformam o Desenvolvimento Android com IA
*(Beyond "Vibe Coding": How Conductor and Context-Driven Development Transform Android Development with AI)*

> **Status:** Draft / Work in Progress  
> **Autor:** Danilo Bertelli  
> **Target Audience:** Android Engineers, Tech Leads, AI & Software Engineering Enthusiasts  
> **Target Formats:** Medium (in-depth technical guide) & LinkedIn (executive summary & key takeaways)  
> **Repositório do Estudo de Caso:** [github.com/danilobertelli/GamesCatalog](https://github.com/danilobertelli/GamesCatalog)

---

## 1. Executive Summary / Resumo Executivo

O desenvolvimento assistido por IA revolucionou a velocidade de escrita de código, mas trouxe um desafio crítico: o chamado *"Vibe Coding"* — programar sem especificações formais, sem arquitetura consistente, gerando código que "parece funcionar", mas acumula débitos técnicos e alucinações.

Neste artigo prático e técnico, apresento a aplicação do **Conductor**, um framework de orquestração baseado em **Context-Driven Development (CDD)** para o ecossistema Antigravity. Demonstro na prática como desenvolvemos o aplicativo Android **GamesCatalog** (Kotlin 2.2, Android SDK 36, Room, Koin e Jetpack Compose), onde cada linha de código foi guiada por especificações formais, ciclo rigoroso de **TDD (Red ➔ Green)** e pontos de controle (*gates*) com aprovação humana obrigatória antes de cada commit.

---

## 2. Estrutura do Artigo / Article Outline

### Introdução: O Dilema da IA no Desenvolvimento de Software
- A armadilha do "código solto" (*Vibe Coding*): por que pedir à IA para "criar um app" gera débitos técnicos imediatos.
- Por que a IA precisa de contexto persistente (regras de produto, stack tecnológica e diretrizes de workflow).
- O que é o **Conductor**: o "maestro" que guia a IA através de Context-Driven Development.

---

### Parte 1: Como Começar com o Conductor no Antigravity / CLI

Nesta seção, mostro como qualquer engenheiro ou equipe de software pode configurar e usar o Conductor do zero dentro do **Google Antigravity (IDE ou CLI)**.

#### 1. O que é o Conductor e onde ele vive?
O Conductor é um plugin/skill oficial de workflow para o Antigravity. Em vez de prompts soltos em janelas de chat efêmeras, o Conductor estrutura todo o ciclo de vida do projeto em arquivos Markdown versionados no Git sob o diretório `conductor/`.

#### 2. Os Comandos e o Fluxo Operacional (Slash Commands):

| Comando | Função Principal | Quando Usar |
| :--- | :--- | :--- |
| `/setup` | Faz o scaffold inicial do ambiente e define os pilares do projeto. | No início do projeto (Greenfield) ou ao adotar o Conductor em projeto existente (Brownfield). |
| `/newTrack` | Cria e planeja uma nova unidade atômica de trabalho (Feature, Bugfix, Chore). | Sempre que for iniciar uma nova tarefa ou funcionalidade. |
| `/implement` | Executa o plano da Track fase por fase com TDD e checagens automáticas. | Durante o desenvolvimento hands-on da funcionalidade. |
| `/status` | Exibe o progresso geral do projeto, tracks ativas e pendências. | A qualquer momento para acompanhamento e alinhamento de progresso. |
| `/review` | Audita o código final implementado contra a especificação e styleguides. | Ao terminar uma track antes do merge final. |
| `/revert` | Desfaz alterações ou reverte passos de uma track com segurança. | Quando uma abordagem técnica precisa ser descartada. |

#### 3. Passo a Passo Prático: Da Inicialização à Execução

##### Passo 1: O Scaffold com `/setup`
Ao disparar `/setup` no prompt do Antigravity, o agente não sai gerando código às cegas; iniciamos um processo de descoberta estruturada conduzido a quatro mãos entre mim e o agente:
1. **Auditoria de Projeto:** Detecta se o repositório é novo (*Greenfield*) ou existente (*Brownfield*).
2. **Definição de Produto (`conductor/product.md`):** Definimos em conjunto o objetivo do app, público-alvo, personas e principais casos de uso.
3. **Diretrizes e Design (`conductor/product-guidelines.md`):** Filosofia de design, tom de voz, experiência do usuário.
4. **Stack Tecnológica (`conductor/tech-stack.md`):** Registro formal das escolhas: linguagem (Kotlin 2.2), minSdk (36), UI (Jetpack Compose), Persistência (Room 2.7), Injeção de Dependências (Koin), Concorrência (Coroutines/Flow).
5. **Workflow e Governança (`conductor/workflow.md`):** Nossas regras de ouro da engenharia: branch strategy, convenção de commits (*Reason, Solution, Test*), TDD obrigatório e proteção de commits (a IA nunca commita sem a minha aprovação prévia).

##### Passo 2: O Planejamento com `/newTrack`
Em vez de pedir "crie a tela de catálogo", usamos `/newTrack "Setup Room Local Storage, Domain Models, Repository, and Koin DI"`.
O Conductor automaticamente:
- Cria o diretório isolado da track: `conductor/tracks/<track_id>/`.
- Gera o **`spec.md`**: Requisitos funcionais, não-funcionais e critérios de aceitação.
- Gera o **`plan.md`**: O plano de execução granular quebrado em **Fases sequenciais** com checkboxes interativos (`[ ]`, `[~]`, `[x]`).
- Gera o **`metadata.json`**: Metadados rastreáveis com timestamp e status da track.
- Registra a nova track no índice central do projeto (`conductor/tracks.md`).

##### Passo 3: A Execução com `/implement`
Com o plano revisado e aprovado por mim, a execução começa:
- **Plan Mode & Tool Execution:** O Antigravity utiliza ferramentas nativas (`write_to_file`, `replace_file_content`, `run_command`) para criar e alterar arquivos com validação a cada etapa.
- **Fase por Fase com TDD:** O agente executa as tarefas sequencialmente, garantindo o ciclo Red ➔ Green para cada componente antes de prosseguir.
- **Verification Gates do Conductor:** Ao término de cada fase, o Conductor inclui uma tarefa obrigatória de verificação manual (`Task: Conductor - User Manual Verification 'Phase X'`), exigindo que eu audite e valide cada entrega antes de avançar.

#### 4. Governança em Camadas: O Segredo para não Perder o Controle da IA
Um dos maiores receios ao utilizar agentes de IA autônomos em bases de código reais é o risco de commits descontrolados ou alterações destrutivas no histórico do Git. 

Aqui entra um padrão fundamental que adotei: a **Governança em Camadas (Layered Governance)**, combinando as regras específicas do projeto com as minhas diretivas globais de ambiente.

| Camada | Onde Reside | Responsabilidade | Exemplo Prático |
| :--- | :--- | :--- | :--- |
| **Camada 1: Minhas Diretivas Globais (Personal Guardrails)** | Configuração global do Antigravity (`GEMINI.md` ou `.gemini/antigravity/rules`) | **Guardrails de Segurança e Políticas Inegociáveis** válidas para todos os meus projetos. | **Guardrail de Commit:** Proíbe estritamente a IA de executar `git commit` sem a minha autorização verbal prévia e expressa. Permite apenas testes e `git add`. |
| **Camada 2: Orquestração do Projeto (Conductor)** | Diretório `conductor/` versionado no repositório (`workflow.md`, `spec.md`, `plan.md`) | **Regras de Negócio, Arquitetura e Fluxo do Projeto.** | **Verification Gates:** Obriga a pausar ao fim de cada fase do plano. Padroniza o formato do commit (*Reason, Solution, Test*). |

> 💡 **Por que essa separação é poderosa?**  
> O Conductor organiza o **ritmo** do desenvolvimento (o *como* e o *quando* avançar), enquanto os Guardrails Globais garantem a **minha soberania** sobre ações críticas (o *botão vermelho* que a IA nunca pode apertar sozinha). O agente formula o commit completo, lista os testes validados, mas quem dá o veredito final sou sempre eu.

---

### Parte 2: Hands-On — Implementando a Track 1 (Persistência Room + Koin DI)
- **Cenário:** Projeto Android moderno (SDK 36, AGP 9.5, Kotlin 2.2).
- **Fase 1: Setup de Dependências e Desafios Reais:**
  - Configuração do Version Catalog (`libs.versions.toml`).
  - Resolução de incompatibilidades reais: KSP2, AGP 9.5 e migração para Room 2.7.0.
  - Alinhamento da JDK do Gradle Daemon (JVM 21) para evitar incompatibilidade de bytecode ASM no Robolectric.
- **Fase 2: Camada de Domínio com TDD:**
  - O que significa o ciclo **Red ➔ Green** na prática com IA:
    - 🔴 **Red:** Criação do teste unitário (`GameTest.kt`) antes de qualquer classe de domínio existir. Verificação da falha no Gradle.
    - 🟢 **Green:** Criação do modelo imutável `Game`, do enum `GameStatus` e da interface `GameRepository`. Testes 100% verdes.
- **Fase 3: Persistência Local (Room):**
  - Implementação de `GameEntity`, `Converters`, `GameDao` e `GamesCatalogDatabase`.
  - Testes em memória com Robolectric e Turbine para validação de `Flow` reativo.
  - Implementação de `GameRepositoryImpl` com mapeamento seguro e despacho em `Dispatchers.IO`.
- **Fase 4: Injeção de Dependências com Koin:**
  - Red Phase: teste de resolução de grafo com Koin (`KoinModulesTest.kt`).
  - Green Phase: criação de `AppModule.kt` e inicialização limpa no `GamesCatalogApplication`.
- **Fase 5: Verificação Global e Finalização da Track:**
  - Execução de `./gradlew testDebugUnitTest` com 100% de sucesso.
  - Atualização do status da track no Conductor e push para o repositório remoto.

---

### Parte 3: Hands-On — Track 2 (UI Reativa com Compose & O "Mundo Real" no Dispositivo)

Após a camada de dados estar 100% testada e blindada, iniciamos a Track 2 (`ui_games_catalog_screen_20260917`) para construir a interface do usuário. Esta fase revelou o verdadeiro poder do pareamento com IA e por que o feedback humano contínuo é insubstituível.

#### 1. TDD na Camada de Apresentação (ViewModel & UI State)
- **O Desafio:** Garantir ordenação alfabética (A-Z) reativa e filtragem instantânea por busca sem tocar na UI.
- **Red Phase:** Criação de `GamesCatalogViewModelTest` com `MainDispatcherRule` (JUnit TestWatcher com `StandardTestDispatcher`) e assertions com Turbine e mocks do repository.
- **Green Phase:** Modelagem de `GamesCatalogUiState` e implementação do `GamesCatalogViewModel` combinando `combine(repository.getAllGames(), searchQuery)` com `stateIn(SharingStarted.WhileSubscribed(5_000))`.

#### 2. Componentes Atômicos & Design System
- Criação de componentes isolados com Previews de Compose:
  - `GameListItem`: Card com placeholder de imagem, título em ellipsis e avaliação formatada.
  - `CatalogSearchBar`: Campo de busca arredondado com ícone de lupa e ação rápida de limpeza.
  - `CatalogEmptyState`: Tratamento visual distinto para "Biblioteca Vazia" vs "Nenhum resultado encontrado".

#### 3. Os Dois "Choques de Realidade": Onde o Olhar Humano Faz Toda a Diferença
Aqui ocorreu um dos momentos mais ricos do estudo de caso:

##### Caso 1: O Bug Visual do Edge-to-Edge no Dispositivo Físico
Com o Android 15 e `enableEdgeToEdge()` ativo no `MainActivity`, a janela do app desenha por padrão sob a barra de status e barra de navegação. 
- **O Problema:** Ao rodar no aparelho físico plugado via ADB, o título *"My Games"* colidiu diretamente com o relógio (`14:03`) e os ícones de bateria da barra de status do sistema.
- **A Resolução Conduzida por IA via ADB:**
  - Ao rodar no meu aparelho conectado, notei o bug visual imediatamente: a top bar estava sobrepondo a status bar.
  - O agente Antigravity capturou um screenshot direto do hardware via `adb shell screencap`, inspecionou o artefato e identificou a ausência de insets no `Column` do `topBar`.
  - Aplicou `Modifier.statusBarsPadding()`, recompilou (`./gradlew installDebug`), enviou o intent de inicialização e capturou novo screenshot comprovando o espaçamento perfeito.

##### Caso 2: A Guardrail de Engenharia — "Nada de Hardcoded Strings"
- Mesmo com a UI visualmente corrigida e testes unitários 100% verdes, notei que os textos ainda estavam literais no código Kotlin.
- **Ajuste Imediato:** Orientei a extração imediata de todas as strings para `res/values/strings.xml`, com suporte a descrições de acessibilidade (`contentDescription`), formatação dinâmica com parâmetros (`%1$d/5`, `%1$s`) e consumo via `stringResource(R.string...)`.
- **A Lição:** A IA pode acelerar a escrita a 1000 km/h, mas a aderência estrita às boas práticas da plataforma depende da nossa sensibilidade técnica como engenheiros que conduzem o processo.

---

---

### Parte 4: Hands-On — Track 3 (Tela de Cadastro de Novo Jogo & O Refinamento de Domínio Pré-UI)

Na Track 3 (`ui_add_game_screen_20260917`), avançamos para a tela de criação de novos jogos, acionada pelo Floating Action Button da tela principal. Aqui aconteceu mais um exemplo fascinante de engenharia colaborativa com IA.

#### 1. A Intervenção de Domínio: "Evitando o Caos de Dados com Plataformas Pré-Cadastradas"
Antes de desenhar qualquer tela ou formulário, notei um risco clássico de modelagem:
> Se deixássemos o campo de plataformas como texto livre, cada usuário digitaria de um jeito: *"PS5"*, *"Playstation 5"*, *"ps 5"*, *"play5"*. Isso destruiria a consistência do banco de dados e inviabilizaria filtros futuros.

- **A Decisão do Engenheiro:** Orientei pausar a UI e adicionar uma fase prévia no Conductor dedicada à criação de uma tabela de `Platform` no Room com 28 plataformas pré-cadastradas (PlayStation, Xbox, Nintendo, PC, Retro, etc.).
- **Execução Estruturada pelo Conductor:**
  1. **Fase 1 (Domain & Pre-seeding):** Criação da entidade `PlatformEntity`, DAO, migração do banco Room para v2 e injeção do repositório no Koin com TDD.
  2. **Fase 2 (Navegação Jetpack Compose):** Adição de `androidx-navigation-compose` e orquestração de rotas limpas (`AppNavHost`).
  3. **Fase 3 (ViewModel & State Machine com TDD):** Testes unitários para validação de título obrigatório, seleção de status, nota (1-5 estrelas) e seleção múltipla de plataformas.
  4. **Fase 4 (Compose UI & Feedback Real no Hardware):**
     - Criação de componentes atômicos: `StarRatingPicker`, `StatusChipGroup` e `PlatformChipGroup` (usando `FlowRow` com chips carregados dinamicamente do Room).
     - 100% dos textos e mensagens de erro extraídos em `strings.xml`.
     - Suporte a teclado virtual com `imePadding()`, scroll vertical e TopAppBar com navegação de retorno.
     - Validação instantânea no aparelho físico conectado via ADB e captura de tela do fluxo completo (do FAB ao formulário preenchido e retorno ao catálogo).

---

### Parte 5: Hands-On — Track 4 (Visualização, Edição & Exclusão de Jogo com Confirmação)

Na Track 4 (`ui_game_detail_screen_20260917`), fechamos o ciclo de gerenciamento de dados local implementando a tela de visualização e edição detalhada de um jogo cadastrado.

#### 1. Requisitos Clássicos de Negócio & Decisões de Design
Ao definir a track, alinhamos regras estritas de edição:
- **Título e Plataformas são Imutáveis (Read-Only):** Exibidos em cards com destaque e chips informativos, garantindo consistência histórica.
- **Campos Editáveis:** Status de progresso (`Want to Play`, `Playing`, `Completed`, `Abandoned`), avaliação em estrelas (1-5) e anotações/resenha do jogador.
- **Ação Explícita de Salvar:** Botão fixo no rodapé com elevação tonal e insets de navegação, persistindo via `GameRepository.upsertGame` e retornando com feedback visual.
- **Ação Crítica de Exclusão:** Ícone de lixeira na TopAppBar acionando um `AlertDialog` de confirmação ("Tem certeza que deseja excluir? Esta ação não pode ser desfeita").

#### 2. TDD & Arquitetura Robusta
- Red Phase em `GameDetailViewModelTest.kt` cobrindo carregamento por ID via `SavedStateHandle`, tratamento de jogo inexistente (`isGameNotFound`), modificação de campos, persistência e exclusão.
- Reuso de componentes atômicos construídos nas tracks anteriores (`StatusChipGroup`, `StarRatingPicker`), acelerando a montagem da tela com 100% das strings em `strings.xml`.
- Compilação limpa e instalação automatizada no dispositivo físico (`./gradlew installDebug`), entregando o app pronto para os meus testes manuais no hardware.

---

### Parte 6: Principais Lições Aprendidas (Key Takeaways)
1. **A IA como Pair Programmer Ativo (não gerador passivo):** A discussão em conjunto sobre arquitetura e trade-offs eleva o nível técnico da entrega.
2. **A Verdade dos Testes:** O TDD protege contra alucinações. Se o teste não falhou antes, a IA não provou que o código fez diferença.
3. **Hardware Real Importa:** Nenhuma prévia de layout substitui a execução no aparelho físico com insets, temas e densidades de tela reais.
4. **Governança em Camadas e Guardrails:** A proibição estrita de commits automáticos garante que cada linha enviada ao Git passe pela nossa auditoria consciente antes de entrar no repositório.
5. **Rastreabilidade Absoluta:** Qualquer desenvolvedor novo no projeto consegue abrir a pasta `conductor/tracks/` e entender exatamente o motivo de cada decisão técnica tomada.

---

### Conclusão & Próximos Passos
- O Conductor transforma o desenvolvimento assistido por IA de um "experimento arriscado" em uma engenharia de software previsível, auditável e escalável.
- Próximas tracks planejadas: Consumo da API IGDB / Ktor (Offline-First) e sincronização de dados.

---

## 5. Notas e Trechos de Código para Citar no Artigo

### Exemplo de Estrutura de Commit Conductor
```git
feat(ui): assemble GamesCatalogScreen with localized strings and insets

Reason:
Complete GamesCatalogScreen implementation adhering to Android standards:
prevent status bar overlap with WindowInsets and extract all UI text to
strings.xml.

Solution:
- Extracted all UI texts and content descriptions to strings.xml.
- Applied stringResource across all catalog components and screens.
- Added Modifier.statusBarsPadding() to topBar column for edge-to-edge.
- Added androidx-lifecycle-runtime-compose to dependencies.
- Connected GamesCatalogScreen into MainActivity.
- Completed track ui_games_catalog_screen_20260917.

Test:
- Executed ./gradlew testDebugUnitTest (all passed).
- Installed and validated on connected physical device via adb.
```

### O Ciclo Red ➔ Green na Prática
*(Explicar como o agente roda `.\gradlew testDebugUnitTest`, captura o erro de compilação esperado, implementa a classe e roda novamente até o `BUILD SUCCESSFUL`).*
