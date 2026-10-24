# RotaCerta - Gestão de Coleta e Entrega de Cargas

Sistema em Java puro que simula o setor de operações de uma transportadora, do recebimento da carga até a entrega ao cliente. Desenvolvido como trabalho final da disciplina de **Padrões de Projeto**, aplicando 6 padrões GoF (2 criacionais, 2 estruturais e 2 comportamentais) a regras de negócio reais do transporte rodoviário de cargas.

> **Status:** em desenvolvimento

## Integrantes

| Integrante | Responsabilidade |
|---|---|
| Rafael | Padrões estruturais (Composite e Adapter), serviços, menu interativo e integração |
| Douglas | Padrões criacionais (Factory Method e Builder) e cálculo de frete |
| Jonas | Padrões comportamentais (State e Observer) e ciclo da entrega |

## Padrões de Projeto aplicados

| Padrão | Categoria | Problema de negócio resolvido |
|---|---|---|
| Factory Method | Criacional | Criar o veículo adequado (VUC, Toco ou Carreta) para cada tipo de operação de transporte, validando capacidade, distância e carga perigosa sem `if/switch` espalhados. |
| Builder | Criacional | Montar a ordem de carga, que tem muitos campos opcionais (seguro, escolta, agendamento), validando os obrigatórios e aplicando seguro e escolta automáticos por regra. |
| Composite | Estrutural | Tratar pallets e volumes da mesma forma, somando peso e valor e identificando carga perigosa em qualquer nível da carga. |
| Adapter | Estrutural | Unificar rastreadores de fornecedores diferentes, cada um com seu formato de resposta, em uma posição padronizada do sistema. |
| State | Comportamental | Controlar o ciclo de status da entrega (Aguardando Coleta, Coletada, Em Trânsito, Retida, Entregue, Devolvida) e impedir transições inválidas. |
| Observer | Comportamental | Avisar automaticamente cliente, financeiro e central de monitoramento a cada mudança de status, sem acoplar a entrega a esses setores. |

## Principais regras de negócio

- O peso de um pallet é a soma dos itens mais 25 kg de tara. Uma carga é perigosa se qualquer item dentro dela for perigoso.
- Carga perigosa torna o seguro obrigatório. Carga acima de R$ 300.000,00 exige escolta.
- Cada operação tem seu veículo e seus limites:

| Operação | Veículo | Capacidade | Distância máxima | Carga perigosa | Custo/km |
|---|---|---|---|---|---|
| Urbana | VUC | 3.000 kg | 100 km | Não | R$ 2,50 |
| Regional | Toco | 6.000 kg | 600 km | Sim | R$ 4,20 |
| Rodoviária | Carreta | 30.000 kg | Sem limite | Sim | R$ 6,80 |

- Frete = distância × custo/km + 0,3% do valor da carga (seguro) + R$ 1.200,00 (escolta) + 20% (carga perigosa).
- Uma entrega só pode ser coletada com veículo alocado. Entregue e Devolvida são estados finais.
- Ao ser entregue, o financeiro emite a fatura. Ao ser devolvida, cobra 50% de frete de retorno. Ao ser retida em fiscalização, o monitoramento emite um alerta.

## Estrutura do projeto

```
src/main/java/br/com/rotacerta/
├── Main.java                  # Menu interativo
├── dominio/                   # Entidades do negócio (Cliente, OrdemCarga, Entrega, carga, veículos)
├── padroes/
│   ├── criacional/            # Factory Method
│   ├── estrutural/            # Adapter
│   └── comportamental/        # State e Observer
├── servico/                   # Casos de uso chamados pelo Main
├── excecao/                   # Exceções de regra de negócio
└── util/                      # Log que exibe os padrões em ação
```

Cada classe que participa de um padrão indica seu papel no Javadoc com `@note Papel no Padrão`.

## Tecnologias

- Java 17 (sem frameworks)
- Maven (apenas para a estrutura do projeto, sem dependências)
- IntelliJ IDEA

## Como executar

1. Clone o repositório:
   ```
   git clone https://github.com/SEU_USUARIO/SEU_REPO.git
   ```
2. Abra a pasta no IntelliJ IDEA (o projeto é reconhecido como Maven pelo `pom.xml`).
3. Execute a classe `br.com.rotacerta.Main`.
4. Navegue pelo menu digitando o número das opções.

## Convenções da equipe

- Branch `main` sempre funcionando. Cada integrante trabalha na sua branch: `feature/estruturais`, `feature/criacionais` e `feature/comportamentais`.
- Mensagens de commit no padrão: `feat(builder): valida campos obrigatórios da ordem (RN03)`.
- Integração na `main` via Pull Request.