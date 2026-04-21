# Text Comparison System

Sistema de comparação de conteúdo textual para identificar inter-relações entre dois arquivos e calcular um índice de correlação entre seus conteúdos.

## 1. Visão geral

Este projeto tem como objetivo desenvolver um sistema capaz de receber dois documentos textuais, processar seu conteúdo e apresentar métricas de similaridade, correlação e inter-relação entre os textos.

A proposta está alinhada ao tema de TCC **"Desenvolvimento de sistema de comparação de conteúdo de textos"**, cujo objetivo é **buscar as inter-relações entre dois arquivos, buscando o índice de correlação**.

O sistema será construído com:

- **Frontend:** Vaadin
- **Backend:** Spring Boot
- **Banco de dados:** PostgreSQL
- **Linguagem principal:** Java

## 2. Problema

A comparação manual de documentos pode ser lenta, subjetiva e inconsistente, especialmente quando o volume de texto é grande. Em contextos acadêmicos, corporativos e jurídicos, torna-se útil possuir um sistema que:

- compare dois arquivos de forma automática;
- identifique similaridades e diferenças relevantes;
- calcule um índice de correlação textual;
- apresente uma visualização clara para análise humana.

## 3. Objetivo geral

Desenvolver um sistema web para comparar o conteúdo de dois arquivos textuais, identificando inter-relações entre eles e gerando um índice de correlação baseado em métricas de análise textual.

## 4. Objetivos específicos

- permitir upload de dois arquivos para comparação;
- extrair e normalizar o conteúdo textual dos arquivos;
- aplicar estratégias de pré-processamento de texto;
- calcular métricas de similaridade textual;
- exibir índice de correlação em interface visual;
- destacar trechos semelhantes, relacionados e divergentes;
- armazenar histórico de comparações no banco de dados;
- possibilitar futura expansão para múltiplos algoritmos de comparação.

## 5. Casos de uso iniciais

### 5.1 Comparar dois arquivos
O usuário envia dois arquivos e solicita a análise comparativa.

### 5.2 Visualizar índice de correlação
O sistema apresenta um percentual ou score numérico representando a proximidade entre os conteúdos.

### 5.3 Visualizar trechos relacionados
O sistema mostra blocos, sentenças ou termos em comum entre os dois documentos.

### 5.4 Visualizar diferenças
O sistema evidencia conteúdos exclusivos de cada arquivo.

### 5.5 Consultar histórico
O usuário pode revisar comparações realizadas anteriormente.

## 6. Escopo funcional inicial (MVP)

### Entradas
- upload de dois arquivos por vez;
- suporte inicial para **TXT**, **PDF** e **DOCX**.

### Processamento
- extração de texto;
- limpeza de caracteres especiais;
- normalização;
- tokenização;
- remoção opcional de stopwords;
- cálculo de similaridade.

### Saídas
- índice de correlação textual;
- lista de termos em comum;
- visualização de diferenças;
- resumo da comparação;
- persistência do resultado.

## 7. Métricas sugeridas

A versão inicial pode utilizar uma ou mais das métricas abaixo:

- **Jaccard Similarity**: comparação entre conjuntos de termos;
- **Cosine Similarity** com TF-IDF: comparação vetorial entre documentos;
- **Levenshtein Distance**: útil para trechos curtos;
- **N-gram overlap**: comparação por sequências de palavras.

### Recomendação prática para o TCC
Para a primeira versão, recomenda-se implementar:

1. **TF-IDF + Cosine Similarity** como métrica principal;
2. **Jaccard** como métrica complementar;
3. uma fórmula final de **índice de correlação** derivada da composição ponderada dessas métricas.

Exemplo conceitual:

```text
correlationIndex = (0.7 * cosineSimilarity) + (0.3 * jaccardSimilarity)
```

> Os pesos podem ser ajustados com base em testes e validação experimental.

## 8. Arquitetura proposta

O sistema seguirá uma arquitetura em camadas, com separação clara de responsabilidades.

### 8.1 Camadas
- **Presentation Layer**: interface com Vaadin;
- **Application Layer**: orquestração de casos de uso;
- **Domain Layer**: regras de negócio da comparação textual;
- **Infrastructure Layer**: persistência, parsing de arquivos e integrações.

### 8.2 Estilo arquitetural
- monólito modular para simplificar o desenvolvimento do TCC;
- separação por pacotes orientada por domínio;
- possibilidade futura de extração de módulos ou microsserviços.

## 9. Estrutura inicial do projeto

```text
text-comparison-system/
├── .github/
│   └── copilot-instructions.md
├── docs/
│   ├── architecture.md
│   ├── domain-rules.md
│   └── api-contract.md
├── src/
│   ├── main/
│   │   ├── java/com/prodbygus/textcomparison/
│   │   │   ├── application/
│   │   │   ├── domain/
│   │   │   ├── infrastructure/
│   │   │   ├── presentation/
│   │   │   └── config/
│   │   └── resources/
│   └── test/
├── pom.xml
└── README.md
```

