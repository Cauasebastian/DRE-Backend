# Demonstração do Resultado do Exercício (DRE) - Backend API

## Introdução
Este projeto fornece uma API para cálculos financeiros, incluindo a Demonstração do Resultado do Exercício (DRE) e o valuation de uma empresa. Ele é projetado para receber dados financeiros detalhados como entradas e retornar análises calculadas, como EBITDA, impostos, lucros líquidos e valor projetado da empresa.

# Instalação
 1. **Navegue para o diretório do backend**:
   ```bash
   cd /caminho/para/o/diretorio/que/contem/o/pom.xml
   ```
 2. Configure o arquivo application.properties para:
- Permitir CORS para o frontend.
- Configurar a conexão com o banco de dados PostgreSQL.
  Exemplo:
  ```bash
  spring.datasource.url=jdbc:postgresql://localhost:5432/dre_database
  spring.datasource.username=seu_usuario
  spring.datasource.password=sua_senha
  spring.jpa.hibernate.ddl-auto=update
  cors.allowed.origins=http://localhost:3000
  ```
1. **Instale as dependências:**:
    ```bash
    mvn clean install
    ```
    
2. **Execute o aplicativo Spring Boot**:
    ```bash
    mvn spring-boot:run
    ```
3. **O backend estará disponível em**:
    ```
    http://localhost:8080
    ```
4. **Acesse a documentação da API no Swagger em**:
    ```
    http://localhost:8080/swagger-ui.html
    ```

# Endpoints
### 1. POST /api/dre/calcular
Esse endpoint calcula a Demonstração do Resultado do Exercício (DRE) para cada ano fornecido. Ele retorna as receitas líquidas, EBITDA, impostos e lucros líquidos para cada ano.

Exemplo de JSON de entrada:
  ```bash
{
  "dreAnualRequests": [
    {
      "ano": 2021,
      "receitas": [
        {
          "modeloReceita": "SaaS",
          "tipoReceita": "Inbound",
          "descricao": "Receita de assinatura",
          "ticketMedio": 5000.00,
          "cac": 500.00,
          "investimentoMkt": 2000.00,
          "conversaoInbound": 0.10,
          "vendasInbound": 50,
          "clientesTotais": 200,
          "cancelamento": 0.05,
          "consultorias": 10,
          "ticketMedioConsultorias": 3000.00,
          "receitaBrutaTotal": 250000.00,
          "comissoes": 12500.00
        }
      ],
      "despesas": [
        {
          "descricao": "AWS",
          "tipoDespesa": "Fixa",
          "valor": 10000.00,
          "comissoes": 0.00,
          "cmv": 5000.00
        }
      ],
      "cmv": 50000.00,
      "depreciacao": 10000.00,
      "taxaImposto": 0.15
    },
    {
      "ano": 2022,
      "receitas": [
        {
          "modeloReceita": "SaaS",
          "tipoReceita": "Outbound",
          "descricao": "Receita de serviço",
          "ticketMedio": 6000.00,
          "cac": 600.00,
          "investimentoMkt": 2500.00,
          "conversaoInbound": 0.12,
          "vendasInbound": 60,
          "clientesTotais": 250,
          "cancelamento": 0.04,
          "consultorias": 12,
          "ticketMedioConsultorias": 3500.00,
          "receitaBrutaTotal": 300000.00,
          "comissoes": 15000.00
        }
      ],
      "despesas": [
        {
          "descricao": "AWS",
          "tipoDespesa": "Fixa",
          "valor": 12000.00,
          "comissoes": 0.00,
          "cmv": 5500.00
        }
      ],
      "cmv": 55000.00,
      "depreciacao": 12000.00,
      "taxaImposto": 0.15
    },
    {
      "ano": 2023,
      "receitas": [
        {
          "modeloReceita": "Consultoria",
          "tipoReceita": "Inbound",
          "descricao": "Consultoria especializada",
          "ticketMedio": 7000.00,
          "cac": 700.00,
          "investimentoMkt": 3000.00,
          "conversaoInbound": 0.13,
          "vendasInbound": 70,
          "clientesTotais": 300,
          "cancelamento": 0.03,
          "consultorias": 15,
          "ticketMedioConsultorias": 4000.00,
          "receitaBrutaTotal": 350000.00,
          "comissoes": 17500.00
        }
      ],
      "despesas": [
        {
          "descricao": "Marketing",
          "tipoDespesa": "Variável",
          "valor": 15000.00,
          "comissoes": 0.00,
          "cmv": 6000.00
        }
      ],
      "cmv": 60000.00,
      "depreciacao": 14000.00,
      "taxaImposto": 0.15
    },
    {
      "ano": 2024,
      "receitas": [
        {
          "modeloReceita": "Consultoria",
          "tipoReceita": "Outbound",
          "descricao": "Consultoria premium",
          "ticketMedio": 8000.00,
          "cac": 800.00,
          "investimentoMkt": 3500.00,
          "conversaoInbound": 0.14,
          "vendasInbound": 80,
          "clientesTotais": 350,
          "cancelamento": 0.02,
          "consultorias": 20,
          "ticketMedioConsultorias": 4500.00,
          "receitaBrutaTotal": 400000.00,
          "comissoes": 20000.00
        }
      ],
      "despesas": [
        {
          "descricao": "Serviços de TI",
          "tipoDespesa": "Fixa",
          "valor": 18000.00,
          "comissoes": 0.00,
          "cmv": 6500.00
        }
      ],
      "cmv": 65000.00,
      "depreciacao": 16000.00,
      "taxaImposto": 0.15
    },
    {
      "ano": 2025,
      "receitas": [
        {
          "modeloReceita": "Assinatura",
          "tipoReceita": "Inbound",
          "descricao": "Receita recorrente",
          "ticketMedio": 9000.00,
          "cac": 900.00,
          "investimentoMkt": 4000.00,
          "conversaoInbound": 0.15,
          "vendasInbound": 90,
          "clientesTotais": 400,
          "cancelamento": 0.01,
          "consultorias": 25,
          "ticketMedioConsultorias": 5000.00,
          "receitaBrutaTotal": 450000.00,
          "comissoes": 22500.00
        }
      ],
      "despesas": [
        {
          "descricao": "Infraestrutura",
          "tipoDespesa": "Fixa",
          "valor": 20000.00,
          "comissoes": 0.00,
          "cmv": 7000.00
        }
      ],
      "cmv": 70000.00,
      "depreciacao": 18000.00,
      "taxaImposto": 0.15
    }
  ],
  "taxaDesconto": 0.10,
  "anosProjecao": 5
}
```
### 2. POST /api/dre/valuation
Este endpoint calcula o valuation total da empresa, projetando fluxos de caixa futuros com base em uma taxa de desconto e uma taxa de crescimento.

