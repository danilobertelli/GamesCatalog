# Games Catalog (Conductor Example) 🎮

Aplicativo Android moderno desenvolvido com **Jetpack Compose** e **Clean Architecture**, construído como projeto de referência para estudos práticos do **Conductor** (metodologia de orquestração ágil orientada a IA com o Antigravity) e base para a escrita de um artigo técnico.

O projeto exemplifica um fluxo completo de engenharia de software moderno: desenvolvimento orientado a especificações e tracks, TDD (Test-Driven Development), persistência local reativa (*offline-first*), e integração com serviços externos via OAuth2 e APIs REST.

---

## 📌 Motivação e Objetivos

Este repositório foi criado com dois propósitos fundamentais:
1. **Estudos Práticos do Conductor**: Validar na prática como a abordagem baseada em *tracks*, especificações granulares (`spec.md`), planos incrementais (`plan.md`) e ciclos rígidos de verificação aceleram o desenvolvimento de software sem comprometer qualidade de código, padrões de commit e estabilidade.
2. **Escrita de Artigo Técnico**: Servir como estudo de caso real demonstrando arquitetura Android moderna, boas práticas de desacoplamento, estratégias de testes automatizados e integração de APIs REST de terceiros com autenticação em nuvem.

---

## 🚀 Funcionalidades

- **Catálogo de Jogos (Home)**:
  - Listagem organizada de jogos com cards modernos em Jetpack Compose.
  - Indicadores visuais de status (*Quero Jogar*, *Jogando*, *Zerado*, *Abandonado*).
  - Exibição de capas de jogos em alta resolução via CDN e avaliação por estrelas (1 a 5).
  - Busca local instantânea com filtragem reativa via `StateFlow`.
- **Cadastro de Jogos Inteligente (Add Game)**:
  - Integração com a API do **IGDB (Internet Game Database)** via Twitch OAuth2.
  - Busca em tempo real com *debounce* de 400ms a partir de 3 caracteres digitados.
  - Pop-up de sugestões do IGDB com pré-visualização de capa, ano de lançamento e plataformas.
  - Preenchimento automático de metadados: título, sinopse, capa oficial e plataformas correspondentes.
  - Seleção manual ou complementar de plataformas (PC, PlayStation, Xbox, Nintendo Switch, etc.).
- **Detalhes e Gerenciamento do Jogo (Detail)**:
  - Visualização da capa com suporte a *aspect ratio* e *placeholders*.
  - Edição de status de progresso, notas e notas de texto.
  - Registro automático de data de conclusão ao marcar o jogo como *Zerado*.
  - Exclusão segura com diálogo de confirmação.
- **Rastreabilidade e Logs**:
  - Logs estruturados (`Log.d` / `Log.e`) em camadas críticas (gerenciamento de tokens OAuth, requisições à API IGDB e operações de persistência) com proteção de segredos.

---

## 🏗 Arquitetura & Tecnologias

O projeto segue os princípios de **Clean Architecture** e diretrizes oficiais de arquitetura de aplicativos Android recomendadas pelo Google:

```
app/
 ├── data/
 │    ├── local/          # Room Database, DAOs, Entidades, TypeConverters e Pré-seeding
 │    ├── remote/         # Retrofit Services, Twitch OAuth2 Manager, IGDB Data Source, DTOs e Interceptors
 │    └── repository/     # Implementações concretas de GameRepository e PlatformRepository
 ├── domain/
 │    ├── model/          # Modelos de domínio puros (Game, Platform, GameStatus, GameSearchResult)
 │    └── repository/     # Contratos e interfaces de repositórios
 └── ui/
      ├── catalog/        # Tela de catálogo, cards de jogos e ViewModel
      ├── addgame/        # Formulário de cadastro, autocomplete IGDB e ViewModel
      ├── detail/         # Tela de detalhes, edição, exclusão e ViewModel
      ├── navigation/     # Jetpack Navigation Compose e destinos de rotas
      └── theme/          # Design System com Material 3, tipografia e cores
```

### Stack Tecnológica
- **Linguagem**: Kotlin 2.0+
- **UI Toolkit**: Jetpack Compose & Material 3
- **Concorrência & Reatividade**: Coroutines & Kotlin Flow (`StateFlow`, `combine`, `WhileSubscribed`)
- **Persistência Local**: Room Database com TypeConverters para listas
- **Networking**: Retrofit 2, OkHttp 3 (com Interceptor de autenticação) e Kotlinx Serialization JSON
- **Imagens**: Coil 3 (Compose Image Loader)
- **Injeção de Dependências**: Service Locator desacoplado (`AppContainer`)
- **Testes Automatizados**: JUnit 4, Kotlinx Coroutines Test, Turbine, Mockito-Kotlin

---

## ⚙️ Configuração do Ambiente e Pré-requisitos

### Pré-requisitos
- **Android Studio**: Ladybug (2024.2+) ou superior
- **JDK**: Versão 21 (recomendado JetBrains Runtime / OpenJDK 21)
- **Android SDK**: Build Tools e Platform 35+

### Configuração de Chaves da API do IGDB
Para habilitar a busca em tempo real de jogos e capas na tela de cadastro, configure suas credenciais de desenvolvedor da Twitch/IGDB no arquivo `local.properties` (localizado na raiz do projeto).

> [!IMPORTANT]
> O arquivo `local.properties` **nunca deve ser enviado ao Git**. Ele já está incluído no `.gitignore`.

1. Acesse o [Twitch Developer Console](https://dev.twitch.tv/console) e registre uma aplicação para obter o **Client ID** e gerar um **Client Secret**.
2. Adicione as seguintes linhas ao seu `local.properties`:
   ```properties
   igdb.clientId=SEU_TWITCH_CLIENT_ID
   igdb.clientSecret=SEU_TWITCH_CLIENT_SECRET
   ```
3. O build Gradle injeta automaticamente essas propriedades no `BuildConfig` de forma segura.

---

## 🛠 Comandos Gradle Úteis

Execute a partir da raiz do projeto no terminal ou PowerShell:

```bash
# Executar a suíte de testes unitários
./gradlew testDebugUnitTest

# Compilar o APK de depuração
./gradlew assembleDebug

# Compilar e instalar diretamente em um dispositivo/emulador conectado
./gradlew installDebug
```

---

## 📋 Metodologia Conductor

Este repositório foi desenvolvido estritamente seguindo o fluxo de trabalho do **Conductor**:

1. **Tracks Granulares**: Cada grande funcionalidade do aplicativo possui sua própria pasta em `conductor/tracks/` contendo:
   - `metadata.json`: Metadados e estado da track (`new`, `in_progress`, `completed`).
   - `spec.md`: Especificação técnica, contratos de API e critérios de aceite.
   - `plan.md`: Lista de fases incrementais e tarefas executadas em TDD.
2. **Registro Centralizado**: O arquivo `conductor/tracks.md` mantém a visão unificada do progresso do projeto.
3. **Commit Standards**: Mensagens de commit padronizadas por tipo e escopo, sem expor segredos locais ou quebrar rastreabilidade.

---

## 📄 Licença

Projeto desenvolvido para fins didáticos, educacionais e de pesquisa tecnológica. Sinta-se livre para estudar, clonar e adaptar.