## 10. Proposta de organização dos pacotes

```text
com.prodbygus.textcomparison
├── application
│   ├── dto
│   ├── service
│   └── usecase
├── domain
│   ├── model
│   ├── repository
│   ├── service
│   └── valueobject
├── infrastructure
│   ├── parser
│   ├── persistence
│   ├── similarity
│   └── config
├── presentation
│   ├── view
│   ├── component
│   └── api
└── shared
    ├── exception
    └── util
```

## 11. Entidades iniciais

### Document
Representa um arquivo submetido ao sistema.

Campos sugeridos:
- id
- originalFileName
- fileType
- content
- normalizedContent
- uploadedAt

### Comparison
Representa uma comparação entre dois documentos.

Campos sugeridos:
- id
- documentAId
- documentBId
- cosineSimilarity
- jaccardSimilarity
- correlationIndex
- summary
- createdAt

### MatchSegment
Representa trechos ou termos relacionados entre os documentos.

Campos sugeridos:
- id
- comparisonId
- sourceExcerpt
- targetExcerpt
- similarityScore
- category

## 12. Fluxo principal

1. usuário acessa a interface Vaadin;
2. realiza upload de dois arquivos;
3. backend extrai o texto dos arquivos;
4. sistema aplica pré-processamento;
5. motor de comparação calcula métricas;
6. sistema armazena os resultados;
7. interface exibe score, trechos relacionados e diferenças.

## 13. Requisitos funcionais

- RF01: permitir upload de dois arquivos para comparação;
- RF02: validar formato e tamanho dos arquivos;
- RF03: extrair o conteúdo textual dos arquivos;
- RF04: calcular métricas de similaridade;
- RF05: gerar índice de correlação;
- RF06: exibir resultados na interface;
- RF07: armazenar histórico das comparações;
- RF08: permitir consulta de comparações anteriores.

## 14. Requisitos não funcionais

- RNF01: arquitetura simples, modular e evolutiva;
- RNF02: tempo de resposta adequado para documentos pequenos e médios;
- RNF03: código padronizado e de fácil manutenção;
- RNF04: tratamento consistente de erros;
- RNF05: interface limpa e objetiva;
- RNF06: compatibilidade com PostgreSQL.

## 15. Tecnologias previstas

- Java 21+
- Spring Boot
- Spring Data JPA
- Vaadin
- PostgreSQL
- Maven
- Apache PDFBox (PDF)
- Apache POI (DOCX)
- Lombok (opcional)
- Testcontainers (opcional para testes)

## 16. Estratégia de desenvolvimento

### Etapa 1
- configurar projeto Spring Boot + Vaadin;
- configurar PostgreSQL;
- criar entidades iniciais;
- estruturar arquitetura base.

### Etapa 2
- implementar upload de arquivos;
- implementar parsing de TXT, PDF e DOCX;
- persistir documentos.

### Etapa 3
- implementar normalização e pré-processamento;
- implementar cálculo de similaridade.

### Etapa 4
- implementar interface de comparação;
- exibir score e resultados.

### Etapa 5
- implementar histórico;
- realizar testes e refinamentos.

## 17. Ideias para diferencial acadêmico

Para enriquecer o TCC, o sistema pode incluir futuramente:

- comparação por sentenças e não apenas por documento completo;
- pesos diferentes para título, subtítulo e corpo;
- análise semântica com embeddings;
- comparação entre múltiplos arquivos;
- exportação do relatório comparativo em PDF.

## 18. Valor acadêmico do projeto

Este projeto possui relevância por unir:

- engenharia de software;
- processamento de texto;
- modelagem de sistema;
- análise comparativa automatizada;
- visualização de resultados.

Além disso, é um tema aplicável em contextos acadêmicos, empresariais, jurídicos e documentais.

## 19. Como o GitHub Copilot deve ajudar

Os arquivos deste repositório orientam o GitHub Copilot a:

- respeitar a arquitetura em camadas;
- evitar mistura de regras de negócio com UI;
- gerar classes coesas e com responsabilidade única;
- criar código Java idiomático para Spring Boot e Vaadin;
- manter nomes claros e consistentes;
- priorizar legibilidade e testabilidade.

Veja os arquivos:

- `.github/copilot-instructions.md`
- `docs/architecture.md`
- `docs/domain-rules.md`
- `docs/api-contract.md`

## 20. Próximos passos

1. gerar a estrutura inicial do projeto Maven;
2. configurar dependências de Spring Boot, Vaadin e PostgreSQL;
3. criar modelo de domínio;
4. implementar upload e parsing;
5. implementar motor de correlação;
6. construir interface Vaadin.

## 21. Possível título acadêmico

**Desenvolvimento de um sistema de comparação de conteúdo textual para identificação de inter-relações entre documentos e cálculo de índice de correlação**

## 22. Licença

Uso acadêmico e educacional.