#### Corpo da Requisição (JSON)
Use o mesmo JSON da requisição para /api/dre/calcular (acima), que contém as receitas e despesas anuais, bem como a taxaDesconto e anosProjecao.

Exemplo de Resposta
```json
{
  "valorPresenteLiquido": 1002399.08,
  "valorTerminal": 3266267.92,
  "valuationTotal": 4268667.00
}
```

## Tecnologias Utilizadas
- Spring Boot: Framework Java para desenvolvimento rápido e robusto.   
- PostgreSQL: Banco de dados relacional para armazenamento das informações financeiras.
- Swagger: Documentação interativa para APIs.
# Como funciona o sistema

### Variáveis Gerais que Compõem uma DRE

Uma DRE é composta por variáveis que representam receitas, despesas e cálculos intermediários para determinar resultados financeiros, como EBITDA e lucro líquido. No seu modelo, as variáveis principais são:

1. **Receitas:**
- receitaBrutaTotal: soma de todas as receitas brutas (consultorias, assinaturas, etc.).
- comissoes: custos variáveis baseados em vendas ou contratos.
- ticketMedio: valor médio por cliente.
- investimentoMkt: investimento em marketing para aquisição de clientes.

2. **Despesas:**
- cmv (Custo das Mercadorias Vendidas): despesas diretamente relacionadas à produção/venda.
- despesasOperacionais: custos indiretos como aluguel, marketing fixo e folha de pagamento.
- depreciacao: desvalorização de bens ao longo do tempo.
- taxaImposto: percentual de impostos aplicáveis sobre o lucro.

3. **Resultados Intermediários:**
- receitaLiquida: receita bruta menos CMV e comissões.
- ebitda: lucro operacional antes de juros, impostos, depreciação e amortização.
- lucroLiquido: resultado final após impostos e depreciação.

### Variáveis que Podem Ficar Zeradas Sem Impactar nos Cálculos

1. **Receitas:**
- comissoes: se zerado, implica ausência de custos variáveis associados a vendas.
investimentoMkt: se zerado, não haverá custo com marketing.
2. **Despesas:**
- depreciacao: se zerado, indica ausência de bens depreciáveis.
cmv: em alguns modelos, pode ser irrelevante (ex.: serviços intangíveis).
3. **Resultados Intermediários:**
- Valores calculados como receitaLiquida, ebitda, e lucroLiquido não podem ser zerados diretamente, pois dependem de receitas e despesas.

### Campos Dependentes de Variáveis Imputadas

Estes campos são dependentes e calculados a partir de outros valores. Em TypeScript, seria ideal representá-los como "campos derivados", protegendo-os contra entrada direta. Exemplos:

- **receitaBrutaTotal:** calculado como a soma das receitas brutas individuais (ex.: consultorias, assinaturas).
Depende de ticketMedio, consultorias, e outros campos específicos em Receita.
- **receitaLiquida:** calculado como:
 ```bash
   receitaLiquida = receitaBrutaTotal - cmv - comissoes;
   ```
- **despesasOperacionais:** soma de todas as despesas categorizadas como operacionais.
  Calculado a partir de valores em Despesa.
- **ebitda:** calculado como:
   ```bash
   ebitda = receitaLiquida - despesasOperacionais;
   ```
- **impostos:** calculado como:
  ```bash
   impostos = ebitda * taxaImposto;
   ```
  - **lucroLiquido:** calculado como:
  ```bash
   impostos = ebitda * taxaImposto;
   ```

  
